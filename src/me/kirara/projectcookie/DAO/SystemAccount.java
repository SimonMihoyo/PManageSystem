package me.kirara.projectcookie.DAO;

import me.kirara.projectcookie.Record.UserOption;
import me.kirara.projectcookie.Searchable;
import me.kirara.projectcookie.databaseManager;
import me.kirara.projectcookie.Data.*;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SystemAccount implements Searchable<UserOption>
{
    private static SystemAccount instance = new SystemAccount();
    public static SystemAccount getInstance()
    {
        return instance;
    }
    private SystemAccount()
    {
    }

    public boolean AddUser(UserData user)
    {
        // 执行添加操作
        String addUserSQL = "INSERT INTO PManageStore.User (vUserName, vUserPass, vUserRealName, iTypeId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement addUserStatement = databaseManager.getInstance().getExistsConnection().prepareStatement(addUserSQL)) {
            addUserStatement.setString(1, user.getNickName());
            addUserStatement.setString(2, user.getPassword());
            addUserStatement.setString(3, user.getRealName());
            addUserStatement.setInt(4, user.getTypeID());
            addUserStatement.executeUpdate();
            JOptionPane.showMessageDialog(null,
                    "添加账号成功！",
                    "成功",
                    JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null,
                    "添加账号失败: " + ex.getMessage(),
                    "数据库错误",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean DeleteUser(int id)
    {
        String deleteSQL = "DELETE FROM PManageStore.User WHERE iUserId = ?";
        try (PreparedStatement stmt = databaseManager.getInstance().getExistsConnection().prepareStatement(deleteSQL)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0)
            {
                JOptionPane.showMessageDialog(null,
                        "用户 " + id  + " 已删除！",
                        "成功",
                        JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
            else
            {
                JOptionPane.showMessageDialog(null,
                        "删除失败：用户不存在！",
                        "错误",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null,
                    "删除用户失败: " + ex.getMessage(),
                    "数据库错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean AddSystemType(SystemType systemType)
    {
        try {
            // 插入数据库
            String sql = "INSERT INTO PManageStore.Type (cTypeName, Annotation) VALUES (?, ?)";
            PreparedStatement pstmt = databaseManager.getInstance().getExistsConnection().prepareStatement(sql);
            pstmt.setString(1, systemType.getName());
            pstmt.setString(2, systemType.getDescription());
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "数据库操作失败: " + ex.getMessage(),
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean DeleteSystemType(int id)
    {
        try {
            // 删除数据库中的记录
            String sql = "DELETE FROM PManageStore.Type WHERE iTypeId = ?";
            PreparedStatement pstmt = databaseManager.getInstance().getExistsConnection().prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "数据库操作失败: " + ex.getMessage(),
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateUser(int userId, String newPassword, String newRealName) {
        StringBuilder updateSQL = new StringBuilder("UPDATE PManageStore.User SET ");
        List<String> updates = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (newPassword != null && !newPassword.isEmpty()) {
            updates.add("vUserPass = ?");
            params.add(newPassword);
        }
        if (newRealName != null && !newRealName.isEmpty()) {
            updates.add("vUserRealName = ?");
            params.add(newRealName);
        }

        if (updates.isEmpty()) {
            return false; // 没有需要更新的字段
        }

        updateSQL.append(String.join(", ", updates));
        updateSQL.append(" WHERE iUserId = ?");
        params.add(userId);

        try (PreparedStatement stmt = databaseManager.getInstance().getExistsConnection().prepareStatement(updateSQL.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "修改用户失败: " + ex.getMessage(),
                    "数据库错误",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public List<UserOption> search(String keyword) {
        List<UserOption> users = new ArrayList<>();
        String sql = "SELECT u.iUserId, u.vUserName, u.vUserRealName, u.iTypeId, t.cTypeName " +
                "FROM PManageStore.User u " +
                "JOIN PManageStore.Type t ON u.iTypeId = t.iTypeId " +
                "WHERE u.vUserName LIKE ?";

        try (PreparedStatement stmt = databaseManager.getInstance().getExistsConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(new UserOption(
                            rs.getInt("iUserId"),
                            rs.getString("vUserName"),
                            rs.getString("vUserRealName"),
                            rs.getInt("iTypeId"),
                            rs.getString("cTypeName")
                    ));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }

    // 新增方法：获取所有系统类型
    public List<SystemType> getAllSystemTypes() {
        List<SystemType> types = new ArrayList<>();
        String sql = "SELECT iTypeId, cTypeName, Annotation FROM PManageStore.Type";

        try (PreparedStatement stmt = databaseManager.getInstance().getExistsConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("iTypeId");
                String name = rs.getString("cTypeName");
                String description = rs.getString("Annotation");
                types.add(new SystemType(id, name, description));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "获取账号类型失败: " + ex.getMessage(),
                    "数据库错误",
                    JOptionPane.ERROR_MESSAGE);
        }
        return types;
    }
}
