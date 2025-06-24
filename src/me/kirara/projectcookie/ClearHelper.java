package me.kirara.projectcookie;

import javax.swing.*;

public class ClearHelper
{
    // 辅助方法：清空所有输入字段
    public static void clearAll(Object... objects)
    {
        for (Object object : objects)
        {
            if (object instanceof JTextField)
            {
                ((JTextField) object).setText("");
            }
            else if (object instanceof JTextArea)
            {
                ((JTextArea) object).setText("");
            }
            else if (object instanceof JComboBox)
            {
                ((JComboBox<?>) object).removeAllItems();
            }
        }
    }
}
