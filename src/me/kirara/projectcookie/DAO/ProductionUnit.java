package me.kirara.projectcookie.DAO;

import me.kirara.projectcookie.Data.ManufacturerData;

import me.kirara.projectcookie.Record.ManufacturerOption;
import me.kirara.projectcookie.Searchable;
import me.kirara.projectcookie.databaseManager;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductionUnit implements Searchable<ManufacturerOption>
{
    private static final ProductionUnit instance = new ProductionUnit();
    public static ProductionUnit getInstance()
    {
        return instance;
    }
    private ProductionUnit()
    {}

    public boolean addUnit(ManufacturerData manufacturerData)
    {
        // 执行数据库插入
        String sql = "INSERT INTO PManageStore.Manufacturer (vFactuerName, cContanctMan, cContanctPhone, " +
                "vContanctAddress, Description, cPostalCode, SimplifiedCode, vBusinecssScope) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = databaseManager.getInstance().getExistsConnection().prepareStatement(sql)) {
            fillStatement(manufacturerData, pstmt);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) { // 处理唯一约束冲突
                JOptionPane.showMessageDialog(null, "生产单位名称已存在！", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "数据库错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
    }

    public boolean deleteUnit(int id)
    {
        String deleteSQL = "DELETE FROM PManageStore.Manufacturer WHERE iFacturerId = ?";
        try (PreparedStatement stmt = databaseManager.getInstance().getExistsConnection().prepareStatement(deleteSQL))
        {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0)
            {
                JOptionPane.showMessageDialog(null, "删除成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
            else
            {
                JOptionPane.showMessageDialog(null, "删除失败：记录不存在", "错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        catch (SQLException ex)
        {
            if (ex.getSQLState().equals("23000")) { // 外键约束错误
                JOptionPane.showMessageDialog(
                        null,
                        "无法删除！该生产单位已被药品信息关联，请先修改相关药品记录。",
                        "存在依赖",
                        JOptionPane.ERROR_MESSAGE
                );
            }
            else
            {
                JOptionPane.showMessageDialog(
                        null,
                        "数据库错误: " + ex.getMessage(),
                        "错误",
                        JOptionPane.ERROR_MESSAGE
                );
            }
            return false;
        }
    }

    public boolean updateUnit(ManufacturerData manufacturerData,int currentManufacturerId)
    {
        // 执行数据库更新
        String sql = "UPDATE PManageStore.Manufacturer SET " +
                "vFactuerName = ?, cContanctMan = ?, cContanctPhone = ?, " +
                "vContanctAddress = ?, Description = ?, cPostalCode = ?, " +
                "SimplifiedCode = ?, vBusinecssScope = ? " +
                "WHERE iFacturerId = ?";

        try (PreparedStatement pstmt = databaseManager.getInstance().getExistsConnection().prepareStatement(sql))
        {
            fillStatement(manufacturerData, pstmt);
            pstmt.setInt(9, currentManufacturerId);

            int rows = pstmt.executeUpdate();
            if (rows > 0)
            {
                JOptionPane.showMessageDialog(null, "修改成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
            else
            {
                JOptionPane.showMessageDialog(null, "修改失败：记录不存在", "错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        catch (SQLException ex)
        {
            if (ex.getErrorCode() == 1062)
            { // 处理唯一约束冲突
                JOptionPane.showMessageDialog(null, "生产单位名称已存在！", "错误", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "数据库错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
    }

    private void fillStatement(ManufacturerData manufacturerData, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, manufacturerData.getName());
        pstmt.setString(2, manufacturerData.getContact());
        pstmt.setString(3, manufacturerData.getPhone());
        pstmt.setString(4, manufacturerData.getAddress());
        pstmt.setString(5, manufacturerData.getDescription().isEmpty() ? null : manufacturerData.getDescription());
        pstmt.setString(6, manufacturerData.getPostalCode());
        pstmt.setString(7, manufacturerData.getSimplifiedCode());
        pstmt.setString(8, manufacturerData.getvBusinecssScope());
        //return pstmt;
    }

    @Override
    public  List<ManufacturerOption> search(String keyword) {
        List<ManufacturerOption> units = new ArrayList<>();
        String sql = "SELECT iFacturerId, vFactuerName, cContanctMan, cContanctPhone, vContanctAddress, " +
                "Description, cPostalCode, SimplifiedCode, vBusinecssScope " +
                "FROM PManageStore.Manufacturer WHERE vFactuerName LIKE ?";

        try (PreparedStatement stmt = databaseManager.getInstance().getExistsConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    units.add(new ManufacturerOption(
                            rs.getInt("iFacturerId"),
                            rs.getString("vFactuerName"),
                            rs.getString("cContanctMan"),
                            rs.getString("cContanctPhone"),
                            rs.getString("vContanctAddress"),
                            rs.getString("Description"),
                            rs.getString("cPostalCode"),
                            rs.getString("SimplifiedCode"),
                            rs.getString("vBusinecssScope")
                    ));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return units;
    }
}
