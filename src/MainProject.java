import javax.swing.*;
import javax.swing.event.*;


public class MainProject
{
    public static void main(String[] args)
    {
        // 设置系统属性
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        // 设置应用名称
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "YourAppName");

        Functions.iniWindow();// 初始化窗口

        authentication.logInAuth("1","1<"); // 登录界面
    }
}
