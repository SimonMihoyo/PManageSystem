// PManageSystem preferenceWindow.java
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

public class preferenceWindow
{
    private static JDialog preferenceDialog = null;

    public static void createPreferenceWindow(JFrame parentFrame)
    {
        // 检查偏好设置窗口是否已经打开
        if (preferenceDialog == null || !preferenceDialog.isVisible()) {
            // 如果没有打开或已关闭，则创建一个新的偏好设置窗口
            preferenceDialog = new JDialog(parentFrame, "偏好设置", true);
            preferenceDialog.setSize(500, 400);
            preferenceDialog.setLocationRelativeTo(parentFrame);  // 居中显示
            preferenceDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        } else {
            // 如果窗口已经打开，什么都不做
            return;
        }
        // TODO: 实现偏好设置窗口的功能
        JPanel panel = new JPanel();
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new java.awt.Dimension(500, 400));
        // 添加选项卡
        // 常规标签页
        JPanel generalPanel = new JPanel();
        generalPanel.add(new JLabel("常规设置"));
        generalPanel.add(new JCheckBox("启用功能 A"));
        generalPanel.add(new JCheckBox("启用功能 B"));
        tabbedPane.addTab("常规", generalPanel);
        //
        panel.add(tabbedPane);
        preferenceDialog.add(panel);
        preferenceDialog.setVisible(true);
    }
}
