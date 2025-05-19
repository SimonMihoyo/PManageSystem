// PManageSystem MainProject.java
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
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;


public class MainProject
{
    public static String iniPath;

    static boolean isFirstUse;
    private static IniFileManager iniFileManager;
    private static Connection dbConnection;

    public static void main(String[] args)
    {
        // 设置系统属性
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        // 设置应用名称
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "YourAppName");

        // 初始化 IniFileManager
        iniFileManager = new IniFileManager();
        iniPath = SystemUtil.getConfigPath("settings.ini");
        try {
            if (iniFileManager.exists(iniPath)) {
                iniFileManager.load(Path.of(iniPath));
            } else {
                iniFileManager.create(iniPath);
            }
            //isFirstUse = "true".equals(iniFileManager.get("System", "FirstUse"));
        } catch ( IOException e) {
            JOptionPane.showMessageDialog(null, "无法初始化配置文件: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // 创建并显示启动屏幕
        SplashScreen splash = new SplashScreen();
        splash.setVisible(true);

        // 使用 SwingWorker 执行初始化任务
        SwingWorker<Void, String> initializer = new SwingWorker<>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                // 模拟初始化任务
                publish("正在启动...");
                Thread.sleep(1000); // 替换为实际的启动逻辑
                publish("正在检查更新...");
                Thread.sleep(1000);
                if (Functions.checkIfHaveUpdate())
                {
                    publish("有可用更新，请下载安装...");
                    Thread.sleep(1000);
                }
                else
                {
                    publish("目前没有可用更新");
                    Thread.sleep(1000);
                }
                publish("正在检测首次使用...");
                Thread.sleep(1000);
                isFirstUse = iniDatabase.checkIfFirstUse(iniFileManager);
                if (isFirstUse)
                {
                    publish("正在检查用户权限...");
                    Thread.sleep(1000);
                    Functions.checkUserPrivileges(splash, iniFileManager); // 这会触发对话框
                    publish("正在初始化数据库...");
                    Thread.sleep(1000); // 替换为统一的数据库初始化逻辑
                    operateDB.closeConnection(iniDatabase.con);
                }
                return null;
            }

            @Override
            protected void process(java.util.List<String> chunks)
            {
                // 更新启动屏幕状态
                for (String status : chunks)
                {
                    splash.setStatus(status);
                }
            }

            @Override
            protected void done()
            {
                try
                {
                    // 检查初始化是否成功
                    get(); // 抛出任何异常
                    // 关闭启动屏幕
                    splash.dispose();

                    // 显示登录窗口
                    dbConnection = operateDB.setupConnection(iniFileManager);
                    if (dbConnection == null)
                    {
                        throw new Exception("No connection available");
                    }
                    LoginWindow loginWindow = new LoginWindow(iniFileManager,dbConnection);
                    int loginResult = loginWindow.showLoginDialog(iniFileManager,dbConnection);
                    if (loginResult == LoginWindow.LOGIN_SUCCESS)
                    {
                        SwingUtilities.invokeLater(() -> Functions.setupWindow(iniFileManager)); // 传递 iniFileManager
                    } else if (loginResult == LoginWindow.LOGIN_CANCELLED)
                    {
                        System.exit(0); // 取消时直接退出
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "登录失败，程序将退出。",
                                "登录失败", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);  // 如果登录失败，退出程序
                    }
                }
                catch (Exception e)
                {
                    // 处理初始化失败
                    JOptionPane.showMessageDialog(null, "初始化失败: " + e.getMessage(),
                            "错误", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        };

        // 启动初始化任务
        initializer.execute();
    }

}
