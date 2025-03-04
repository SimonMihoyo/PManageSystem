import javax.swing.*;
import java.io.*;

public class authentication
{
    public static void logInAuth(String username, String password)
    {
        String passwordPath ;//= "~/appdata/com.SFO.authentication/passwords";
        String osName = System.getProperty("os.name").toLowerCase();
        //System.out.println(osName);
        if(osName.contains("win"))
        {
            passwordPath = System.getenv("APPDATA") + "\\com.SFO.authentication\\passwords";
        }
        else if(osName.contains("mac"))
        {
            passwordPath = System.getProperty("user.home") + "/Library/Application Support/com.SFO.authentication/passwords";
        }
        else
        {
            passwordPath = System.getProperty("user.home") + "/.config/com.SFO.authentication/passwords";
        }
        if (!new File(passwordPath).exists())
        {
            JOptionPane.showMessageDialog(null, "Password file not found. Please contact the administrator.");
            return;
        }
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(passwordPath));
            String line;
            while((line = br.readLine())!= null)
            {
                String[] parts = line.split(":");
                if(parts[0].equals(username))
                {
                    if(parts[1].equals(password))
                    {
                        return;
                    }
                }
            }
            br.close();
        }
        catch(IOException e)
        {
            // todo: 使用更可靠的日志
            //e.printStackTrace();
        }
    }
}
