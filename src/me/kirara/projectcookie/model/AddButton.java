package me.kirara.projectcookie.model;

import static me.kirara.projectcookie.model.CustomButton.*;

import javax.swing.*;
import java.awt.*;

public class AddButton
{
    public static JButton AddButton(String title)
    {
        return AddButton(title, null, null);
    }

    public static JButton AddButton(String title,Color frontColor,Color backColor)
    {
        JButton addButton = new JButton(title);
        addButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.setPreferredSize(new Dimension(150, 40));
        addButton.setBackground(new Color(59, 130, 246));
        setButtonStyle(addButton, frontColor, backColor);
        return addButton;
    }
}
