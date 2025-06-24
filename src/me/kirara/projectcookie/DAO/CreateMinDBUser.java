package me.kirara.projectcookie.DAO;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import me.kirara.projectcookie.DatabaseInitializer;
import me.kirara.projectcookie.Encryption;
import me.kirara.projectcookie.databaseManager;
import me.kirara.projectcookie.iniFileManager;
import me.kirara.projectcookie.Data.WizardData;

public class CreateMinDBUser
{

    public static void createMinDBUser(WizardData data)
    {
        try {
            String StringedDBPassword = new String(data.DBpassword);
            String createUserSQL = "CREATE USER '" + data.DBusername + "'@'localhost' IDENTIFIED BY '" + StringedDBPassword + "'";
            Statement statement = DatabaseInitializer.getInstance().getCon().createStatement();
            statement.executeUpdate(createUserSQL);

            // 保存用户名，Key，IV和加密后的密码
            // todo: 考虑使用更安全的方式保存Key和IV
            iniFileManager.getInstance().set("MinUser", "Username", data.DBusername);
            String Key = Encryption.generateKey();
            String IV = Encryption.generateIV();
            String EncryptedPassword = Encryption.encrypt(new String(data.DBpassword), Key, IV);
            iniFileManager.getInstance().set("MinUser", "Key", Key);
            iniFileManager.getInstance().set("MinUser", "IV", IV);
            iniFileManager.getInstance().set("MinUser", "EncryptedPassword", EncryptedPassword);

            String grantAllSQL = "GRANT ALL PRIVILEGES ON PManageStore.* TO '" + data.DBusername + "'@'localhost'";
            statement.executeUpdate(grantAllSQL);

            String addUserSQL = "INSERT INTO PManageStore.User (vUserName, vUserPass, vUserRealName, iTypeId) VALUES (?, ?, ?, ?)";
            PreparedStatement addUserStatement = DatabaseInitializer.getInstance().getCon().prepareStatement(addUserSQL);
            addUserStatement.setString(1, data.username);
            addUserStatement.setString(2, new String(data.password));
            addUserStatement.setString(3, data.realName);
            addUserStatement.setInt(4, data.typeId);
            addUserStatement.executeUpdate();

            //JOptionPane.showMessageDialog(mainPanel, "用户添加成功！");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "添加用户失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            // todo: 考虑使用更可靠的日志
            ex.printStackTrace();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchPaddingException |
                 IllegalBlockSizeException | BadPaddingException | InvalidKeyException ex) {
            throw new RuntimeException(ex);
        }
    }
}
