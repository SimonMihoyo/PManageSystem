// PManageSystem iniDatabase.java
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

import javax.swing.*;
import javax.swing.JDialog;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

public class iniDatabase
{
    // 添加静态变量存储验证结果
    public static boolean isVerified = false;
    public static String verifiedUser;
    public static String verifiedPassword;
    public static Connection con;

    /**
     * 显示验证框
     * @param owner 窗口所有者
     * @param ini Ini文件管理器
     */
    public static void showRootCredentialDialog(Window owner, IniFileManager ini)
    {
        JDialog dialog = new JDialog(owner, "首次使用需验证管理员权限",Dialog.ModalityType.APPLICATION_MODAL);
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
        // 验证按钮监听器
        verifyBtn.addActionListener(e ->
        {
            String user = userField.getText();
            String password = new String(passField.getPassword());
            try {
                con = operateDB.getConnection(user, password);
            } catch (ClassNotFoundException | SQLException ex) {
                throw new RuntimeException(ex);
            }
            try
            {
                boolean havePrivilege = operateDB.checkUserPrivilege(con, user, "ALTER");
                if (con != null && havePrivilege)
                {
                    isVerified = true;
                    verifiedUser = user;
                    verifiedPassword = password;
                    verifyBtn.setText("验证成功");
                    verifyBtn.setEnabled(false);
                    continueBtn.setEnabled(true);
                    // 初始化数据库
                    operateDB.executeSQLFile(con, "./src/SQL/ini.sql");
                    Wizard.showWizardDialog(con,ini);
                }
                else
                {
                    JOptionPane.showMessageDialog(dialog, "验证失败：权限不足或凭据错误");
                }
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(dialog, "连接失败：" + ex.getMessage());
            }
        });

        // 继续按钮监听器
        continueBtn.addActionListener(e -> dialog.dispose());
        buttonPanel.add(verifyBtn);
        buttonPanel.add(continueBtn);
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }




    /**
     * 检查是否第一次使用，如果是，则进行初始化数据库操作
     * @return 是否第一次使用
     */
    public static boolean checkIfFirstUse(IniFileManager ini) throws IOException
    {
        // 检查ini文件
        if (!ini.exists(MainProject.iniPath))
        {
            ini.create(MainProject.iniPath);
        }
        Path path = Path.of(MainProject.iniPath);
        ini.load(path);
        String firstUse = ini.get("System","FirstUse");
        if(firstUse.equals("true"))
        {
            // 第一次使用
            ini.set("System","FirstUse","false");
            ini.save(path);
            return true;
        }
        else
        {
            // 并非第一次
            return false;
        }
    }

}
