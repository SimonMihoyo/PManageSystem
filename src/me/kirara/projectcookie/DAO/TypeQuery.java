package me.kirara.projectcookie.DAO;

import me.kirara.projectcookie.Data.TypeOption;
import me.kirara.projectcookie.databaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TypeQuery {
    public static List<TypeOption> getTypeOptions()
    {
        return getTypeOptions(databaseManager.getInstance().getExistsConnection());
    }

    public static List<TypeOption> getTypeOptions(Connection connection)
    {
        List<TypeOption> options = new ArrayList<>();
        String sql = "SELECT iTypeId, cTypeName FROM PManageStore.Type";

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("iTypeId");
                String name = rs.getString("cTypeName");
                options.add(new TypeOption(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 在实际应用中应使用日志记录器
            System.err.println("Error loading account types: " + e.getMessage());
        }
        return options;
    }
}