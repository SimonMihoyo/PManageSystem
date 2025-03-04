import javax.swing.*;
import java.awt.event.*;

public class iniWindow
{
    // todo: 完善菜单栏
    // 添加文件菜单
    ////@org.jetbrains.annotations.NotNull
    private static JMenu createFileMenu()
    {
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);

        JMenuItem item1 = new JMenuItem("New", KeyEvent.VK_N);
        item1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        //item1.addActionListener(e ->Functions.newFile() );

        JMenuItem item2 = new JMenuItem("Open", KeyEvent.VK_O);
        item2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        item2.addActionListener(e -> Functions.openFile() );

        JMenuItem item3 = new JMenuItem("Save", KeyEvent.VK_S);
        item3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        // item3.addActionListener(e -> Functions.saveFile() );

        JMenuItem item4 = new JMenuItem("Save As", KeyEvent.VK_S);
        item4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));
        // item4.addActionListener(e -> Functions.saveAsFile() );

        JMenuItem item5 = new JMenuItem("Exit", KeyEvent.VK_X);
        item5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        item5.addActionListener(e -> Functions.exit() );

        menu.add(item1);
        menu.add(item2);
        menu.add(item3);
        menu.add(item4);
        menu.addSeparator();
        menu.add(item5);
        return menu;
    }

    // 添加编辑菜单
    //@org.jetbrains.annotations.NotNull
    private static JMenu createEditMenu()
    {
        JMenu menu = new JMenu("Edit");
        menu.setMnemonic(KeyEvent.VK_E);
        JMenuItem item1 = new JMenuItem("Undo", KeyEvent.VK_Z);
        item1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        JMenuItem item2 = new JMenuItem("Redo", KeyEvent.VK_Y);
        item2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        JMenuItem item3 = new JMenuItem("Cut", KeyEvent.VK_X);
        item3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        JMenuItem item4 = new JMenuItem("Copy", KeyEvent.VK_C);
        item4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        JMenuItem item5 = new JMenuItem("Paste", KeyEvent.VK_V);
        item5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        menu.add(item1);
        menu.add(item2);
        menu.addSeparator();
        menu.add(item3);
        menu.add(item4);
        menu.add(item5);
        return menu;
    }

    // 添加视图菜单
    //@org.jetbrains.annotations.NotNull
    private static JMenu createViewMenu()
    {
        JMenu menu = new JMenu("View");
        menu.setMnemonic(KeyEvent.VK_V);
        JMenuItem item1 = new JMenuItem("Zoom In", KeyEvent.VK_I);
        item1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK));
        JMenuItem item2 = new JMenuItem("Zoom Out", KeyEvent.VK_O);
        item2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        JMenuItem item3 = new JMenuItem("Reset Zoom", KeyEvent.VK_R);
        item3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        menu.add(item1);
        menu.add(item2);
        menu.add(item3);
        return menu;
    }

    // 添加帮助菜单
    //@org.jetbrains.annotations.NotNull
    private static JMenu createHelpMenu()
    {
        JMenu menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        JMenuItem item1 = new JMenuItem("Help Center", KeyEvent.VK_C);
        item1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,InputEvent.CTRL_DOWN_MASK));
        menu.add(item1);
        return menu;
    }

    // 创建菜单栏
    //@org.jetbrains.annotations.NotNull
    public static JMenuBar createMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        menuBar.add(createViewMenu());
        menuBar.add(createHelpMenu());
        return menuBar;
    }

    // 创建状态栏
//    //@org.jetbrains.annotations.NotNull
//    public static JFrame createStatusBar()
//    {
//
//    }

    // 修改应用程序名菜单栏选项


}
