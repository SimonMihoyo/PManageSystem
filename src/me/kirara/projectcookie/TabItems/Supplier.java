
package me.kirara.projectcookie.TabItems;

import me.kirara.projectcookie.Data.SupplierData;
import me.kirara.projectcookie.Record.SupplierOption;
import me.kirara.projectcookie.databaseManager;
import me.kirara.projectcookie.model.AddButton;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static me.kirara.projectcookie.ClearHelper.clearAll;

/**
 * Supplier视图类
 */
public class Supplier
{
    private static final Supplier instance = new Supplier();
    public static Supplier getInstance()
    {
        return instance;
    }
    private Supplier() {}

    // 添加供货单位
    public void initAddSupplierTab(JPanel tabPanel)
    {
        // 创建主面板并设置边距
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        tabPanel.add(mainPanel);

        // 添加标题
        JLabel titleLabel = new JLabel("添加供货单位");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(titleLabel);

        // 创建输入面板
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("供货单位信息"));
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
        inputPanel.add(new JLabel("联系人*:"), gbc);

        gbc.gridx = 1;
        JTextField contactField = new JTextField(25);
        contactField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        inputPanel.add(contactField, gbc);

        // 联系电话
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("联系电话*:"), gbc);

        gbc.gridx = 1;
        JTextField phoneField = new JTextField(25);
        phoneField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        inputPanel.add(phoneField, gbc);

        // 联系地址
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("联系地址*:"), gbc);

        gbc.gridx = 1;
        JTextField addressField = new JTextField(25);
        addressField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        inputPanel.add(addressField, gbc);

        // 邮政编码
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("邮政编码*:"), gbc);

        gbc.gridx = 1;
        JTextField postalCodeField = new JTextField(25);
        postalCodeField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        inputPanel.add(postalCodeField, gbc);

        // 简码
        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(new JLabel("简码*:"), gbc);

        gbc.gridx = 1;
        JTextField codeField = new JTextField(25);
        codeField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        inputPanel.add(codeField, gbc);

        // 经营范围
        gbc.gridx = 0;
        gbc.gridy = 6;
        inputPanel.add(new JLabel("经营范围*:"), gbc);

        gbc.gridx = 1;
        JTextArea businessScopeArea = new JTextArea(3, 20);
        businessScopeArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        businessScopeArea.setLineWrap(true);
        JScrollPane businessScrollPane = new JScrollPane(businessScopeArea);
        inputPanel.add(businessScrollPane, gbc);

        // 描述
        gbc.gridx = 0;
        gbc.gridy = 7;
        inputPanel.add(new JLabel("描述:"), gbc);

        gbc.gridx = 1;
        JTextArea descArea = new JTextArea(3, 20);
        descArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        descArea.setLineWrap(true);
        JScrollPane descScrollPane = new JScrollPane(descArea);
        inputPanel.add(descScrollPane, gbc);

        // 添加按钮
        JButton addButton = AddButton.AddButton("添加");

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
            if (me.kirara.projectcookie.DAO.Supplier.getInstance().addSupplier(new SupplierData(name, contact, phone, address, postalCode, code, businessScope, description)))
            {
                // 清空表单
                clearAll(nameField, contactField, phoneField, addressField, postalCodeField, codeField, businessScopeArea, descArea);
            }
        });
    }

    // 删除供货单位
    public void initDeleteSupplierTab(JPanel tabPanel, JFrame parentFrame) {
        tabPanel.setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        tabPanel.add(mainPanel, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("搜索供货单位"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 单位名称标签
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
        JComboBox<SupplierOption> supplierComboBox = new JComboBox<>();
        supplierComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(supplierComboBox, gbc);

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
        infoPanel.add(new JLabel("联系电话:"), gbc);

        gbc.gridx = 1;
        JLabel phoneLabel = new JLabel("");
        phoneLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoPanel.add(phoneLabel, gbc);

        // 联系地址
        gbc.gridx = 0;
        gbc.gridy = 2;
        infoPanel.add(new JLabel("联系地址:"), gbc);

        gbc.gridx = 1;
        JLabel addressLabel = new JLabel("");
        addressLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoPanel.add(addressLabel, gbc);

        // 邮政编码
        gbc.gridx = 0;
        gbc.gridy = 3;
        infoPanel.add(new JLabel("邮政编码:"), gbc);

        gbc.gridx = 1;
        JLabel postalCodeLabel = new JLabel("");
        postalCodeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoPanel.add(postalCodeLabel, gbc);

        // 简码
        gbc.gridx = 0;
        gbc.gridy = 4;
        infoPanel.add(new JLabel("简码:"), gbc);

        gbc.gridx = 1;
        JLabel codeLabel = new JLabel("");
        codeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoPanel.add(codeLabel, gbc);

        // 经营范围
        gbc.gridx = 0;
        gbc.gridy = 5;
        infoPanel.add(new JLabel("经营范围:"), gbc);

        gbc.gridx = 1;
        JLabel businessScopeLabel = new JLabel("");
        businessScopeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        JScrollPane businessScrollPane = new JScrollPane(businessScopeLabel);
        infoPanel.add(businessScrollPane, gbc);

        // 描述
        gbc.gridx = 0;
        gbc.gridy = 6;
        infoPanel.add(new JLabel("描述:"), gbc);

        gbc.gridx = 1;
        JLabel descLabel = new JLabel("");
        descLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        JScrollPane descScrollPane = new JScrollPane(descLabel);
        infoPanel.add(descScrollPane, gbc);

        mainPanel.add(infoPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton deleteButton = AddButton.AddButton("删除",new Color(220,38,38),new Color(239,68,68));

        mainPanel.add(deleteButton);

        // 搜索按钮事件
        searchButton.addActionListener(e -> searchButtonActionPerformed(searchField, supplierComboBox));

        // 下拉框选择事件
        supplierComboBox.addActionListener(e -> {
            SupplierOption selected = (SupplierOption) supplierComboBox.getSelectedItem();
            if (selected != null) {
                contactLabel.setText(selected.contact());
                phoneLabel.setText(selected.phone());
                addressLabel.setText(selected.address());
                postalCodeLabel.setText(selected.postalCode());
                codeLabel.setText(selected.simplifiedCode());
                businessScopeLabel.setText(selected.businessScope());
                descLabel.setText(selected.description() != null ? selected.description() : "");
            } else {
                clearAll(contactLabel, phoneLabel, addressLabel, postalCodeLabel, codeLabel, businessScopeLabel, descLabel);
            }
        });

        // 删除按钮事件
        deleteButton.addActionListener(e -> {
            SupplierOption selected = (SupplierOption) supplierComboBox.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(parentFrame, "请先选择一个供货单位！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    parentFrame,
                    "确定要永久删除 '" + selected.name() + "' 吗？此操作不可恢复！",
                    "确认删除",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                String deleteSQL = "DELETE FROM PManageStore.Supplier WHERE iSupplierId = ?";
                try (PreparedStatement stmt = databaseManager.getInstance().getExistsConnection().prepareStatement(deleteSQL)) {
                    stmt.setInt(1, selected.id());
                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(parentFrame, "删除成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        searchButton.doClick(); // 刷新列表
                    } else {
                        JOptionPane.showMessageDialog(parentFrame, "删除失败：记录不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    if (ex.getSQLState().equals("23000")) { // 外键约束错误
                        JOptionPane.showMessageDialog(
                                parentFrame,
                                "无法删除！该供货单位已被药品信息关联，请先解除相关药品的供货关系。",
                                "存在依赖",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                                parentFrame,
                                "数据库错误: " + ex.getMessage(),
                                "错误",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });
    }

    // 修改供货单位信息
    public void initModifySupplierTab(JPanel tabPanel) {
        tabPanel.setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        tabPanel.add(mainPanel, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("搜索供货单位"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 单位名称标签
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
        JComboBox<SupplierOption> supplierComboBox = new JComboBox<>();
        supplierComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(supplierComboBox, gbc);

        mainPanel.add(searchPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("编辑供货单位信息"));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 单位名称
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("单位名称*:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField nameField = new JTextField(25);
        nameField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(nameField, gbc);

        // 联系人
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("联系人*:"), gbc);

        gbc.gridx = 1;
        JTextField contactField = new JTextField(25);
        contactField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(contactField, gbc);

        // 联系电话
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("联系电话*:"), gbc);

        gbc.gridx = 1;
        JTextField phoneField = new JTextField(25);
        phoneField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(phoneField, gbc);

        // 联系地址
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("联系地址*:"), gbc);

        gbc.gridx = 1;
        JTextField addressField = new JTextField(25);
        addressField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(addressField, gbc);

        // 邮政编码
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("邮政编码*:"), gbc);

        gbc.gridx = 1;
        JTextField postalCodeField = new JTextField(25);
        postalCodeField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(postalCodeField, gbc);

        // 简码
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("简码*:"), gbc);

        gbc.gridx = 1;
        JTextField codeField = new JTextField(25);
        codeField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(codeField, gbc);

        // 经营范围
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("经营范围*:"), gbc);

        gbc.gridx = 1;
        JTextArea businessScopeArea = new JTextArea(3, 20);
        businessScopeArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        businessScopeArea.setLineWrap(true);
        JScrollPane businessScrollPane = new JScrollPane(businessScopeArea);
        formPanel.add(businessScrollPane, gbc);

        // 描述
        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(new JLabel("描述:"), gbc);

        gbc.gridx = 1;
        JTextArea descArea = new JTextArea(3, 20);
        descArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        descArea.setLineWrap(true);
        JScrollPane descScrollPane = new JScrollPane(descArea);
        formPanel.add(descScrollPane, gbc);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        // 保存按钮
        JButton saveButton = AddButton.AddButton("保存修改");

        // 重置按钮
        JButton resetButton = AddButton.AddButton("重置",new Color(107,114,128),new Color(156,163,175));

        buttonPanel.add(saveButton);
        buttonPanel.add(resetButton);
        mainPanel.add(buttonPanel);

        // 搜索按钮事件
        searchButton.addActionListener(e -> searchButtonActionPerformed(searchField,supplierComboBox));

        // 下拉框选择事件
        supplierComboBox.addActionListener(e -> {
            SupplierOption selected = (SupplierOption) supplierComboBox.getSelectedItem();
            if (selected != null) {
                nameField.setText(selected.name());
                contactField.setText(selected.contact());
                phoneField.setText(selected.phone());
                addressField.setText(selected.address());
                postalCodeField.setText(selected.postalCode());
                codeField.setText(selected.simplifiedCode());
                businessScopeArea.setText(selected.businessScope());
                descArea.setText(selected.description() != null ? selected.description() : "");
            }
        });

        // 保存按钮事件
        saveButton.addActionListener(e -> {
            SupplierOption selected = (SupplierOption) supplierComboBox.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(tabPanel, "请先选择一个供货单位！", "提示", JOptionPane.WARNING_MESSAGE);
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
                JOptionPane.showMessageDialog(tabPanel, "带*号的字段不能为空！", "输入错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!postalCode.matches("\\d{6}")) {
                JOptionPane.showMessageDialog(tabPanel, "邮政编码必须为6位数字！", "输入错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (me.kirara.projectcookie.DAO.Supplier.getInstance().updateSupplier(new SupplierData(name,contact,phone,address,description,postalCode,code,businessScope),selected.id()))
            {
                searchButton.doClick(); // 刷新列表
            }
        });

        // 重置按钮事件
        resetButton.addActionListener(e -> {
            SupplierOption selected = (SupplierOption) supplierComboBox.getSelectedItem();
            if (selected != null) {
                nameField.setText(selected.name());
                contactField.setText(selected.contact());
                phoneField.setText(selected.phone());
                addressField.setText(selected.address());
                postalCodeField.setText(selected.postalCode());
                codeField.setText(selected.simplifiedCode());
                businessScopeArea.setText(selected.businessScope());
                descArea.setText(selected.description() != null ? selected.description() : "");
            } else {
                clearAll(nameField, contactField, phoneField, addressField, postalCodeField, codeField, businessScopeArea, descArea);
            }
        });
    }

    private void searchButtonActionPerformed(JTextField searchField,JComboBox<SupplierOption> supplierComboBox)
    {
        String keyword = searchField.getText().trim();
        supplierComboBox.removeAllItems();

        List<SupplierOption> suppliers = me.kirara.projectcookie.DAO.Supplier.getInstance().search(keyword);
        for (SupplierOption supplier : suppliers) {
            supplierComboBox.addItem(supplier);
        }

        if (!suppliers.isEmpty()) {
            supplierComboBox.setSelectedIndex(0);
        }
    }
}
