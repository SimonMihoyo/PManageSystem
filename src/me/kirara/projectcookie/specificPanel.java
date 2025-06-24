// specificPanel.java
package me.kirara.projectcookie;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class specificPanel {
    // 导航项内部类
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

    // 创建管理员导航树
    public static JTree AdminTree() {
        NavItem rootItem = new NavItem("功能导航");
        NavItem category1 = new NavItem("账号管理", Arrays.asList(
                new NavItem("系统账号类型管理"),
                new NavItem("添加账号"),
                new NavItem("删除账号"),
                new NavItem("修改账号信息")
        ));
        NavItem category2 = new NavItem("生产单位资料管理", Arrays.asList(
                new NavItem("添加生产单位"),
                new NavItem("删除生产单位"),
                new NavItem("修改生产单位信息")
        ));
        NavItem category3 = new NavItem("供货单位资料管理", Arrays.asList(
                new NavItem("添加供货单位"),
                new NavItem("删除供货单位"),
                new NavItem("修改供货单位信息")
        ));
        NavItem category4 = new NavItem("药品信息管理", Arrays.asList(
                new NavItem("添加药品"),
                new NavItem("删除药品信息"),
                new NavItem("修改药品信息")
        ));
        NavItem category5 = new NavItem("药房信息管理", Arrays.asList(
                new NavItem("添加药房"),
                new NavItem("删除药房信息"),
                new NavItem("修改药房信息")
        ));

        rootItem.getChildren().addAll(Arrays.asList(category1, category2, category3, category4, category5));

        // 构建树节点
        DefaultMutableTreeNode root = createTreeNode(rootItem);
        return new JTree(new DefaultTreeModel(root));
    }

    // 递归创建树节点
    private static DefaultMutableTreeNode createTreeNode(NavItem item) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(item.getName());
        for (NavItem child : item.getChildren()) {
            node.add(createTreeNode(child));
        }
        return node;
    }

    public static JTree AuditorTree()
    {
        NavItem rootItem = new NavItem("功能导航");
        NavItem category1 = new NavItem("账号管理", List.of(
                new NavItem("修改密码")
        ));
        NavItem category2 = new NavItem("业务单据录入", Arrays.asList(
                new NavItem("药品申购单"),
                new NavItem("药品入库单"),
                new NavItem("药品出库单")
        ));
        NavItem category3 = new NavItem("业务单据审核", List.of(
                new NavItem("单据审核")
        ));
        NavItem category4 = new NavItem("药品管理", Arrays.asList(
                new NavItem("药品零售调价"),
                new NavItem("药品过期管理")
        ));
        NavItem category5 = new NavItem("统计与报表", Arrays.asList(
                new NavItem("库存报表"),
                new NavItem("缺货报表")
        ));
        rootItem.getChildren().addAll(Arrays.asList(category1, category2, category3, category4, category5));

        // 构建树节点
        DefaultMutableTreeNode root = createTreeNode(rootItem);
        return new JTree(new DefaultTreeModel(root));
    }

    public static JTree UserTree()
    {
        NavItem rootItem = new NavItem("功能导航");
        NavItem category1 = new NavItem("药品管理", Arrays.asList(
                new NavItem("药品查询"),
                new NavItem("药品购买")
        ));
        rootItem.getChildren().add(category1);
        DefaultMutableTreeNode root = createTreeNode(rootItem);
        return new JTree(new DefaultTreeModel(root));
    }

    public static JTree OtherTree()
    {
        NavItem rootItem = new NavItem("功能导航");
        return new JTree(new DefaultTreeModel(createTreeNode(rootItem)));
    }
}