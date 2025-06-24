// PManageSystem DatabaseInitializer.java
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

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.Connection;

public class DatabaseInitializer {
    // 添加静态变量存储单例实例
    private static DatabaseInitializer instance;

    // 非静态变量存储验证结果
    public static boolean isVerified = false;
    public String verifiedUser;
    public String verifiedPassword;
    public Connection con;

    public Connection getCon() {
        if (con == null) {
            System.err.println("数据库连接未初始化");
        }
        return con;
    }

    // 私有构造函数，确保单例模式
    private DatabaseInitializer() {}

    // 获取单例实例的方法
    public static DatabaseInitializer getInstance() {
        if (instance == null) {
            instance = new DatabaseInitializer();
        }
        return instance;
    }

    /**
     * 显示验证框
     * @param owner 窗口所有者
     */
    public void showRootCredentialDialog(Window owner) {
        JDialog dialog = CreateUI(owner);

        // 输入面板
        JPanel inputPanel = (JPanel) dialog.getContentPane().getComponent(0);
        JTextField userField = (JTextField) inputPanel.getComponent(1);
        JPasswordField passField = (JPasswordField) inputPanel.getComponent(3);

        // 按钮面板
        JPanel buttonPanel = (JPanel) dialog.getContentPane().getComponent(1);
        JButton verifyBtn = (JButton) buttonPanel.getComponent(0);
        JButton continueBtn = (JButton) buttonPanel.getComponent(1);

        // 验证按钮监听器
        verifyBtn.addActionListener(e -> {
            String user = userField.getText();
            String password = new String(passField.getPassword());

            try
            {
                con = databaseManager.getInstance().getConnection(user, password);
                boolean havePrivilege = databaseManager.getInstance().checkUserPrivilege(con,user, "ALTER");
                if (con != null && havePrivilege)
                {
                    isVerified = true;
                    verifiedUser = user;
                    verifiedPassword = password;
                    verifyBtn.setText("验证成功");
                    verifyBtn.setEnabled(false);
                    continueBtn.setEnabled(true);
                    // 初始化数据库

                    // 获取当前类所在的 JAR 文件或类路径
                    File classPath = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                    // 向上导航到项目根目录
                    File sqlFile = new File(classPath.toString(), "SQL/ini.sql");
                    System.out.println(sqlFile.getAbsolutePath());

                    if (sqlFile.exists()) {
                        // 文件存在，可以使用
                        databaseManager.getInstance().executeSQLFile(con, sqlFile.getAbsolutePath());
                    } else {
                        System.err.println("SQL 文件不存在: " + sqlFile.getAbsolutePath());
                    }
                    //iniFileManager.getInstance().set("System","FirstUse","false");
                }
                else
                {
                    JOptionPane.showMessageDialog(dialog, "验证失败：权限不足或凭据错误");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "连接失败：" + ex.getMessage());
            }

        });

        // 继续按钮监听器
        continueBtn.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    /**
     * 创建验证框的 UI
     * @param owner 窗口所有者
     * @return 创建好的 JDialog
     */
    private JDialog CreateUI(Window owner) {
        JDialog dialog = new JDialog(owner, "首次使用需验证管理员权限", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(null);
        dialog.toFront(); // 确保对话框置顶
        dialog.setAutoRequestFocus(true); // 自动请求焦点

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        JTextField userField = new JTextField("root");
        JPasswordField passField = new JPasswordField();
        inputPanel.add(new JLabel("数据库用户名:"));
        inputPanel.add(userField);
        inputPanel.add(new JLabel("数据库密码:"));
        inputPanel.add(passField);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        JButton verifyBtn = new JButton("验证");
        JButton continueBtn = new JButton("继续");
        continueBtn.setEnabled(false); // 初始置灰

        buttonPanel.add(verifyBtn);
        buttonPanel.add(continueBtn);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        return dialog;
    }


}