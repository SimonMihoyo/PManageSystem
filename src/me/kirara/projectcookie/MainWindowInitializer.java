// Project Cookie MainWindowInitializer.java
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
// @since Alpha 0.1.1
// @author SimonMihoyo

package me.kirara.projectcookie;

import me.kirara.projectcookie.Data.UserType;
import me.kirara.projectcookie.TabItems.ProductionUnit;
import me.kirara.projectcookie.TabItems.Supplier;
import me.kirara.projectcookie.TabItems.SystemAccount;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 主窗口类
 *
 * @author SimonMihoyo
 * @since ReAlpha 0.6.1
 * @version ReAlpha 0.6.3_BugFix
 */
public class MainWindowInitializer extends BaseFrame implements CreateUI
{
    // 单例模式
    private static volatile MainWindowInitializer instance;

    // 一些静态的东西
    private static JLabel timeLabel;
    private static final String HELP_DOCUMENT_PATH = SystemUtil.getConfigPath("./HelpDoc/index.html");
    private static JTabbedPane tabbedPane;
    private static Map<String, Component> nodeToTabMap; // 记录节点与标签页的映射
    private static Map<String, Integer> nodeToTabIndexMap; // 记录节点与标签页索引的映射
    private static Map<String, Icon> iconMap; // 记录节点与图标的映射

    /**
     * 获取单例模式的实例
     * @return 单例模式的实例
     */
    public static MainWindowInitializer getInstance() {
        if (instance == null) {  // 第一次检查，减少同步开销
            synchronized (MainWindowInitializer.class) {
                if (instance == null) {  // 第二次检查，确保线程安全
                    instance = new MainWindowInitializer("MainWindow", 800, 850, false);
                }
            }
        }
        return instance;
    }

    /**
     * 构造函数
     *
     * @param title 窗口标题
     * @param width 窗口宽度
     * @param height 窗口高度
     * @param resizable 是否可调整大小
     * @author SimonMihoyo
     * @since Alpha 0.1.1
     */
    public MainWindowInitializer(String title, int width, int height, boolean resizable) {
        super(title, width, height, resizable);
        // 设置系统属性
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        // 设置应用名称
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "YourAppName");
        setJMenuBar(createMenuBar());
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createUI());
        add(panel);
    }

    /**
     * 创建主内容面板
     * @return 主内容面板
     * @author SimonMihoyo
     */
    @Override
    public JPanel createUI() {
        JPanel panel = new JPanel(new BorderLayout()); // 使用 BorderLayout 布局管理器
        panel.add(createImagePanel(), BorderLayout.NORTH);
        panel.add(createMainContent(), BorderLayout.CENTER);
        panel.add(createStatusBar(), BorderLayout.SOUTH);
        return panel;
    }

    /**
     * 创建文件菜单
     * @return 文件菜单
     */
    private JMenu createFileMenu() {
        JMenu menu = new JMenu("文件");
        menu.setMnemonic(KeyEvent.VK_F);

        JMenuItem item1 = new JMenuItem("退出", KeyEvent.VK_X);
        item1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.META_DOWN_MASK));
        item1.addActionListener(e -> System.exit(0));

        menu.add(item1);
        return menu;
    }

    /**
     * 创建编辑菜单
     * @return 编辑菜单
     */

    private JMenu createEditMenu() {
        JMenu menu = new JMenu("编辑");
        menu.setMnemonic(KeyEvent.VK_E);
        JMenuItem item1 = new JMenuItem("撤销", KeyEvent.VK_Z);
        item1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.META_DOWN_MASK));
        JMenuItem item2 = new JMenuItem("重做", KeyEvent.VK_Y);
        item2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.META_DOWN_MASK));
        JMenuItem item3 = new JMenuItem("剪切", KeyEvent.VK_X);
        item3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.META_DOWN_MASK));
        JMenuItem item4 = new JMenuItem("复制", KeyEvent.VK_C);
        item4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.META_DOWN_MASK));
        JMenuItem item5 = new JMenuItem("粘贴", KeyEvent.VK_V);
        item5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.META_DOWN_MASK));
        JMenuItem item6 = new JMenuItem("查找", KeyEvent.VK_D);
        item6.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
        JMenuItem item7 = new JMenuItem("替换", KeyEvent.VK_R);
        item7.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK));
        menu.add(item1);
        menu.add(item2);
        menu.addSeparator();
        menu.add(item3);
        menu.add(item4);
        menu.add(item5);
        menu.addSeparator();
        menu.add(item6);
        menu.add(item7);
        return menu;
    }

    /**
     * 创建工具菜单
     * @return 工具菜单
     */
    private JMenu createToolsMenu() {
        JMenu menu = new JMenu("工具");
        menu.setMnemonic(KeyEvent.VK_T);
        JMenuItem item1 = new JMenuItem("首选项", KeyEvent.VK_P);
        item1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.META_DOWN_MASK));
        // todo
        //item1.addActionListener(e -> PreferenceWindowInitializer.createPreferenceWindow(mainFrame));
        menu.add(item1);
        return menu;
    }

    /**
     * 创建帮助菜单
     * @return 帮助菜单
     */
    private JMenu createHelpMenu() {
        JMenu menu = new JMenu("帮助");
        menu.setMnemonic(KeyEvent.VK_H);
        JMenuItem item1 = new JMenuItem("帮助中心", KeyEvent.VK_C);
        item1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.META_DOWN_MASK));
        // todo
        //item1.addActionListener(e -> Desktop.getDesktop().browse(URI.create("file://" + HELP_DOCUMENT_PATH)));
        menu.add(item1);
        return menu;
    }

    /**
     * 创建菜单栏
     * @return 菜单栏
     */
    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        menuBar.add(createToolsMenu());
        menuBar.add(createHelpMenu());
        setupMacOSHandlers();
        return menuBar;
    }

    /**
     * 创建图片面板
     * @return 图片面板
     */
    public JPanel createImagePanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img/title.png"))).getImage();
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                g.drawImage(backgroundImage, 0, 0, panelWidth, panelHeight, this);
            }
        };
        panel.setPreferredSize(new Dimension(600, 150));
        return panel;
    }

    /**
     * 创建主面板
     * @return 主面板（分割布局）
     */
    public JSplitPane createMainContent() {
        // 初始化节点到标签页的映射
        nodeToTabMap = new HashMap<>();
        nodeToTabIndexMap = new HashMap<>();
        // 图标未制作完成，先注释掉
        // todo: 制作图标
        //initializeIcons();

        // 左侧导航面板
        JPanel navPanel = createNavigationPanel();
        navPanel.setBackground(new Color(240, 240, 240)); // 设置导航面板背景色
        JScrollPane navScrollPane = new JScrollPane(navPanel);
        navScrollPane.setBorder(BorderFactory.createEmptyBorder()); // 移除滚动面板边框

        // 右侧标签页面板
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT); // 支持滚动标签页
        tabbedPane.setFont(new Font("微软雅黑", Font.PLAIN, 12));

        // 添加全局鼠标监听器处理中键点击关闭
        tabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 处理中键点击关闭标签页
                if (e.getButton() == MouseEvent.BUTTON2) {
                    int tabIndex = tabbedPane.indexAtLocation(e.getX(), e.getY());
                    if (tabIndex != -1) {
                        String title = tabbedPane.getTitleAt(tabIndex);
                        closeTab(title);
                    }
                }
            }
        });

        // 创建分割面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navScrollPane, tabbedPane);
        splitPane.setOneTouchExpandable(true); // 支持一键展开/折叠
        splitPane.setDividerLocation(200); // 初始位置为200像素
        splitPane.setResizeWeight(0.2); // 调整大小时保持20%比例
        splitPane.setDividerSize(5); // 分割线宽度
        splitPane.setBackground(new Color(230, 230, 230)); // 分割线背景色

        return splitPane;
    }

    /**
     * 初始化导航树图标
     */
    // todo: 制作图标
    private void initializeIcons() {
        iconMap = new HashMap<>();
        // 使用Java内置图标或加载自定义图标
        iconMap.put("功能导航", new ImageIcon(MainWindowInitializer.class.getResource("/icons/folder.png")));
        iconMap.put("账号管理", new ImageIcon(MainWindowInitializer.class.getResource("/icons/user.png")));
        iconMap.put("生产单位资料管理", new ImageIcon(MainWindowInitializer.class.getResource("/icons/factory.png")));
        iconMap.put("供货单位资料管理", new ImageIcon(MainWindowInitializer.class.getResource("/icons/supplier.png")));
        iconMap.put("药品信息管理", new ImageIcon(MainWindowInitializer.class.getResource("/icons/medicine.png")));
        iconMap.put("药房信息管理", new ImageIcon(MainWindowInitializer.class.getResource("/icons/pharmacy.png")));
        iconMap.put("系统账号类型管理", new ImageIcon(MainWindowInitializer.class.getResource("/icons/account_type.png")));
        iconMap.put("添加账号", new ImageIcon(MainWindowInitializer.class.getResource("/icons/add_user.png")));
        iconMap.put("删除账号", new ImageIcon(MainWindowInitializer.class.getResource("/icons/delete_user.png")));
        iconMap.put("修改账号信息", new ImageIcon(MainWindowInitializer.class.getResource("/icons/edit_user.png")));
        iconMap.put("添加生产单位", new ImageIcon(MainWindowInitializer.class.getResource("/icons/add_factory.png")));
        iconMap.put("删除生产单位", new ImageIcon(MainWindowInitializer.class.getResource("/icons/delete_factory.png")));
        iconMap.put("修改生产单位信息", new ImageIcon(MainWindowInitializer.class.getResource("/icons/edit_factory.png")));
        iconMap.put("添加供货单位", new ImageIcon(MainWindowInitializer.class.getResource("/icons/add_supplier.png")));
        iconMap.put("删除供货单位", new ImageIcon(MainWindowInitializer.class.getResource("/icons/delete_supplier.png")));
        iconMap.put("修改供货单位信息", new ImageIcon(MainWindowInitializer.class.getResource("/icons/edit_supplier.png")));
        iconMap.put("添加药品", new ImageIcon(MainWindowInitializer.class.getResource("/icons/add_medicine.png")));
        iconMap.put("删除/修改药品信息", new ImageIcon(MainWindowInitializer.class.getResource("/icons/edit_medicine.png")));
        iconMap.put("添加药房", new ImageIcon(MainWindowInitializer.class.getResource("/icons/add_pharmacy.png")));
        iconMap.put("删除/修改药房信息", new ImageIcon(MainWindowInitializer.class.getResource("/icons/edit_pharmacy.png")));
    }

    /**
     * 创建导航面板
     * @return 导航面板
     */
    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel(new BorderLayout());

        // 添加顶部标题
        JLabel titleLabel = new JLabel("  系统功能导航");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        titleLabel.setPreferredSize(new Dimension(0, 40));
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        titleLabel.setBackground(new Color(220, 220, 220));
        titleLabel.setOpaque(true);
        navPanel.add(titleLabel, BorderLayout.NORTH);

        // 创建导航树
        JTree navTree = createNavigationTree();
        navTree.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        navPanel.add(new JScrollPane(navTree), BorderLayout.CENTER);

        return navPanel;
    }

    /**
     * 创建导航树
     * @return 导航树
     */
    private JTree createNavigationTree() {
        JTree navTree;
        UserType.Type userType = Main.getLoginStatus().getUserType();

        // 根据用户类型创建不同的导航树
        if (userType == UserType.Type.ADMIN) {
            navTree = specificPanel.AdminTree();
        } else if (userType == UserType.Type.AUDITOR) {
            // 审计员视图（示例）
            navTree = specificPanel.AuditorTree();
        } else if (userType == UserType.Type.USER) {
            // 普通用户视图（示例）
            navTree = specificPanel.UserTree();
        } else {
            // 其他用户类型默认视图
            navTree = specificPanel.UserTree();
        }

        navTree.setShowsRootHandles(true);
        navTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        navTree.setRootVisible(false);
        navTree.setFont(new Font("微软雅黑", Font.PLAIN, 12));

        UIManager.put("Tree.selectionBackground", new Color(220, 230, 245));
        UIManager.put("Tree.selectionForeground", Color.BLACK);

        navTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = navTree.getRowForLocation(e.getX(), e.getY());
                if (row != -1) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) navTree.getPathForRow(row).getLastPathComponent();
                    if (selectedNode != null && selectedNode.isLeaf()) {
                        String nodeName = selectedNode.toString();
                        openTab(nodeName);
                    }
                }
            }
        });

        navTree.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = navTree.getRowForLocation(e.getX(), e.getY());
                if (row != -1) {
                    Rectangle rowBounds = navTree.getRowBounds(row);
                    if (rowBounds.contains(e.getPoint())) {
                        navTree.setSelectionRow(row);
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                navTree.clearSelection();
            }
        });

        return navTree;
    }

    /**
     * 创建带关闭按钮的标签页
     * @param title 标签页标题
     * @return 标签页
     *
     * @since Alpha 0.2.6
     */
    private JPanel createTabTitleComponent(String title, Runnable onClose) {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        titlePanel.setOpaque(false);
        titlePanel.putClientProperty("nodeName", title);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        titlePanel.add(titleLabel);

        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("微软雅黑", Font.BOLD, 10));
        closeButton.setPreferredSize(new Dimension(16, 16));
        closeButton.setContentAreaFilled(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setForeground(new Color(100, 100, 100));

        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setForeground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setForeground(new Color(100, 100, 100));
            }
        });

        closeButton.addActionListener(e -> closeTab(title));

        titlePanel.add(closeButton);
        return titlePanel;
    }

    /**
     * 统一的关闭标签页方法
     * @param nodeName 节点名称（标签页标题）
     *
     * @since ReAlpha 0.6.3
     */
    private void closeTab(String nodeName) {
        if (nodeToTabIndexMap.containsKey(nodeName)) {
            int index = nodeToTabIndexMap.get(nodeName);
            if (index >= 0 && index < tabbedPane.getTabCount()) {
                tabbedPane.remove(index);
                nodeToTabMap.remove(nodeName);
                nodeToTabIndexMap.remove(nodeName);
                updateTabIndices();
            }
        }
    }

    /**
     * 打开或切换到指定标签页
     * @param nodeName 节点名称（标签页标题）
     */
    private void openTab(String nodeName) {
        // 如果标签页已存在，先关闭它
        if (nodeToTabMap.containsKey(nodeName)) {
            closeTab(nodeName); // 移除旧标签页
        }

        // 创建新标签页
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 添加标签页
        tabbedPane.addTab(nodeName, tabPanel);
        int newTabIndex = tabbedPane.getTabCount() - 1;

        // 初始化标签页内容（每次都会重新加载）
        initializeTab(tabPanel, this, nodeName);

        // 设置带关闭按钮的标签标题
        tabbedPane.setTabComponentAt(newTabIndex, createTabTitleComponent(nodeName, () -> closeTab(nodeName)));

        tabbedPane.setSelectedIndex(newTabIndex);

        // 更新映射关系
        nodeToTabMap.put(nodeName, tabPanel);
        nodeToTabIndexMap.put(nodeName, newTabIndex);
    }

    // todo:初始化每一个标签页
    private void initializeTab(JPanel tabPanel, JFrame parentFrame, String nodeName)
    {
        // 根据 nodeName 初始化不同的标签页内容
        switch (nodeName) {
            case "系统账号类型管理":
                SystemAccount.getInstance().initSystemAccountTypeTab(tabPanel, parentFrame);
                break;
            case "添加账号":
                SystemAccount.getInstance().initAddAccountTab(tabPanel, parentFrame);
                break;
            case "删除账号":
                SystemAccount.getInstance().initDeleteAccountTab(tabPanel, parentFrame);
                break;
            case "修改账号信息":
                SystemAccount.getInstance().initModifyAccountTab(tabPanel, parentFrame);
                break;
            case "添加生产单位":
                ProductionUnit.getInstance().initAddProductionUnitTab(tabPanel);
                break;
            case "删除生产单位":
                ProductionUnit.getInstance().initDeleteProductionUnitTab(tabPanel, parentFrame);
                break;
            case "修改生产单位信息":
                ProductionUnit.getInstance().initModifyProductionUnitTab(tabPanel, parentFrame);
                break;
            case "添加供货单位":
                Supplier.getInstance().initAddSupplierTab(tabPanel);
                break;
            case "删除供货单位":
                Supplier.getInstance().initDeleteSupplierTab(tabPanel, parentFrame);
                break;
            case "修改供货单位信息":
                Supplier.getInstance().initModifySupplierTab(tabPanel);
                break;
//            case "添加药品":
//                TabPanelInitializer.initAddMedicineTab(tabPanel);
//                break;
//            case "删除药品信息":
//                TabPanelInitializer.initDeleteMedicineTab(tabPanel);
//                break;
//            case "修改药品信息":
//                TabPanelInitializer.initModifyMedicineTab(tabPanel);
//                break;
//            case "添加药房":
//                TabPanelInitializer.initAddPharmacyTab(tabPanel);
//                break;
//            case "删除药房信息":
//                TabPanelInitializer.initDeletePharmacyTab(tabPanel);
//                break;
//            case "修改药房信息":
//                TabPanelInitializer.initModifyPharmacyTab(tabPanel);
//                break;
            default:
                // 默认情况下，显示一个简单的标签页内容
                tabPanel.add(new JLabel("功能开发中：" + nodeName, SwingConstants.CENTER), BorderLayout.CENTER);
                break;
        }
    }

    /**
     * 更新标签页索引映射
     */
    private void updateTabIndices() {
        nodeToTabIndexMap.clear();
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component tabComponent = tabbedPane.getTabComponentAt(i);
            if (tabComponent instanceof JPanel panel) {
                if (panel.getComponentCount() > 0 && panel.getComponent(0) instanceof JLabel label) {
                    String nodeName = label.getText();
                    nodeToTabIndexMap.put(nodeName, i);
                }
            }
        }
    }

    /**
     * 创建状态栏
     * @return 状态栏
     */
    public JPanel createStatusBar() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        JLabel statusLabel = new JLabel("点击此处查看帮助文档。");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusLabel.setForeground(Color.RED);
        statusLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        statusLabel.setToolTipText("点击可查看帮助文档");
        statusLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openHelpDocument();
            }
        });
        statusPanel.add(statusLabel, BorderLayout.WEST);
        timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        timeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        statusPanel.add(timeLabel, BorderLayout.EAST);
        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();
        statusLabel.setPreferredSize(new Dimension(600, 20));
        timeLabel.setPreferredSize(new Dimension(180, 20));
        statusLabel.setToolTipText("状态消息");
        return statusPanel;
    }

    /**
     * 更新状态栏中时间
     */
    private void updateTime() {
        SwingUtilities.invokeLater(() -> {
            String timePrefix = "当前时间：";
            String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
            timeLabel.setText("<html>" + timePrefix + "<b>" + time + "</b></html>");
        });
    }

    /**
     * 打开帮助文档
     */
    private void openHelpDocument() {
        try {
            if (HELP_DOCUMENT_PATH.startsWith("http")) {
                Desktop.getDesktop().browse(new URI(HELP_DOCUMENT_PATH));
            } else {
                File helpFile = new File(HELP_DOCUMENT_PATH);
                if (helpFile.exists()) {
                    Desktop.getDesktop().open(helpFile);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "帮助文件未找到：" + HELP_DOCUMENT_PATH,
                            "错误",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "无法打开帮助文档：" + ex.getMessage(),
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 设置Mac OS X相关事件处理器
     */
    private void setupMacOSHandlers() {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                if (desktop.isSupported(Desktop.Action.APP_ABOUT)) {
                    desktop.setAboutHandler(e -> showAboutDialog());
                }
                if (desktop.isSupported(Desktop.Action.APP_QUIT_HANDLER)) {
                    desktop.setQuitHandler((e, response) -> {
                        if (confirmExit()) {
                            response.performQuit();
                        } else {
                            response.cancelQuit();
                        }
                    });
                }
                if (desktop.isSupported(Desktop.Action.APP_PREFERENCES)) {
                    //desktop.setPreferencesHandler(e ->PreferenceWindowInitializer.createPreferenceWindow(mainFrame));
                }
            } catch (UnsupportedOperationException ex) {
                // todo: 使用可靠的日志
            }
        }
    }

    /**
     * 显示关于对话框
     */
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(null, """
                Re Project Cookie
                Version 1.0
                Copyright © 2025 SimonMihoyo""",
                "关于", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 确认退出
     * @return 是否确认退出
     */
    private boolean confirmExit() {
        int result = JOptionPane.showConfirmDialog(null, "确定退出？", "退出",
                JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
}
