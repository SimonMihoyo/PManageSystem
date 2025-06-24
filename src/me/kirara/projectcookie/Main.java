// Project Cookie SystemUtil.java
//
// Copyright (C) 2025 SimonMihoyo
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see https://www.gnu.org/licenses/.
//
// @since Alpha 0.2.7
// @author SimonMihoyo

package me.kirara.projectcookie;

import me.kirara.projectcookie.Data.LoginStatus;
import me.kirara.projectcookie.Exceptions.*;
import javax.swing.*;

/**
 * 主类
 */
public class Main
{

    /**
     * 判断是否是第一次使用
     */
    static class isFirstUse
    {
        public String isFirstUse;

        public isFirstUse(String isFirstUse)
        {
            this.isFirstUse = isFirstUse;
        }

        public Boolean get()
        {
            return this.isFirstUse.equals("true") || this.isFirstUse.equals("1");
        }
    }

    static iniFileManager iniFileManager;
    static me.kirara.projectcookie.Data.LoginStatus loginResult;
    public static LoginStatus getLoginStatus()
    {
        return loginResult;
    }

    /**
     * 程序入口
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 处理命令行参数
        Functions.processArgs(args);
        // 初始化 IniFileManager
        iniFileManager = iniFileManager.getInstanceFromArgs(args);

        // 创建并显示启动屏幕
        SplashScreen splash = new SplashScreen();
        splash.setVisible(true);

        // 直接在主线程中顺序执行初始化任务
        try {
            splash.setStatus(LocalizationManager.getInstance().getLocalizedString("Now_staring"));
            Thread.sleep(1000);

            splash.setStatus(LocalizationManager.getInstance().getLocalizedString("CheckUpdates"));
            Thread.sleep(1000);

            if (Functions.checkIfHaveUpdate()) {
                splash.setStatus("有可用更新，请下载安装...");
                Thread.sleep(1000);
            } else {
                splash.setStatus(LocalizationManager.getInstance().getLocalizedString("NoUpdates"));
                Thread.sleep(1000);
            }

            splash.setStatus("正在检测首次使用...");
            Thread.sleep(1000);

            isFirstUse isFirstUse = new isFirstUse(iniFileManager.getInstance().get("System", "FirstUse"));
            if (isFirstUse.get()) {
                splash.setStatus("正在检查用户权限...");
                Thread.sleep(1000);

                // 在EDT中显示权限检查对话框
                SwingUtilities.invokeAndWait(() ->
                        Functions.checkUserPrivileges(splash)
                );

                splash.setStatus("正在初始化数据库...");
                Thread.sleep(1000);

                SwingUtilities.invokeAndWait(() -> {
                    Wizard.getInstance().setVisible(true);
                });

                databaseManager.getInstance().closeConnection();
            }

            // 关闭启动屏幕
            splash.dispose();

            // 显示登录窗口
            if (databaseManager.getInstance().getExistsConnection() == null) {
                throw new NoSuchConnectionException("No connection available");
            }

            LoginWindow loginWindow = new LoginWindow();
            loginResult = loginWindow.showLoginDialog();
            System.out.println(loginResult.toString());

            if (loginResult.getLoginStatus() == LoginWindow.LOGIN_SUCCESS) {
                SwingUtilities.invokeLater(() ->
                        MainWindowInitializer.getInstance().setVisible(true)
                );
            }
        }
//        catch (LoginCanceledException e) {
//            System.exit(0);
//        } catch (CloseConnectionErrorException e) {
//            JOptionPane.showMessageDialog(null, "数据库错误: " + e.getMessage(),
//                    "错误", JOptionPane.ERROR_MESSAGE);
//            System.exit(1);
//        } catch (LoginFailedException e) {
//            JOptionPane.showMessageDialog(null, "登陆失败: " + e.getMessage(),
//                    "错误", JOptionPane.ERROR_MESSAGE);
//        }
        catch (Exception e) {
            e.printStackTrace();
            // 处理初始化失败
            JOptionPane.showMessageDialog(null, "初始化失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        finally {
            // 确保启动屏幕关闭
            splash.dispose();
        }
    }
}