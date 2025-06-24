// PManageSystem Wizard.java
// Copyright (C) 2025 SimonMihoyo
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

package me.kirara.projectcookie;

import me.kirara.projectcookie.Data.WizardData;
import me.kirara.projectcookie.Exceptions.*;
import me.kirara.projectcookie.DAO.TypeQuery;
import static me.kirara.projectcookie.DAO.CreateMinDBUser.*;
import me.kirara.projectcookie.Data.TypeOption;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Wizard extends BaseWindow implements CreateUI {
    private static final Wizard instance = new Wizard("Wizard", 500, 400,true, false);
    public WizardData data = new WizardData();
    private final List<TypeOption> typeOptions;
    private static  Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            connection = DatabaseInitializer.getInstance().getCon();
        }
        return connection;
    }

    // 声明组件为类的成员变量
    private JCheckBox autoUpdateCheckBox;
    private JCheckBox defaultThemeCheckBox;
    private JTextField userNameField;
    private JPasswordField passwordField;
    private JTextField nickNameField;
    private JPasswordField systemUserPasswordField;
    private JTextField realNameField;
    private JComboBox<TypeOption> typeComboBox;

    public static Wizard getInstance() {
        return instance;
    }

    public Wizard(String title, int width, int height, boolean modal, boolean resizable) {
        super(title, width, height, modal, resizable);
        if (getConnection() == null)
        { // 使用getter
            JOptionPane.showMessageDialog(null, "数据库连接失败！", "错误", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        typeOptions = TypeQuery.getTypeOptions(getConnection());
        JPanel mainPanel = createUI();
        setContentPane(mainPanel);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        // 设置窗口背景颜色
        getContentPane().setBackground(Color.WHITE);
    }

    public JPanel createUI() {
        // 主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JPanel cardPanel = new JPanel(new CardLayout());
        List<JPanel> steps = new ArrayList<>();
        JPanel step1 = step1();
        steps.add(step1);
        JPanel step2 = step2();
        steps.add(step2);
        JPanel step3 = step3();
        steps.add(step3);
        for (int i = 0; i < steps.size(); i++) {
            cardPanel.add(steps.get(i), String.valueOf(i));
        }
        mainPanel.add(cardPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        JButton prevButton = createStyledButton("上一步");
        JButton nextButton = createStyledButton("下一步");
        JButton cancelButton = createStyledButton("取消");
        JButton finishButton = createStyledButton("完成");

        prevButton.setEnabled(false);
        finishButton.setVisible(false);

        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
        final int[] currentStep = {0};

        prevButton.addActionListener(e -> {
            if (currentStep[0] > 0) {
                currentStep[0]--;
                cardLayout.show(cardPanel, String.valueOf(currentStep[0]));
                nextButton.setEnabled(true);
                finishButton.setVisible(false);
                if (currentStep[0] == 0) {
                    prevButton.setEnabled(false);
                }
            }
        });

        nextButton.addActionListener(e -> {
            if (currentStep[0] == 0) {
                data.autoUpdate = autoUpdateCheckBox.isSelected();
                data.defaultTheme = defaultThemeCheckBox.isSelected();
            } else if (currentStep[0] == 1) {
                if (userNameField.getText().isEmpty() || passwordField.getPassword().length == 0) {
                    JOptionPane.showMessageDialog(this, "用户名和密码不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                data.DBusername = userNameField.getText();
                data.DBpassword = passwordField.getPassword();
            }
            if (currentStep[0] < steps.size() - 1) {
                currentStep[0]++;
                cardLayout.show(cardPanel, String.valueOf(currentStep[0]));
                prevButton.setEnabled(true);
                if (currentStep[0] == steps.size() - 1) {
                    nextButton.setEnabled(false);
                    finishButton.setVisible(true);
                }
            }
        });

        finishButton.addActionListener(e -> {
            if (realNameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "真实姓名不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (systemUserPasswordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(mainPanel, "系统用户密码不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (typeComboBox.getSelectedIndex() < 0) {
                JOptionPane.showMessageDialog(mainPanel, "请选择账号类型！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (nickNameField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "昵称不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            data.realName = realNameField.getText();
            data.typeId = ((TypeOption) Objects.requireNonNull(typeComboBox.getSelectedItem())).getId(); // 获取 iTypeId
            data.password = systemUserPasswordField.getPassword();
            data.username = nickNameField.getText();

            //todo
            try {
                createMinDBUser(data);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainPanel, "创建数据库用户失败！", "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }finally {
                dispose();
            }
        });

        cancelButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(finishButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel step1() {
        // 步骤 1: 欢迎和选项
        JPanel step1Panel = new JPanel(new GridLayout(4, 1, 10, 10));
        step1Panel.setBackground(Color.WHITE);
        JLabel welcomeLabel = new JLabel("欢迎使用 RE Project Cookie！请配置初始设置：");
        welcomeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        step1Panel.add(welcomeLabel);
        autoUpdateCheckBox = new JCheckBox("启用自动更新检查");
        defaultThemeCheckBox = new JCheckBox("使用默认主题");
        autoUpdateCheckBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        defaultThemeCheckBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        step1Panel.add(autoUpdateCheckBox);
        step1Panel.add(defaultThemeCheckBox);
        return step1Panel;
    }

    private JPanel step2() {
        // 主面板使用边界布局（可扩展的灵活布局）
        JPanel step2Panel = new JPanel(new BorderLayout(10, 20));
        step2Panel.setBackground(Color.WHITE);

        // 欢迎标签单独放在顶部（北区）
        JLabel welcomeLabel = new JLabel("请配置用于 RE Project Cookie的数据库用户名和密码：");
        welcomeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        step2Panel.add(welcomeLabel, BorderLayout.NORTH);

        // 创建表单面板（2行2列）
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);

        JLabel userNameLabel = new JLabel("用户名:");
        userNameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        userNameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        // 添加组件到表单面板
        formPanel.add(userNameLabel);
        formPanel.add(userNameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        // 将表单面板放在主面板中央
        step2Panel.add(formPanel, BorderLayout.CENTER);

        // 添加底部边距（可选）
        step2Panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        return step2Panel;
    }

    private JPanel step3() {
        // 主面板使用边界布局
        JPanel step3Panel = new JPanel(new BorderLayout(10, 15));
        step3Panel.setBackground(Color.WHITE);
        step3Panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10)); // 添加内边距

        // 1. 顶部标题单独一行
        JLabel titleLabel = new JLabel("请创建用于登陆系统的管理员账户");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        step3Panel.add(titleLabel, BorderLayout.NORTH);

        // 2. 创建表单面板 (4行2列)
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);

        // 创建标签
        JLabel nickNameLabel = new JLabel("昵称:");
        nickNameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JLabel systemUserPasswordLabel = new JLabel("系统用户密码:");
        systemUserPasswordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JLabel realNameLabel = new JLabel("真实姓名:");
        realNameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JLabel typeLabel = new JLabel("账号类型:");
        typeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        // 创建输入组件
        nickNameField = new JTextField(20);
        systemUserPasswordField = new JPasswordField(20);
        realNameField = new JTextField(20);
        typeComboBox = new JComboBox<>(typeOptions.toArray(new TypeOption[0]));

        // 设置默认选择第一个选项 (admin)
        typeComboBox.setSelectedIndex(0);
        typeComboBox.setEnabled(false);

        // 添加组件到表单
        formPanel.add(nickNameLabel);
        formPanel.add(nickNameField);
        formPanel.add(systemUserPasswordLabel);
        formPanel.add(systemUserPasswordField);
        formPanel.add(realNameLabel);
        formPanel.add(realNameField);
        formPanel.add(typeLabel);
        formPanel.add(typeComboBox);

        // 将表单添加到主面板中央
        step3Panel.add(formPanel, BorderLayout.CENTER);

        return step3Panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        // 使用通用字体
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setBackground(new Color(0, 123, 255));
        //button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 123, 255), 2),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 105, 217));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 123, 255));
            }
        });
        return button;
    }
}