
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URI;
import java.util.*;
import java.util.List;

public class WindowInitializer {
    private static JLabel timeLabel;
    public static final String HELP_DOCUMENT_PATH = SystemUtil.getConfigPath("./HelpDoc/index.html");
    private static JTabbedPane tabbedPane;
    private static Map<String, Component> nodeToTabMap; // 记录节点与标签页的映射
    private static Map<String, Integer> nodeToTabIndexMap; // 记录节点与标签页索引的映射

    // 导航项目的数据结构
    static class NavItem {
        private final String name;
        private final List<NavItem> children;

        public NavItem(String name) {
            this.name = name;
            this.children = new ArrayList<>();
        }

        public NavItem(String name, List<NavItem> children) {
            this.name = name;
            this.children = children;
        }

        public String getName() {
            return name;
        }

        public List<NavItem> getChildren() {
            return children;
        }
    }

    // 创建文件菜单
    @org.jetbrains.annotations.NotNull
    private static JMenu createFileMenu() {
        JMenu menu = new JMenu("文件");
        menu.setMnemonic(KeyEvent.VK_F);

        JMenuItem item1 = new JMenuItem("退出", KeyEvent.VK_X);
        item1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.META_DOWN_MASK));
        item1.addActionListener(e -> Functions.exit());

        menu.add(item1);
        return menu;
    }

    // 创建编辑菜单
    @org.jetbrains.annotations.NotNull
    private static JMenu createEditMenu() {
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

    // 创建工具菜单
    @org.jetbrains.annotations.NotNull
    private static JMenu createToolsMenu(JFrame mainFrame) {
        JMenu menu = new JMenu("工具");
        menu.setMnemonic(KeyEvent.VK_T);
        JMenuItem item1 = new JMenuItem("首选项", KeyEvent.VK_P);
        item1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.META_DOWN_MASK));
        item1.addActionListener(e -> preferenceWindow.createPreferenceWindow(mainFrame));
        menu.add(item1);
        return menu;
    }

    // 创建帮助菜单
    @org.jetbrains.annotations.NotNull
    private static JMenu createHelpMenu() {
        JMenu menu = new JMenu("帮助");
        menu.setMnemonic(KeyEvent.VK_H);
        JMenuItem item1 = new JMenuItem("帮助中心", KeyEvent.VK_C);
        item1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.META_DOWN_MASK));
        menu.add(item1);
        return menu;
    }

    // 创建菜单栏
    @org.jetbrains.annotations.NotNull
    public static JMenuBar createMenuBar(JFrame mainFrame) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        menuBar.add(createToolsMenu(mainFrame));
        menuBar.add(createHelpMenu());
        setupMacOSHandlers(mainFrame);
        return menuBar;
    }

    // 创建主内容区域（左右分割布局）
    public static JSplitPane createMainContent() {
        // 初始化节点到标签页的映射
        nodeToTabMap = new HashMap<>();
        nodeToTabIndexMap = new HashMap<>();

        // 左侧导航面板
        JPanel navPanel = createNavigationPanel();
        JScrollPane navScrollPane = new JScrollPane(navPanel);

        // 右侧标签页面板
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT); // 支持滚动标签页

        // 创建分割面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navScrollPane, tabbedPane);
        splitPane.setOneTouchExpandable(true); // 支持一键展开/折叠
        splitPane.setDividerLocation(0.15); // 初始位置为15%
        splitPane.setResizeWeight(0.15); // 调整大小时保持15%比例

        return splitPane;
    }

    /**
     * 创建图片面板
     * @return 图片面板
     */
    public static JPanel createImagePanel() {
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

    // 创建左侧导航面板
    private static JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel(new BorderLayout());
        JTree navTree = createNavigationTree();
        navPanel.add(new JScrollPane(navTree), BorderLayout.CENTER);
        return navPanel;
    }

    // 创建导航树
    private static JTree createNavigationTree() {
        // 定义导航项目数据
        NavItem rootItem = new NavItem("功能导航");
        NavItem category1 = new NavItem("账号管理", List.of(
                new NavItem("系统账号类型管理"),
                new NavItem("添加账号"),
                new NavItem("删除账号"),
                new NavItem("修改账号信息")
        ));
        NavItem category2 = new NavItem("生产单位资料管理", List.of(
                new NavItem("添加生产单位"),
                new NavItem("删除生产单位"),
                new NavItem("修改生产单位信息")
        ));
        NavItem category3 = new NavItem("供货单位资料管理", List.of(
                new NavItem("添加供货单位"),
                new NavItem("删除供货单位"),
                new NavItem("修改供货单位信息")
        ));
        NavItem category4 = new NavItem("药品信息管理", List.of(
                new NavItem("添加药品"),
                new NavItem("删除/修改药品信息")
        ));
        NavItem category5 = new NavItem("药房信息管理", List.of(
                new NavItem("添加药房"),
                new NavItem("删除/修改药房信息")
        ));

        rootItem.getChildren().addAll(List.of(category1, category2, category3, category4,category5));

        // 构建树节点
        DefaultMutableTreeNode root = createTreeNode(rootItem);
        JTree navTree = new JTree(new DefaultTreeModel(root));
        navTree.setShowsRootHandles(true); // 显示根节点手柄
        navTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); // 单选模式

        // 添加树节点点击监听器
        navTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) navTree.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode.isLeaf()) { // 只处理叶子节点
                String nodeName = selectedNode.toString();
                openTab(nodeName);
            }
        });

        return navTree;
    }

    // 递归创建树节点
    private static DefaultMutableTreeNode createTreeNode(NavItem item) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(item.getName());
        for (NavItem child : item.getChildren()) {
            node.add(createTreeNode(child));
        }
        return node;
    }

    // 创建带关闭按钮的标签页标题组件
    private static JPanel createTabTitleComponent(String title, Runnable onClose) {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titlePanel.add(titleLabel);

        JButton closeButton = new JButton("×");
        closeButton.setPreferredSize(new Dimension(16, 16));
        closeButton.setContentAreaFilled(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.addActionListener(e -> onClose.run());
        titlePanel.add(closeButton);

        return titlePanel;
    }

    // 打开或切换到指定标签页
    private static void openTab(String nodeName) {
        if (nodeToTabMap.containsKey(nodeName)) {
            // 标签页已存在，切换到该标签页
            int tabIndex = nodeToTabIndexMap.get(nodeName);
            tabbedPane.setSelectedIndex(tabIndex);
        } else {
            // todo:创建每一个标签页
            // 创建新标签页
            JPanel tabPanel = new JPanel(new BorderLayout());
            tabPanel.add(new JLabel("内容：" + nodeName, SwingConstants.CENTER), BorderLayout.CENTER);

            // 添加标签页
            tabbedPane.addTab(nodeName, tabPanel);
            int newTabIndex = tabbedPane.getTabCount() - 1;

            // 设置自定义标签标题（带关闭按钮）
            tabbedPane.setTabComponentAt(newTabIndex, createTabTitleComponent(nodeName, () -> {
                int index = tabbedPane.indexOfTabComponent(tabbedPane.getTabComponentAt(newTabIndex));
                if (index != -1) {
                    tabbedPane.remove(index);
                    nodeToTabMap.remove(nodeName);
                    nodeToTabIndexMap.remove(nodeName);
                    updateTabIndices();
                }
            }));

            tabbedPane.setSelectedIndex(newTabIndex);

            // 更新映射
            nodeToTabMap.put(nodeName, tabPanel);
            nodeToTabIndexMap.put(nodeName, newTabIndex);

            // 添加标签页关闭监听器
            tabbedPane.addChangeListener(e -> updateTabIndices());
        }
    }

    // 更新标签页索引映射（当标签页关闭或顺序改变时）
    private static void updateTabIndices() {
        nodeToTabIndexMap.clear();
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            String tabTitle = tabbedPane.getTitleAt(i);
            if (nodeToTabMap.containsKey(tabTitle)) {
                nodeToTabIndexMap.put(tabTitle, i);
            }
        }
    }

    // 创建状态栏
    public static JPanel createStatusBar() {
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
        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> updateTime());
        timer.start();
        statusLabel.setPreferredSize(new Dimension(600, 20));
        timeLabel.setPreferredSize(new Dimension(180, 20));
        statusLabel.setToolTipText("状态消息");
        return statusPanel;
    }

    // 更新时间
    private static void updateTime() {
        SwingUtilities.invokeLater(() -> {
            String timePrefix = "当前时间：";
            String time = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
            timeLabel.setText("<html>" + timePrefix + "<b>" + time + "</b></html>");
        });
    }

    // 打开帮助文档
    private static void openHelpDocument() {
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

    // 设置 macOS 特定处理
    private static void setupMacOSHandlers(JFrame mainFrame) {
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
                    desktop.setPreferencesHandler(e -> preferenceWindow.createPreferenceWindow(mainFrame));
                }
            } catch (UnsupportedOperationException ex) {
                // todo: 使用可靠的日志
            }
        }
    }

    private static void showAboutDialog() {
        JOptionPane.showMessageDialog(null, """
                My Application
                Version 1.0
                Copyright © 2025 SimonMihoyo""",
                "关于", JOptionPane.INFORMATION_MESSAGE);
    }

    private static boolean confirmExit() {
        int result = JOptionPane.showConfirmDialog(null, "确定退出？", "退出",
                JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
}

