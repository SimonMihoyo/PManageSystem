package me.kirara.projectcookie.Data;

import me.kirara.projectcookie.Data.UserType.*;

public class LoginStatus {
    public final int LoginStatus;
    public static String LoginName = "";
    public final UserType.Type userType;

    public LoginStatus(int loginStatus, String loginName, String usertype) { // 修改构造方法
        LoginStatus = loginStatus;
        LoginName = loginName;
        userType = ConstructUserType(usertype);
    }

    @Override
    public String toString() {
        return "LoginStatus{" +
                "LoginStatus=" + LoginStatus +
                ", LoginName='" + LoginName + '\'' +
                ", UserType='" + userType + '\'' +
                '}';
    }

    private UserType.Type ConstructUserType(String userType)
    {
        return switch (userType) {
            case "Admin" -> Type.ADMIN;
            case "User" -> Type.USER;
            case "Guest" -> Type.GUEST;
            case "Auditor" -> Type.AUDITOR;
            case "Demo" -> Type.DEMO;
            default -> Type.UNKNOWN;
        };
    }

    public String getLoginName() {
        return LoginName;
    }

    public int getLoginStatus() {
        return LoginStatus;
    }

    public UserType.Type getUserType() {
        return userType;
    }
}