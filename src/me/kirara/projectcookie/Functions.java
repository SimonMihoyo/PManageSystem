package me.kirara.projectcookie;

import javax.swing.*;
import java.awt.*;
import java.util.regex.*;

public class Functions
{
    /**
     * 匹配是否非本地连接数据库
     * @param url 数据库地址
     * @return 是否匹配成功
     * @since ReAlpha 0.6.0
     */
    public static boolean isNonLocalJdbcUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }

        // 正则表达式：匹配jdbc:mysql://host:port/格式
        Pattern pattern = Pattern.compile(
                "^jdbc:mysql://([^:/]+|\\[[^\\]]+\\])(:\\d+)?/.*$",
                Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(url);

        if (!matcher.matches()) {
            return false; // 格式不匹配
        }

        // 提取主机名（group(1)）
        String host = matcher.group(1);

        // 移除IPv6地址的方括号
        if (host.startsWith("[") && host.endsWith("]")) {
            host = host.substring(1, host.length() - 1);
        }

        // 判断是否为本地地址
        return !(
                "localhost".equalsIgnoreCase(host) ||
                        "127.0.0.1".equals(host) ||
                        "::1".equals(host)
        );
    }

    /**
     * 处理命令行参数
     * @param args 命令行参数
     * @since ReAlpha 0.6.0
     */
    public static void processArgs(String[] args) {
        if (args.length > 0)
        {
            if (args[0].equals("-h") || args[0].equals("--help"))
            {
                JDialog dialog = new JDialog();
                dialog.setModal(true);
                JPanel Panel = new JPanel(new GridLayout(3, 1, 10, 10));
                dialog.setSize(500, 200);
                dialog.setTitle("RE:PManageSystem");
                Panel.add(new JLabel("        Available commands:"));
                Panel.add(new JLabel("  -h, --help : Show this help message"));
                Panel.add(new JLabel("  \"path/to/config.ini\" : Use the specified configuration file to start"));
                dialog.add(Panel);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                System.exit(0);
            }
            //todo: 处理其他命令行参数
        }
    }

    /**
     * 检查是否有更新
     * @return 是否有
     */
    public static boolean checkIfHaveUpdate() {
        // todo:完善更新检查逻辑
        return false;
    }

    /**
     * 检查用户的权限
     */
    public static void checkUserPrivileges(Window owner)
    {
        DatabaseInitializer.getInstance().showRootCredentialDialog(owner);
        // 如果未验证直接退出
        if (!DatabaseInitializer.isVerified) System.exit(0);
    }
}
