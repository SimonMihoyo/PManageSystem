package me.kirara.projectcookie.TabItems;

import me.kirara.projectcookie.Data.ManufacturerData;
import me.kirara.projectcookie.Record.ManufacturerOption;
import me.kirara.projectcookie.model.AddButton;

import static me.kirara.projectcookie.ClearHelper.clearAll;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;


public class ProductionUnit
{
    private static final ProductionUnit instance = new ProductionUnit();
    public static ProductionUnit getInstance()
    {
        return instance;
    }
    private ProductionUnit() {}

    // 添加生产单位
    public void initAddProductionUnitTab(JPanel tabPanel) {
        // 创建主面板并设置边距
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        tabPanel.add(mainPanel);

        // 添加标题
        JLabel titleLabel = new JLabel("添加生产单位");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(titleLabel);

        // 创建输入面板
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("生产单位信息"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(inputPanel);

        // 设置GridBag布局的列宽
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        inputPanel.add(new JLabel("单位名称*:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField nameField = new JTextField(25);
        nameField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        inputPanel.add(nameField, gbc);

        // 联系人
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        inputPanel.add(new JLabel("联系人*:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField contactField = new JTextField(25);
        contactField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        inputPanel.add(contactField, gbc);

        // 联系电话
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        inputPanel.add(new JLabel("联系电话*:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField phoneField = new JTextField(25);
        phoneField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        inputPanel.add(phoneField, gbc);

        // 联系地址
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        inputPanel.add(new JLabel("联系地址*:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField addressField = new JTextField(25);
        addressField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        inputPanel.add(addressField, gbc);

        // 邮政编码
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        inputPanel.add(new JLabel("邮政编码*:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField postalCodeField = new JTextField(25);
        postalCodeField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        inputPanel.add(postalCodeField, gbc);

        // 简码
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        inputPanel.add(new JLabel("简码*:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField codeField = new JTextField(25);
        codeField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        inputPanel.add(codeField, gbc);

        // 经营范围
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        inputPanel.add(new JLabel("经营范围*:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextArea businessScopeArea = new JTextArea(3, 20);
        businessScopeArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        businessScopeArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(businessScopeArea);
        inputPanel.add(scrollPane, gbc);

        // 描述
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.3;
        inputPanel.add(new JLabel("描述:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextArea descArea = new JTextArea(3, 20);
        descArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        descArea.setLineWrap(true);
        JScrollPane descScrollPane = new JScrollPane(descArea);
        inputPanel.add(descScrollPane, gbc);

        // 添加按钮
        JButton addButton = AddButton.AddButton("添加") ;

        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(addButton);

        // 添加事件监听
        addButton.addActionListener(e -> {
            // 获取输入值
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            String postalCode = postalCodeField.getText().trim();
            String code = codeField.getText().trim();
            String businessScope = businessScopeArea.getText().trim();
            String description = descArea.getText().trim();

            // 输入验证
            if (name.isEmpty() || contact.isEmpty() || phone.isEmpty() || address.isEmpty() ||
                    postalCode.isEmpty() || code.isEmpty() || businessScope.isEmpty()) {
                JOptionPane.showMessageDialog(tabPanel, "带*号的字段不能为空！", "输入错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!postalCode.matches("\\d{6}")) {
                JOptionPane.showMessageDialog(tabPanel, "邮政编码必须为6位数字！", "输入错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (me.kirara.projectcookie.DAO.ProductionUnit.getInstance().addUnit(new ManufacturerData(name, contact, phone, address, description, postalCode, code, businessScope)))
            {
                JOptionPane.showMessageDialog(tabPanel, "添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                // 清空表单
                clearAll(nameField,contactField,phoneField,addressField,postalCodeField,codeField,businessScopeArea,descArea);
            }
        });
    }

    // 删除生产单位
    public void initDeleteProductionUnitTab(JPanel tabPanel, JFrame parentFrame) {
        tabPanel.setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        tabPanel.add(mainPanel, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new GridBagLayout());

        searchPanel.setBorder(BorderFactory.createTitledBorder("搜索生产单位"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 生产单位名称标签
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        searchPanel.add(new JLabel("单位名称:"), gbc);

        // 搜索输入框
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(searchField, gbc);

        // 搜索按钮
        gbc.gridx = 2;
        gbc.weightx = 0.2;
        JButton searchButton = new JButton("搜索");
        searchButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(searchButton, gbc);

        // 下拉框（显示搜索结果）
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        JComboBox<ManufacturerOption> manufacturerComboBox = new JComboBox<>();
        manufacturerComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(manufacturerComboBox, gbc);

        mainPanel.add(searchPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // ========================== 详细信息面板 ==========================
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("单位详细信息"));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 联系人
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        infoPanel.add(new JLabel("联系人:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel contactLabel = new JLabel("");
        contactLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoPanel.add(contactLabel, gbc);

        // 联系电话
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        infoPanel.add(new JLabel("联系电话:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel phoneLabel = new JLabel("");
        phoneLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoPanel.add(phoneLabel, gbc);

        // 联系地址
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        infoPanel.add(new JLabel("联系地址*:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel addressLabel = new JLabel("");
        addressLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoPanel.add(addressLabel, gbc);

        // 邮政编码
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        infoPanel.add(new JLabel("邮政编码*:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel postalCodeLabel = new JLabel("");
        postalCodeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoPanel.add(postalCodeLabel, gbc);

        // 简码
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        infoPanel.add(new JLabel("简码*:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel codeLabel = new JLabel("");
        codeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoPanel.add(codeLabel, gbc);

        // 经营范围
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        infoPanel.add(new JLabel("经营范围*:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel businessScopeLabel = new JLabel("");
        businessScopeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(businessScopeLabel);
        infoPanel.add(scrollPane, gbc);

        // 描述
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.3;
        infoPanel.add(new JLabel("描述:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel descLabel = new JLabel("");
        descLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        JScrollPane descScrollPane = new JScrollPane(descLabel);
        infoPanel.add(descScrollPane, gbc);

        mainPanel.add(infoPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton deleteButton = AddButton.AddButton("删除");
        mainPanel.add(deleteButton);

        searchButton.addActionListener(e -> searchButtonPerformed(searchField, manufacturerComboBox));

        // 下拉框选择事件
        manufacturerComboBox.addActionListener(e ->
        {
            ManufacturerOption selected = (ManufacturerOption) manufacturerComboBox.getSelectedItem();
            if (selected != null)
            {
                contactLabel.setText(selected.contact());
                phoneLabel.setText(selected.phone());
                addressLabel.setText(selected.address());
                descLabel.setText(selected.description());
                postalCodeLabel.setText(selected.postalCode());
                codeLabel.setText(selected.simplifiedCode());
                businessScopeLabel.setText(selected.vBusinecssScope());
            }
            else
            {
                clearAll(contactLabel, phoneLabel, addressLabel, descLabel, postalCodeLabel, codeLabel, businessScopeLabel);
            }
        });

        // 删除按钮事件
        deleteButton.addActionListener(e ->
        {
            ManufacturerOption selected = (ManufacturerOption) manufacturerComboBox.getSelectedItem();
            if (selected == null)
            {
                JOptionPane.showMessageDialog(parentFrame, "请先选择一个生产单位！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    parentFrame,
                    "确定要永久删除 '" + selected.name() + "' 吗？此操作不可恢复！",
                    "确认删除",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION)
            {
                if (me.kirara.projectcookie.DAO.ProductionUnit.getInstance().deleteUnit(selected.id()))
                {
                    searchButton.doClick(); // 刷新列表
                }
            }
        });
    }

    // 修改生产单位信息
    public void initModifyProductionUnitTab(JPanel tabPanel, JFrame parentFrame) {
        tabPanel.setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        tabPanel.add(mainPanel, BorderLayout.CENTER);

        // ========================== 搜索面板 ==========================
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("搜索生产单位"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 生产单位名称标签
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        searchPanel.add(new JLabel("单位名称:"), gbc);

        // 搜索输入框
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(searchField, gbc);

        // 搜索按钮
        gbc.gridx = 2;
        gbc.weightx = 0.2;
        JButton searchButton = new JButton("搜索");
        searchButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(searchButton, gbc);

        // 下拉框（显示搜索结果）
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        JComboBox<ManufacturerOption> manufacturerComboBox = new JComboBox<>();
        manufacturerComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(manufacturerComboBox, gbc);

        mainPanel.add(searchPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // ========================== 编辑面板 ==========================
        JPanel editPanel = new JPanel(new GridBagLayout());
        editPanel.setBorder(BorderFactory.createTitledBorder("编辑单位信息"));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 单位名称
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        editPanel.add(new JLabel("单位名称*:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField nameField = new JTextField(25);
        nameField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        editPanel.add(nameField, gbc);

        // 联系人
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        editPanel.add(new JLabel("联系人*:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField contactField = new JTextField(25);
        contactField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        editPanel.add(contactField, gbc);

        // 联系电话
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        editPanel.add(new JLabel("联系电话*:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField phoneField = new JTextField(25);
        phoneField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        editPanel.add(phoneField, gbc);

        // 联系地址
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        editPanel.add(new JLabel("联系地址*:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField addressField = new JTextField(25);
        addressField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        editPanel.add(addressField, gbc);

        // 邮政编码
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        editPanel.add(new JLabel("邮政编码*:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField postalCodeField = new JTextField(25);
        postalCodeField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        editPanel.add(postalCodeField, gbc);

        // 简码
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        editPanel.add(new JLabel("简码*:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField codeField = new JTextField(25);
        codeField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        editPanel.add(codeField, gbc);

        // 经营范围
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        editPanel.add(new JLabel("经营范围*:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextArea businessScopeArea = new JTextArea(3, 20);
        businessScopeArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        businessScopeArea.setLineWrap(true);
        JScrollPane businessScrollPane = new JScrollPane(businessScopeArea);
        editPanel.add(businessScrollPane, gbc);

        // 描述
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.3;
        editPanel.add(new JLabel("描述:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextArea descArea = new JTextArea(3, 20);
        descArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        descArea.setLineWrap(true);
        JScrollPane descScrollPane = new JScrollPane(descArea);
        editPanel.add(descScrollPane, gbc);

        mainPanel.add(editPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // ========================== 按钮面板 ==========================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        // 保存按钮
        JButton saveButton = AddButton.AddButton("保存");

        // 重置按钮
        JButton resetButton = AddButton.AddButton("重置",new Color(75, 85, 99),new Color(107, 114, 128));

        buttonPanel.add(saveButton);
        buttonPanel.add(resetButton);
        mainPanel.add(buttonPanel);

        // 当前选中的生产单位ID
        final int[] currentManufacturerId = {-1};

        // ========================== 事件监听 ==========================
        // 搜索按钮事件
        searchButton.addActionListener(e -> searchButtonPerformed(searchField, manufacturerComboBox));

        // 下拉框选择事件
        manufacturerComboBox.addActionListener(e -> {
            ManufacturerOption selected = (ManufacturerOption) manufacturerComboBox.getSelectedItem();
            if (selected != null) {
                currentManufacturerId[0] = selected.id();
                nameField.setText(selected.name());
                contactField.setText(selected.contact());
                phoneField.setText(selected.phone());
                addressField.setText(selected.address());
                postalCodeField.setText(selected.postalCode());
                codeField.setText(selected.simplifiedCode());
                businessScopeArea.setText(selected.vBusinecssScope());
                descArea.setText(selected.description() != null ? selected.description() : "");
            } else {
                currentManufacturerId[0] = -1;
                clearAll(nameField, contactField, phoneField, addressField, postalCodeField, codeField, businessScopeArea, descArea);
            }
        });

        // 保存按钮事件
        saveButton.addActionListener(e -> {
            if (currentManufacturerId[0] == -1) {
                JOptionPane.showMessageDialog(parentFrame, "请先选择一个生产单位！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 获取输入值
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            String postalCode = postalCodeField.getText().trim();
            String code = codeField.getText().trim();
            String businessScope = businessScopeArea.getText().trim();
            String description = descArea.getText().trim();

            // 输入验证
            if (name.isEmpty() || contact.isEmpty() || phone.isEmpty() || address.isEmpty() ||
                    postalCode.isEmpty() || code.isEmpty() || businessScope.isEmpty()) {
                JOptionPane.showMessageDialog(parentFrame, "带*号的字段不能为空！", "输入错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!postalCode.matches("\\d{6}")) {
                JOptionPane.showMessageDialog(parentFrame, "邮政编码必须为6位数字！", "输入错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (me.kirara.projectcookie.DAO.ProductionUnit.getInstance().updateUnit(new ManufacturerData(name, contact, phone, address, description, postalCode, code, businessScope), currentManufacturerId[0]))
            {
                searchButton.doClick();
                clearAll(nameField, contactField, phoneField, addressField, postalCodeField, codeField, businessScopeArea, descArea);
            }
        });

        // 重置按钮事件
        resetButton.addActionListener(e -> {
            ManufacturerOption selected = (ManufacturerOption) manufacturerComboBox.getSelectedItem();
            if (selected != null) {
                nameField.setText(selected.name());
                contactField.setText(selected.contact());
                phoneField.setText(selected.phone());
                addressField.setText(selected.address());
                postalCodeField.setText(selected.postalCode());
                codeField.setText(selected.simplifiedCode());
                businessScopeArea.setText(selected.vBusinecssScope());
                descArea.setText(selected.description() != null ? selected.description() : "");
            } else {
                clearAll(nameField, contactField, phoneField, addressField, postalCodeField, codeField, businessScopeArea, descArea);
            }
        });
    }

    private void searchButtonPerformed(JTextField searchField, JComboBox<ManufacturerOption> manufacturerComboBox)
    {
        String searchText = searchField.getText().trim();
        manufacturerComboBox.removeAllItems();

        List<ManufacturerOption> units = me.kirara.projectcookie.DAO.ProductionUnit.getInstance().search(searchText);
        for (ManufacturerOption unit : units) {
            manufacturerComboBox.addItem(unit);
        }

        if (!units.isEmpty()) {
            manufacturerComboBox.setSelectedIndex(0);
        }
    }
}
