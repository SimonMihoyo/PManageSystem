package me.kirara.projectcookie.TabItems;

import me.kirara.projectcookie.Data.*;
import me.kirara.projectcookie.Main;
import me.kirara.projectcookie.MainWindowInitializer;
import me.kirara.projectcookie.Record.UserOption;
import me.kirara.projectcookie.DAO.*;
import static me.kirara.projectcookie.ClearHelper.clearAll;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class SystemAccount
{
    private static final SystemAccount instance = new SystemAccount();
    private SystemAccount() {
    }

    public static SystemAccount getInstance()
    {
        return instance;
    }

    public void initAddAccountTab(JPanel tabPanel, JFrame parentFrame) {
        // 创建主面板并设置边距
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        tabPanel.add(mainPanel);

        // 添加标题
        JLabel titleLabel = new JLabel("添加账号");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(titleLabel);

        // 创建输入面板
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("账号信息"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(inputPanel);

        // 查询 Type 表
        List<TypeOption> typeOptions = TypeQuery.getTypeOptions();

        // 设置GridBag布局的列宽
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        inputPanel.add(new JLabel("昵称:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField nickNameField = new JTextField(20);
        nickNameField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        inputPanel.add(nickNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        inputPanel.add(new JLabel("系统用户密码:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JPasswordField systemUserPasswordField = new JPasswordField(20);
        systemUserPasswordField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        inputPanel.add(systemUserPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        inputPanel.add(new JLabel("真实姓名:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField realNameField = new JTextField(20);
        realNameField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        inputPanel.add(realNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.2;
        inputPanel.add(new JLabel("账号类型:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JComboBox<TypeOption> typeComboBox = new JComboBox<>(typeOptions.toArray(new TypeOption[0]));
        typeComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        inputPanel.add(typeComboBox, gbc);

        // 添加间距
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // "添加"按钮
        JButton addButton = new JButton("添加账号");
        addButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.setPreferredSize(new Dimension(150, 40));
        addButton.setBackground(new Color(59, 130, 246));
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        addButton.setBorderPainted(false);
        addButton.setOpaque(true);
        mainPanel.add(addButton);

        // 添加事件监听器
        addButton.addActionListener(e -> {
            String nickName = nickNameField.getText();
            String systemUserPassword = new String(systemUserPasswordField.getPassword());
            String realName = realNameField.getText();
            int typeId = ((TypeOption) Objects.requireNonNull(typeComboBox.getSelectedItem())).getId();

            // 输入验证
            if (nickName.isEmpty() || systemUserPassword.isEmpty() || realName.isEmpty()) {
                JOptionPane.showMessageDialog(parentFrame,
                        "昵称、系统用户密码、真实姓名不能为空！",
                        "输入错误",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 密码强度检查
            if (systemUserPassword.length() < 6) {
                JOptionPane.showMessageDialog(parentFrame,
                        "密码长度至少需要6个字符！",
                        "密码强度不足",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            // 执行添加操作
            if (me.kirara.projectcookie.DAO.SystemAccount.getInstance().AddUser(new UserData(nickName, systemUserPassword, realName, typeId)))
            {
                nickNameField.setText("");
                systemUserPasswordField.setText("");
                realNameField.setText("");
                typeComboBox.setSelectedIndex(0);
                nickNameField.requestFocus();
            }
        });
    }

    public void initDeleteAccountTab(JPanel tabPanel, JFrame parentFrame) {
        tabPanel.setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        tabPanel.add(mainPanel, BorderLayout.CENTER);

        // 搜索面板
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("搜索用户"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        searchPanel.add(new JLabel("用户名:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(searchField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        JButton searchButton = new JButton("搜索");
        searchButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(searchButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        JComboBox<UserOption> accountComboBox = new JComboBox<>();
        accountComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(accountComboBox, gbc);

        mainPanel.add(searchPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 用户信息面板
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("用户信息"));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        infoPanel.add(new JLabel("用户名:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JLabel usernameLabel = new JLabel("");
        usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoPanel.add(usernameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        infoPanel.add(new JLabel("真实姓名:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JLabel realNameLabel = new JLabel("");
        realNameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoPanel.add(realNameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        infoPanel.add(new JLabel("账号类型:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JLabel typeLabel = new JLabel("");
        typeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoPanel.add(typeLabel, gbc);

        mainPanel.add(infoPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton deleteButton = new JButton("删除");
        deleteButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        deleteButton.setPreferredSize(new Dimension(100, 40));
        deleteButton.setBackground(new Color(239, 68, 68));
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        deleteButton.setBorderPainted(false);
        deleteButton.setOpaque(true);

        JButton cancelButton = new JButton("取消");
        cancelButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.setBackground(new Color(107, 114, 128));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        cancelButton.setBorderPainted(false);
        cancelButton.setOpaque(true);

        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel);

        // 搜索按钮事件
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            accountComboBox.removeAllItems();

            List<UserOption> users = me.kirara.projectcookie.DAO.SystemAccount.getInstance().search(searchText);
            for (UserOption user : users) {
                accountComboBox.addItem(user);
            }

            if (!users.isEmpty()) {
                accountComboBox.setSelectedIndex(0);
            }
        });

        // 下拉框选择事件监听器
        accountComboBox.addActionListener(e -> {
            UserOption selectedUser = (UserOption) accountComboBox.getSelectedItem();
            if (selectedUser != null) {
                usernameLabel.setText(selectedUser.username());
                realNameLabel.setText(selectedUser.realName());
                typeLabel.setText(selectedUser.typeName());
            } else {
                usernameLabel.setText("");
                realNameLabel.setText("");
                typeLabel.setText("");
            }
        });

        // 删除按钮事件监听器
        deleteButton.addActionListener(e -> {
            UserOption selectedUser = (UserOption) accountComboBox.getSelectedItem();
            if (selectedUser == null) {
                JOptionPane.showMessageDialog(parentFrame, "请先选择一个用户！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 检查是否为当前登录用户
            if (selectedUser.username().equals(Main.getLoginStatus().getLoginName())) {
                JOptionPane.showMessageDialog(parentFrame, "不能删除当前登录的账号！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(parentFrame,
                    "确定要删除用户 " + selectedUser.username() + " 吗？",
                    "确认删除",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                if (me.kirara.projectcookie.DAO.SystemAccount.getInstance().DeleteUser(selectedUser.id()))
                {
                    searchButton.doClick(); // 刷新下拉框
                    searchField.setText("");
                }
            }
        });

        // 取消按钮事件监听器
        cancelButton.addActionListener(e -> {
            searchField.setText("");
            accountComboBox.removeAllItems();
            usernameLabel.setText("");
            realNameLabel.setText("");
            typeLabel.setText("");
        });
    }
    
    public JDialog CreateAccountTypeDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Create Account Type", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        // 创建输入面板
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("账号类型:");
        JTextField nameField = new JTextField(20);
        JLabel annotationLabel = new JLabel("描述:");
        JTextField annotationField = new JTextField(20);

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(annotationLabel);
        inputPanel.add(annotationField);

        OkCancel result = getResult(dialog, inputPanel);

        // 确认按钮事件
        result.confirmButton().addActionListener(e -> {
            String typeName = nameField.getText().trim();
            String annotation = annotationField.getText().trim();

            if (typeName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "账号类型不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // 执行添加操作
            if(me.kirara.projectcookie.DAO.SystemAccount.getInstance().AddSystemType(new SystemType(typeName, annotation)))
            {
                ////todo: 刷新主表格
////                tabPanel.removeAll();
////                TabPanelInitializer.initSystemAccountTypeTab(tabPanel, parent, dbConnection);
////                tabPanel.revalidate();
////                tabPanel.repaint();
                dialog.dispose(); // 关闭对话框
            }
        });
        // 取消按钮事件
        result.cancelButton().addActionListener(e -> dialog.dispose());
        return dialog;
    }

    private OkCancel getResult(JDialog dialog, JPanel inputPanel) {
        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton confirmButton = new JButton("确认");
        JButton cancelButton = new JButton("取消");

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        // 添加组件到对话框
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        return new OkCancel(confirmButton, cancelButton);
    }

    private record OkCancel(JButton confirmButton, JButton cancelButton) {}

    public JDialog DeleteAccountTypeDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Delete Account Type", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        // 获取账号类型列表
        List<TypeOption> typeOptions = TypeQuery.getTypeOptions();

        // 创建下拉框
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel typeLabel = new JLabel("选择类型:");
        JComboBox<TypeOption> typeComboBox = new JComboBox<>(typeOptions.toArray(new TypeOption[0]));
        inputPanel.add(typeLabel);
        inputPanel.add(typeComboBox);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton confirmButton = new JButton("确认");
        JButton cancelButton = new JButton("取消");

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        // 添加组件到对话框
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // 确认按钮事件
        // 确认按钮事件
        confirmButton.addActionListener(e -> {
            TypeOption selectedType = (TypeOption) typeComboBox.getSelectedItem();
            if (selectedType == null) {
                JOptionPane.showMessageDialog(dialog, "请选择一个账号类型", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 弹出确认对话框
            int result = JOptionPane.showConfirmDialog(
                    dialog,
                    "确定要删除账号类型 '" + selectedType + "' 吗？",
                    "删除确认",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            // 如果用户选择“是”，则执行删除操作
            if (result == JOptionPane.YES_OPTION) {
                if (me.kirara.projectcookie.DAO.SystemAccount.getInstance().DeleteSystemType(selectedType.getId()))
                {
                    ////todo: 刷新主表格
////                    tabPanel.removeAll();
////                    TabPanelInitializer.initSystemAccountTypeTab(tabPanel, parent, dbConnection);
////                    tabPanel.revalidate();
////                    tabPanel.repaint();
                    dialog.dispose(); // 关闭对话框
                }
            }
        });
        // 取消按钮事件
        cancelButton.addActionListener(e -> dialog.dispose());
        return dialog;
    }

    public void initModifyAccountTab(JPanel tabPanel, JFrame parentFrame) {
        tabPanel.setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        tabPanel.add(mainPanel, BorderLayout.CENTER);

        // 搜索面板
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("搜索用户"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        searchPanel.add(new JLabel("用户名:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(searchField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        JButton searchButton = new JButton("搜索");
        searchButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(searchButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        JComboBox<UserOption> accountComboBox = new JComboBox<>();
        accountComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(accountComboBox, gbc);

        mainPanel.add(searchPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 用户信息面板
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("用户信息"));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        infoPanel.add(new JLabel("用户名:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JLabel usernameLabel = new JLabel("");
        usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoPanel.add(usernameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        infoPanel.add(new JLabel("系统用户密码:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        infoPanel.add(new JLabel("真实姓名:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField realNameField = new JTextField(20);
        realNameField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoPanel.add(realNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.2;
        infoPanel.add(new JLabel("账号类型:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JLabel typeLabel = new JLabel("");
        typeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoPanel.add(typeLabel, gbc);

        mainPanel.add(infoPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton modifyButton = new JButton("修改");
        modifyButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        modifyButton.setPreferredSize(new Dimension(100, 40));
        modifyButton.setBackground(new Color(59, 130, 246));
        modifyButton.setFocusPainted(false);
        modifyButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        modifyButton.setBorderPainted(false);
        modifyButton.setOpaque(true);

        JButton cancelButton = new JButton("取消");
        cancelButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.setBackground(new Color(107, 114, 128));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        cancelButton.setBorderPainted(false);
        cancelButton.setOpaque(true);

        buttonPanel.add(modifyButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel);

        // 跟踪更改状态
        final boolean[] hasChanges = {false};
        final String[] originalPassword = {""};
        final String[] originalRealName = {""};

        // 搜索按钮事件监听器
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            accountComboBox.removeAllItems();

            List<UserOption> users = me.kirara.projectcookie.DAO.SystemAccount.getInstance().search(searchText);
            for (UserOption user : users) {
                accountComboBox.addItem(user);
            }

            if (!users.isEmpty()) {
                accountComboBox.setSelectedIndex(0);
            }
        });

        // 下拉框选择事件监听器
        accountComboBox.addActionListener(e -> {
            if (hasChanges[0]) {
                int confirm = JOptionPane.showConfirmDialog(parentFrame,
                        "有未保存的更改，是否保存？",
                        "未保存的更改",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    modifyButton.doClick();
                    if (hasChanges[0]) { // 如果保存失败，阻止切换
                        return;
                    }
                } else if (confirm == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }

            UserOption selectedUser = (UserOption) accountComboBox.getSelectedItem();
            if (selectedUser != null) {
                usernameLabel.setText(selectedUser.username());
                passwordField.setText(""); // 密码不显示实际值
                realNameField.setText(selectedUser.realName());
                typeLabel.setText(selectedUser.typeName());
                originalPassword[0] = "";
                originalRealName[0] = selectedUser.realName();
            } else {
                usernameLabel.setText("");
                passwordField.setText("");
                realNameField.setText("");
                typeLabel.setText("");
                originalPassword[0] = "";
                originalRealName[0] = "";

            }
            hasChanges[0] = false;
        });

        // 监听输入字段的变化
        passwordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String newPassword = new String(passwordField.getPassword());
                hasChanges[0] = !newPassword.equals(originalPassword[0]) || !realNameField.getText().equals(originalRealName[0]);
            }
        });

        realNameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String newRealName = realNameField.getText();
                hasChanges[0] = !newRealName.equals(originalRealName[0]) || !new String(passwordField.getPassword()).equals(originalPassword[0]);
            }
        });

        // 修改按钮事件监听器
        modifyButton.addActionListener(e -> {
            UserOption selectedUser = (UserOption) accountComboBox.getSelectedItem();
            if (selectedUser == null) {
                JOptionPane.showMessageDialog(parentFrame, "请先选择一个用户！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String newPassword = new String(passwordField.getPassword());
            String newRealName = realNameField.getText().trim();

            // 输入验证（保持不变）
            if (newPassword.isEmpty() && newRealName.isEmpty()) {
                JOptionPane.showMessageDialog(parentFrame, "密码和真实姓名不能都为空！", "输入错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!newPassword.isEmpty() && newPassword.length() < 6) {
                JOptionPane.showMessageDialog(parentFrame, "密码长度至少需要6个字符！", "密码强度不足", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (newRealName.isEmpty()) {
                JOptionPane.showMessageDialog(parentFrame, "真实姓名不能为空！", "输入错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 调用DAO层方法
            boolean success = me.kirara.projectcookie.DAO.SystemAccount.getInstance()
                    .updateUser(selectedUser.id(),
                            newPassword.isEmpty() ? null : newPassword,
                            newRealName);

            if (success) {
                JOptionPane.showMessageDialog(parentFrame, "修改用户信息成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                if (selectedUser.username().equals(Main.getLoginStatus().getLoginName())) {
                    JOptionPane.showMessageDialog(parentFrame,
                            "您已修改了自己的账号信息，请退出系统并重新登录！",
                            "提示",
                            JOptionPane.WARNING_MESSAGE);
                }
                originalPassword[0] = newPassword.isEmpty() ? originalPassword[0] : newPassword;
                originalRealName[0] = newRealName;
                hasChanges[0] = false;
                searchButton.doClick(); // 刷新下拉框
            } else {
                JOptionPane.showMessageDialog(parentFrame, "修改用户信息失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 取消按钮事件监听器
        cancelButton.addActionListener(e -> {
            clearAll(searchField, accountComboBox, usernameLabel, passwordField, realNameField, typeLabel, originalPassword,originalRealName);
            hasChanges[0] = false;
        });
    }

    public void initSystemAccountTypeTab(JPanel tabPanel, JFrame parentFrame) {
        tabPanel.setLayout(new BorderLayout());
        try {
            /// 使用DAO获取数据
            List<SystemType> types = me.kirara.projectcookie.DAO.SystemAccount.getInstance().getAllSystemTypes();

            // 创建二维数组存储查询结果
            Object[][] tableData = new Object[types.size()][3];
            for (int i = 0; i < types.size(); i++) {
                SystemType type = types.get(i);
                tableData[i][0] = type.getId();
                tableData[i][1] = type.getName();
                tableData[i][2] = type.getDescription();
            }

            // 创建表格
            String[] columns = {"ID", "账号类型", "描述"};
            JTable table = new JTable(tableData, columns);
            JScrollPane scrollPane = new JScrollPane(table);
            tabPanel.add(scrollPane, BorderLayout.CENTER);

            // 添加工具栏
            JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton addButton = new JButton("添加类型");
            JButton editButton = new JButton("删除类型");
            JButton refreshButton = new JButton("刷新");
            toolbar.add(addButton);
            toolbar.add(editButton);
            toolbar.add(Box.createHorizontalGlue());
            toolbar.add(refreshButton);
            tabPanel.add(toolbar, BorderLayout.NORTH);

            // 添加事件监听器
            addButton.addActionListener(e -> {
                JDialog accountDialog = CreateAccountTypeDialog(parentFrame);
                accountDialog.setVisible(true);
                accountDialog.setAlwaysOnTop(true);
            });
            editButton.addActionListener(e -> {
                JDialog accountDialog = DeleteAccountTypeDialog(parentFrame);
                accountDialog.setVisible(true);
                accountDialog.setAlwaysOnTop(true);
            });
            refreshButton.addActionListener(e -> {
                tabPanel.removeAll();
                initSystemAccountTypeTab(tabPanel, parentFrame);
                tabPanel.revalidate();
                tabPanel.repaint();
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(tabPanel, "数据库查询出错: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
