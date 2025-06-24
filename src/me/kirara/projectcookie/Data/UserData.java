package me.kirara.projectcookie.Data;

public class UserData {
    private final String nickName;
    private final String Password;
    private final String RealName;
    private final int TypeID;

    public UserData(String NickName, String Password, String RealName, int TypeID)
    {
        this.nickName = NickName;
        this.Password = Password;
        this.RealName = RealName;
        this.TypeID = TypeID;
    }

    @Override
    public String toString()
    {
        return "NickName: " + nickName + "\nPassword: " + Password + "\nRealName: " + RealName + "\nTypeID: " + TypeID;
    }

    public String getNickName() {
        return nickName;
    }

    public String getPassword() {
        return Password;
    }

    public String getRealName() {
        return RealName;
    }

    public int getTypeID() {
        return TypeID;
    }

}
