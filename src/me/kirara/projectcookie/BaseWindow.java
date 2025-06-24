package me.kirara.projectcookie;

import javax.swing.*;
import java.awt.*;

/**
 * 基本的窗口类，继承自JDialog
 */
public class BaseWindow extends JDialog {
    /**
     * 构造函数
     * @param title 窗口标题
     * @param width 窗口宽度
     * @param height 窗口高度
     * @param modal 是否为模式窗口
     * @param resizable 是否可缩放
     * @since 2025-05-25, ReAlpha 0.6.0
     */
    public BaseWindow(String title, int width, int height, boolean modal, boolean resizable) {
        setTitle(title);
        setSize(width, height);
        setModal(modal);
        setLocationRelativeTo(null);
        setResizable(resizable);
        setLayout(new BorderLayout());
        ;
    }
}