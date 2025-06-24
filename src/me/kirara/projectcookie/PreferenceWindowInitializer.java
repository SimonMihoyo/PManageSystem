// Project Cookie PreferenceWindowInitializer.java
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
// @since Alpha 0.2.3
// @author SimonMihoyo

package me.kirara.projectcookie;

import javax.swing.*;

/**
 * 偏好设置窗口初始化器
 *
 * @author SimonMihoyo
 * @since ReAlpha 0.6.0
 */
public class PreferenceWindowInitializer extends BaseFrame implements CreateUI
{
    /**
     * 构造函数
     *
     * @param title     窗口标题
     * @param width     窗口宽度
     * @param height    窗口高度
     * @param resizable 是否可缩放
     * @see BaseFrame
     * @since ReAlpha 0.6.0
     */
    public PreferenceWindowInitializer(String title, int width, int height, boolean resizable) {
        super(title, width, height, resizable);
    }

    public JPanel createUI()
    {
        return null;
    }
}
