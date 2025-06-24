// PManageSystem LoginWindow.java
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

import me.kirara.projectcookie.Data.*;
import me.kirara.projectcookie.Exceptions.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class LoginWindow extends BaseWindow implements CreateUI {
    // 定义常量
    public static final int LOGIN_SUCCESS = 1;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean isCancelled;
    private String username;
    private String userType;

    private Image backgroundImage;

    /**
     * 构造函数
     */
    public LoginWindow() {
        // 设置登录窗口的基本属性
        super("用户登录", 785, 560, true, false);
        isCancelled = false; // 初始化
        setContentPane(createUI());
    }

    public String getUsername() {
        return username;
    }

    @Override
    public JPanel createUI() {
        // 加载背景图片
        backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img/LoginBackground.jpg"))).getImage();

        // 创建一个自定义的 JPanel 来绘制背景
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // 设置背景面板的布局为 null，这样可以自由放置控件
        backgroundPanel.setLayout(null);

        // 创建用户名、密码输入框和按钮
        JLabel usernameLabel = new JLabel("用户名：");
        JLabel passwordLabel = new JLabel("密码：");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("登录");
        JButton cancelButton = new JButton("取消");

        // 设置按钮的字体
        Font labelFont = new Font("汉仪文黑", Font.BOLD, 14);
        usernameLabel.setFont(labelFont);
        passwordLabel.setFont(labelFont);
        loginButton.setFont(labelFont);
        cancelButton.setFont(labelFont);

        // 设置控件位置
        usernameLabel.setBounds(550, 400, 100, 30);
        passwordLabel.setBounds(550, 450, 100, 30);
        usernameField.setBounds(600, 400, 150, 30);
        passwordField.setBounds(600, 450, 150, 30);
        loginButton.setBounds(600, 500, 70, 30);
        cancelButton.setBounds(680, 500, 70, 30);

        // 添加控件到背景面板
        backgroundPanel.add(usernameLabel);
        backgroundPanel.add(passwordLabel);
        backgroundPanel.add(usernameField);
        backgroundPanel.add(passwordField);
        backgroundPanel.add(loginButton);
        backgroundPanel.add(cancelButton);

        // 登录按钮的事件
        // 修改后的登录按钮事件
        loginButton.addActionListener(e -> {
            username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // 直接调用验证方法
            if (authenticateUser(username, password)) {
                dispose();  // 关闭登录窗口
            } else {
                JOptionPane.showMessageDialog(LoginWindow.this, "用户名或密码错误!");
            }
        });

        // 修改取消按钮事件
        cancelButton.addActionListener(e -> {
            isCancelled = true; // 设置取消标志
            dispose();
        });

        // 添加窗口关闭监听器
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                isCancelled = true; // 用户关闭窗口时设置取消标志
            }
        });

        // 将背景面板添加到对话框中
        return backgroundPanel;
    }

    /**
     * 统一的用户验证方法
     */
    private boolean authenticateUser(String username, String password) {
        try {
            String sql = "SELECT PManageStore.Type.cTypeName " +
                    "FROM PManageStore.User JOIN PManageStore.Type ON PManageStore.User.iTypeId = PManageStore.Type.iTypeId " +
                    "WHERE vUserName = ? AND vUserPass = ?";
            try (PreparedStatement ps = databaseManager.getInstance().getExistsConnection().prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        userType = rs.getString("cTypeName"); // 获取账户类型
                        return true;
                    }
                    return false;
                }
            }
        } catch (SQLException ex) {
            throw new LoginFailedException(ex.getMessage());
        }
    }


    /**
     * 显示登录窗口并返回登录结果
     * @return 登录结果，成功返回 LOGIN_SUCCESS
     */
    public LoginStatus showLoginDialog() {
        setVisible(true);
        if (isCancelled) {
            throw new LoginCanceledException("登录取消!");
        }
        // 返回包含账户类型的结果
        return new LoginStatus(LOGIN_SUCCESS, username, userType);
    }
}