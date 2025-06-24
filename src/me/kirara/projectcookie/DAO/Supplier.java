package me.kirara.projectcookie.DAO;

import me.kirara.projectcookie.Data.SupplierData;
import me.kirara.projectcookie.Record.SupplierOption;
import me.kirara.projectcookie.Searchable;
import me.kirara.projectcookie.databaseManager;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Supplier implements Searchable<SupplierOption>
{
    private static final Supplier instance = new Supplier();
    public static Supplier getInstance()
    {
        return instance;
    }
    private Supplier()
    {}

    public boolean addSupplier(SupplierData supplier)
    {
        // 执行数据库插入
        String sql = "INSERT INTO PManageStore.Supplier (vSupplierName, cContanctMan, cContanctPhone, " +
                "vContanctAddress, Description, cPostalCode, SimplifiedCode, BusinessScope) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = databaseManager.getInstance().getExistsConnection().prepareStatement(sql)) {
            pstmt.setString(1, supplier.getSupplierName());
            pstmt.setString(2, supplier.getContactMan());
            pstmt.setString(3, supplier.getContactPhone());
            pstmt.setString(4, supplier.getContactAddress());
            pstmt.setString(5, supplier.getDescription().isEmpty() ? null : supplier.getDescription());
            pstmt.setString(6, supplier.getPostalCode());
            pstmt.setString(7, supplier.getSimplifiedCode());
            pstmt.setString(8, supplier.getBusinessScope());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
            return false;
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) { // 唯一约束冲突
                JOptionPane.showMessageDialog(null, "供货单位名称已存在！", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "数据库错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
    }

    @Override
    public List<SupplierOption> search(String keyword) {
        List<SupplierOption> suppliers = new ArrayList<>();
        String sql = "SELECT iSupplierId, vSupplierName, cContanctMan, cContanctPhone, vContanctAddress, " +
                "Description, cPostalCode, SimplifiedCode, BusinessScope " +
                "FROM PManageStore.Supplier WHERE vSupplierName LIKE ?";

        try (PreparedStatement stmt = databaseManager.getInstance().getExistsConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    suppliers.add(new SupplierOption(
                            rs.getInt("iSupplierId"),
                            rs.getString("vSupplierName"),
                            rs.getString("cContanctMan"),
                            rs.getString("cContanctPhone"),
                            rs.getString("vContanctAddress"),
                            rs.getString("Description"),
                            rs.getString("cPostalCode"),
                            rs.getString("SimplifiedCode"),
                            rs.getString("BusinessScope")
                    ));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return suppliers;
    }

    public boolean updateSupplier(SupplierData supplier,int selectedID)
    {
// 执行数据库更新
        String updateSQL = "UPDATE PManageStore.Supplier SET " +
                "vSupplierName = ?, cContanctMan = ?, cContanctPhone = ?, " +
                "vContanctAddress = ?, Description = ?, cPostalCode = ?, " +
                "SimplifiedCode = ?, BusinessScope = ? " +
                "WHERE iSupplierId = ?";

        try (PreparedStatement pstmt = databaseManager.getInstance().getExistsConnection().prepareStatement(updateSQL)) {
            pstmt.setString(1, supplier.getSupplierName());
            pstmt.setString(2, supplier.getContactMan());
            pstmt.setString(3, supplier.getContactPhone());
            pstmt.setString(4, supplier.getContactAddress());
            pstmt.setString(5, supplier.getDescription().isEmpty() ? null : supplier.getDescription());
            pstmt.setString(6, supplier.getPostalCode());
            pstmt.setString(7, supplier.getSimplifiedCode());
            pstmt.setString(8, supplier.getBusinessScope());
            pstmt.setInt(9, selectedID);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "修改成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                //searchButton.doClick(); // 刷新列表
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "修改失败：记录不存在", "错误", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) { // 唯一约束冲突
                JOptionPane.showMessageDialog(null, "供货单位名称已存在！", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "数据库错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
    }
}
