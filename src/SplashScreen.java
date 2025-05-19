import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JFrame { // 改为 JFrame
    private final JLabel statusLabel;

    public SplashScreen() {
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
        setContentPane(panel);
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }
}
