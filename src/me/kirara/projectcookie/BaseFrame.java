package me.kirara.projectcookie;

import javax.swing.*;
import java.awt.*;


/**
 * 基本的窗口类，继承自JDialog
 */
public class BaseFrame extends JFrame {
    JFrame frame;

    /**
     * 构造函数
     * @param title 窗口标题
     * @param width 窗口宽度
     * @param height 窗口高度
     * @param resizable 是否可缩放
     * @since 2025-05-25, ReAlpha 0.6.0
     */
    public BaseFrame(String title, int width, int height, boolean resizable) {
        frame = new JFrame();
        setTitle(title);
        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(resizable);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}