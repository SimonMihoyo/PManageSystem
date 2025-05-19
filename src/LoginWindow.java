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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

public class LoginWindow extends JDialog
{
    // 定义常量
    public static final int LOGIN_SUCCESS = 1;
    public static final int LOGIN_FAILED = -1;
    public static final int LOGIN_CANCELLED = 0; // 新增取消状态

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private boolean isCancelled; // 新增标志

    private final Image backgroundImage;

    /**
     * 构造函数
     */
    public LoginWindow(IniFileManager iniFileManager,Connection con) {
        isCancelled = false; // 初始化
        // 设置登录窗口的基本属性
        setTitle("用户登录");
        setModal(true);
        setSize(785, 560);
        setLocationRelativeTo(null);
        setResizable(false);

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
        loginButton.addActionListener(e ->
        {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                if (isValidCredentials(username, password,iniFileManager,con))
                {
                    dispose();  // 关闭登录窗口
                }
                else
                {
                    JOptionPane.showMessageDialog(LoginWindow.this, "用户名或密码错误!");
                }
            } catch (SQLException | ClassNotFoundException | InvalidAlgorithmParameterException |
                     NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                     BadPaddingException | InvalidKeyException ex) {
                throw new RuntimeException(ex);
            }
        });

        // 修改取消按钮事件
        cancelButton.addActionListener(e -> {
            isCancelled = true; // 设置取消标志
            dispose();
        });

        // 添加窗口关闭监听器
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                isCancelled = true; // 用户关闭窗口时设置取消标志
            }
        });

        // 将背景面板添加到对话框中
        setContentPane(backgroundPanel);
    }

    // 假设的验证登录方法
    private boolean isValidCredentials(String username, String password,IniFileManager iniFileManager,Connection con)
            throws SQLException, ClassNotFoundException, InvalidAlgorithmParameterException,
            NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
            BadPaddingException, InvalidKeyException {
        String sql = "select * from PManageStore.User where vUserName = ? and vUserPass = ? ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        System.out.println(rs);
        return rs.next();
    }

    /**
     * 显示登录窗口并返回登录结果
     * @return 登录结果，成功返回 LOGIN_SUCCESS，失败返回 LOGIN_FAILED
     */
    public int showLoginDialog(IniFileManager iniFileManager,Connection con)
            throws SQLException, ClassNotFoundException, InvalidAlgorithmParameterException,
            NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
            BadPaddingException, InvalidKeyException {
        setVisible(true);
        if (isCancelled) {
            return LOGIN_CANCELLED;
        }
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        //Connection con = null;
        try {
            if (isValidCredentials(username, password, iniFileManager, con)) {
                //outConnection[0] = con;
                return LOGIN_SUCCESS;
            } else {
                operateDB.closeConnection(con);
                return LOGIN_FAILED;
            }
        } catch (Exception e) {
            if (con != null) {
                operateDB.closeConnection(con);
            }
            throw e;
        }
    }

}
