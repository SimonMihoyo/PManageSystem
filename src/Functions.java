// PManageSystem Functions.java
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
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Functions
{
    private static char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     *自定义简单生成盐，是一个随机生成的长度为16的字符串，每一个字符是随机的十六进制字符
     */
    public static String genrateSalt() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < sb.capacity(); i++) {
            sb.append(hex[random.nextInt(16)]);
        }
        return sb.toString();
    }

    /**
     * 将byte数组转换为16进制字符串
     * @param hash
     * @return
     */
    public static String toHexString(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * SHA-256加密
     */
    public static String calculateSHA256(String originalString) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(
                originalString.getBytes(StandardCharsets.UTF_8));
        return toHexString(encodedhash);
    }

    /**
     * 设置窗口
     */
    public static void setupWindow(IniFileManager iniFileManager)
    {
        // 创建窗口
        JFrame frame = new JFrame("MainWindow");
        // 设置窗口大小
        frame.setSize(800, 700);
        // 设置窗口位置
        frame.setLocationRelativeTo(null);
        // 设置窗口关闭时的操作
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 添加菜单栏
        JMenuBar menuBar = WindowInitializer.createMenuBar(frame);
        frame.setJMenuBar(menuBar);
        // 创建图片面板
        JPanel imagePanel = WindowInitializer.createImagePanel();
        frame.add(imagePanel, BorderLayout.NORTH);
        // 添加主面板
        JSplitPane mainPanel = WindowInitializer.createMainContent();
        frame.add(mainPanel, BorderLayout.CENTER);
        // 创建状态栏
        JPanel statusPanel = WindowInitializer.createStatusBar();
        // 添加状态栏
        frame.add(statusPanel, BorderLayout.SOUTH);
        // 显示窗口
        frame.setVisible(true);
    }

    /**
     * 检查是否有更新
     * @return 是否有更新
     */
    public static boolean checkIfHaveUpdate()
    {
        // todo: 连接到更新服务器
        // 真的需要做更新的功能吗？
        // 临时使用false
        return false;
    }

    /**
     * 检查用户的权限
     */
    public static void checkUserPrivileges(Window owner, IniFileManager iniFileManager)
    {
        iniDatabase.showRootCredentialDialog( owner, iniFileManager );
        // 如果未验证直接退出
        if (!iniDatabase.isVerified) System.exit(0);
    }

    /**
     * 退出程序
     */
    public static void exit()
    {
        System.exit(0);
    }

}
