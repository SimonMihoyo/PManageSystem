// PManageSystem operateDB.java
// Copyright (C) 2025 SimonMihoyo
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class operateDB
{
    /**
     * 获取数据库连接（默认数据库连接地址（本地数据库））
     * @param UserName 数据库用户名
     * @param Password 数据库密码
     * @return 数据库连接
     */
    public static Connection getConnection(String UserName, String Password) throws ClassNotFoundException, SQLException {
        if (UserName == null || UserName.trim().isEmpty() || Password == null || Password.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名或密码不能为空");
        }
        return getConnection(UserName, Password, "jdbc:mysql://localhost:3306/");
    }


    /**
     * 获取数据库连接（指定数据库连接地址）
     * @param UserName 数据库用户名
     * @param Password 数据库密码
     * @param url 数据库连接地址
     * @return 数据库连接
     */
    public static Connection getConnection(String UserName, String Password, String url) throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, UserName, Password);
    }

    /**
     * 关闭数据库连接
     * @param con 数据库连接
     */
    public static void closeConnection(Connection con) throws SQLException
    {
        if(con!=null)
        {
            con.close();
        }
    }

    /**
     * 执行SQL语句
     * @param con 数据库连接
     * @param sql SQL语句
     * @return 执行结果
     */
    public static String executeSQL(Connection con, String sql) throws SQLException
    {
        if(con!=null)
        {
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            return stmt.getResultSet().getString(1);
        }
        return null;
    }

    /**
     * 事务提交
     * @param con 数据库连接
     */
    public static void commit(Connection con) throws SQLException
    {
        con.setAutoCommit(false);
        con.commit();
        con.setAutoCommit(true);
    }

    /**
     * 检查当前用户是否具有修改表的权限
     * @param con 数据库连接
     * @param user 数据库用户名
     * @param privilege 权限类型（如 ALTER 或 UPDATE）
     * @return 如果具有权限，返回true；否则返回false
     */
    public static boolean checkUserPrivilege(Connection con, String user, String privilege) throws SQLException {
        String sql = "SELECT PRIVILEGE_TYPE FROM information_schema.USER_PRIVILEGES WHERE GRANTEE = ? AND PRIVILEGE_TYPE = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            // 格式化GRANTEE为 '用户名'@'%'（假设用户连接来自任何主机）
            stmt.setString(1, "'" + user + "'@'localhost'");
            stmt.setString(2, privilege);

            try (ResultSet rs = stmt.executeQuery()) {
                // 如果查询到结果，则表示用户具有该权限
                return rs.next();
            }
        }
    }

    // operateDB.java 新增方法
    /**
     * 执行SQL文件初始化数据库
     * @param con 数据库连接
     * @param filePath SQL文件路径
     */
    // 在这debug了半小时，我真的服了
    public static void executeSQLFile(Connection con, String filePath) throws SQLException, IOException {
        if (con == null) return;
        con.setAutoCommit(false);
        try (Statement stmt = con.createStatement()) {
            // 创建并选择数据库
            stmt.execute("CREATE DATABASE IF NOT EXISTS PManageStore");
            stmt.execute("USE PManageStore");
            // 读取 SQL 文件
            String sqlContent = new String(Files.readAllBytes(Paths.get(filePath)));
            // 分割语句
            String[] sqlStatements = sqlContent.split("; *(?=\n|$)");
            for (String sql : sqlStatements) {
                sql = sql.replaceAll("(?m)^--.*?\n", "").trim();
                if (sql.isEmpty()) continue;
                // 大家以后少写点--
                stmt.execute(sql);
            }
            con.commit();
        } catch (SQLException e) {
            con.rollback();
            System.err.println("SQL 错误: " + e.getMessage());
            throw e;
        } catch (IOException e) {
            con.rollback();
            System.err.println("文件读取错误: " + e.getMessage());
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }

    /**
     * 事务回滚
     * @param con 数据库连接
     */
    public static void rollBack(Connection con) throws SQLException
    {
        con.setAutoCommit(false);
        con.rollback();
        con.setAutoCommit(true);
    }

    // 建立连接
    public static Connection setupConnection(IniFileManager iniFileManager)
    {
        try
        {
             Connection con =getConnection(iniFileManager.get("MinUser","Username"),
                    Encryption.decrypt(iniFileManager.get("MinUser","EncryptedPassword"),
                                       iniFileManager.get("MinUser","Key"),
                                       iniFileManager.get("MinUser","IV")));
            return con;
        } catch (ClassNotFoundException | SQLException | InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | InvalidKeyException | BadPaddingException e) {
            throw new RuntimeException(e);
        }

    }
}
