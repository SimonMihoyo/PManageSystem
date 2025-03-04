import javax.swing.*;

public class Functions
{
    public static void iniWindow()
    {
        // 创建窗口
        JFrame frame = new JFrame("My Window");
        // 设置窗口大小
        frame.setSize(800, 600);
        // 设置窗口位置
        frame.setLocationRelativeTo(null);
        // 设置窗口关闭时的操作
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 添加菜单栏
        JMenuBar menuBar = iniWindow.createMenuBar();
        frame.setJMenuBar(menuBar);
        // 显示窗口
        frame.setVisible(true);
    }

    public static void openFile()
    {
        //JOptionPane.showMessageDialog(null, "Open File");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION)
        {
            //System.out.println("You selected the file: " + fileChooser.getSelectedFile().getName());
            JOptionPane.showMessageDialog(null,fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    public static void exit()
    {
        System.exit(0);
    }
}
