import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB
{
    // 不带URL的连接
    public static Connection getConnection(String UserName, String Password) throws ClassNotFoundException, SQLException
    {
        // todo: 修改数据库的名字
        Connection con = getConnection(UserName, Password,"jdbc:mysql://localhost:3306/mydatabase");
        return con;
    }
    // 带URL的连接
    public static Connection getConnection(String UserName, String Password, String url) throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection(url,UserName, Password);
        return con;
    }
    // 关闭连接
    public static void closeConnection(Connection con) throws SQLException
    {
        if(con!=null)
        {
            con.close();
        }
    }
}
