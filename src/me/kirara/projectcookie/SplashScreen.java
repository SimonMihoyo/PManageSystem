// Project Cookie SplashScreen.java
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
// @since Alpha 0.1.5
// @author SimonMihoyo

package me.kirara.projectcookie;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JFrame implements CreateUI { // 改为 JFrame
    private JLabel statusLabel;

    public SplashScreen() {
        setContentPane(createUI());
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    @Override
    public JPanel createUI() {
        // 设置启动屏幕的大小和位置
        setSize(400, 200);
        setLocationRelativeTo(null); // 居中显示
        setUndecorated(true); // 移除窗口装饰，模拟启动屏幕效果
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // 防止用户关闭

        // 创建面板
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // 添加应用程序图标（可选）
        JLabel iconLabel = new JLabel("PManageSystem", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(iconLabel, BorderLayout.CENTER);

        // 添加状态标签
        statusLabel = new JLabel("正在初始化...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(statusLabel, BorderLayout.SOUTH);

        // 设置背景颜色
        panel.setBackground(Color.WHITE);
        return panel;
    }


}
