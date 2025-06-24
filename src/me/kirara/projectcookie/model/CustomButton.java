package me.kirara.projectcookie.model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomButton
{
    public static void setButtonStyle(JButton button,Color frontColor,Color backColor)
    {
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setOpaque(true);

        if (frontColor != null || backColor != null)
        {
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(frontColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(backColor);
                }
            });
        }
        else
        {
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(37, 99, 235));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(new Color(59, 130, 246));
                }
            });
        }

    }

}
