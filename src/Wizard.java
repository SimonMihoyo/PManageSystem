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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Wizard {

    // 数据模型
    static class WizardData {
        public boolean autoUpdate;
        public boolean defaultTheme;
        public String DBusername;
        public char[] DBpassword;
        public String username;
        public char[] password;
        public String realName;
        public int typeId;
    }

    private static String StringedDBPassword;

    static class TypeOption {
        private final int id;
        private final String name;

        public TypeOption(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return name;
        }
    }



    /**
     * 显示向导对话框
     */
    public static void showWizardDialog(Connection con, IniFileManager ini) {
        JDialog dialog = new JDialog(null, "向导", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        WizardData data = new WizardData();

        // 查询 Type 表
        List<TypeOption> typeOptions = new ArrayList<>();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT iTypeId, cTypeName FROM Type ORDER BY iTypeId")) {
            while (rs.next()) {
                int id = rs.getInt("iTypeId");
                String name = rs.getString("cTypeName");
                typeOptions.add(new TypeOption(id, name));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(dialog, "无法加载账号类型: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            dialog.dispose();
            return;
        }

        // 主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel cardPanel = new JPanel(new CardLayout());
        List<JPanel> steps = new ArrayList<>();

        // 步骤 1: 欢迎和选项
        JPanel step1Panel = new JPanel(new GridLayout(4, 1, 10, 10));
        step1Panel.add(new JLabel("欢迎使用 PManageSystem！请配置初始设置："));
        JCheckBox autoUpdateCheckBox = new JCheckBox("启用自动更新检查");
        JCheckBox defaultThemeCheckBox = new JCheckBox("使用默认主题");

        step1Panel.add(autoUpdateCheckBox);
        step1Panel.add(defaultThemeCheckBox);
        steps.add(step1Panel);

        // 步骤 2: 用户名和密码
        JPanel step2Panel = new JPanel(new GridLayout(2, 1, 10, 10));
        JTextField userNameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        step2Panel.add(new JLabel("用户名:"));
        step2Panel.add(userNameField);
        step2Panel.add(new JLabel("密码:"));
        step2Panel.add(passwordField);
        steps.add(step2Panel);

        // 步骤 3: 真实姓名和账号类型
        JPanel step3Panel = new JPanel(new GridLayout(4, 1, 10, 10));
        JTextField nickNameField = new JTextField(20);
        JPasswordField systemUserPasswordField = new JPasswordField(20);
        JTextField realNameField = new JTextField(20);
        JComboBox<TypeOption> typeComboBox = new JComboBox<>(typeOptions.toArray(new TypeOption[0]));
        step3Panel.add(new JLabel("昵称:"));
        step3Panel.add(nickNameField);
        step3Panel.add(new JLabel("系统用户密码:"));
        step3Panel.add(systemUserPasswordField);
        step3Panel.add(new JLabel("真实姓名:"));
        step3Panel.add(realNameField);
        step3Panel.add(new JLabel("账号类型:"));
        step3Panel.add(typeComboBox);
        steps.add(step3Panel);

        for (int i = 0; i < steps.size(); i++) {
            cardPanel.add(steps.get(i), String.valueOf(i));
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton prevButton = new JButton("上一步");
        JButton nextButton = new JButton("下一步");
        JButton cancelButton = new JButton("取消");
        JButton finishButton = new JButton("完成");

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
                    JOptionPane.showMessageDialog(dialog, "用户名和密码不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(dialog, "真实姓名不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (systemUserPasswordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(dialog, "系统用户密码不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (typeComboBox.getSelectedIndex() < 0) {
                JOptionPane.showMessageDialog(dialog, "请选择账号类型！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (nickNameField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "昵称不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            data.realName = realNameField.getText();
            data.typeId = ((TypeOption) Objects.requireNonNull(typeComboBox.getSelectedItem())).getId(); // 获取 iTypeId
            data.password = systemUserPasswordField.getPassword();
            data.username = nickNameField.getText();
            StringedDBPassword = new String(data.DBpassword);
            System.out.println("StringedDBPassword: " + StringedDBPassword);
            try {
                String createUserSQL = "CREATE USER '" + data.DBusername + "'@'localhost' IDENTIFIED BY '"  + StringedDBPassword + "'";
                Statement statement = con.createStatement();
                statement.executeUpdate(createUserSQL);

                // 保存用户名，Key，IV和加密后的密码
                // todo: 考虑使用更安全的方式保存Key和IV
                ini.set("MinUser","Username", data.DBusername);
                String Key = Encryption.generateKey();
                String IV = Encryption.generateIV();
                String EncryptedPassword = Encryption.encrypt(new String(data.DBpassword), Key, IV);
                ini.set("MinUser","Key", Key);
                ini.set("MinUser","IV", IV);
                ini.set("MinUser","EncryptedPassword", EncryptedPassword);
                ini.save(Path.of(MainProject.iniPath));

                String grantAllSQL = "GRANT ALL PRIVILEGES ON PManageStore.* TO '" + data.DBusername + "'@'localhost'";
                statement.executeUpdate(grantAllSQL);

                String addUserSQL = "INSERT INTO User (vUserName, vUserPass, vUserRealName, iTypeId) VALUES (?, ?, ?, ?)";
                PreparedStatement addUserStatement = con.prepareStatement(addUserSQL);
                addUserStatement.setString(1, data.username);
                addUserStatement.setString(2, new String(data.password));
                addUserStatement.setString(3, data.realName);
                addUserStatement.setInt(4, data.typeId);
                addUserStatement.executeUpdate();

                JOptionPane.showMessageDialog(dialog, "用户添加成功！");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "添加用户失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                // todo: 考虑使用更可靠的日志
                ex.printStackTrace();
            } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchPaddingException |
                     IllegalBlockSizeException | BadPaddingException | InvalidKeyException | IOException ex) {
                throw new RuntimeException(ex);
            } finally {
                dialog.dispose();
            }
        });

        cancelButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(finishButton);

        mainPanel.add(cardPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);
        dialog.setAlwaysOnTop(true);
    }

}
