// Project Cookie SystemUtil.java
// 
// Copyright (C) 2025 SimonMihoyo
// 
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see https://www.gnu.org/licenses/.
//
// @since Alpha 0.2.7
// @author SimonMihoyo

package me.kirara.projectcookie;

/**
 * 系统工具类
 * @since Alpha 0.2.7
 * @author SimonMihoyo
 * @see OS
 */
public class SystemUtil
{
    /**
     * 操作系统类型枚举
     * @since Alpha 0.2.7
     * @author SimonMihoyo
     */
    public enum OS {
        WINDOWS("Windows"),
        MACOS("macOS"),
        LINUX("Linux"),
        SOLARIS("Solaris"),
        HARMONY("HarmonyOS"),
        UNKNOWN("Unknown");

        private final String friendlyName;

        /**
         * 构造器
         * @param friendlyName 友好名称
         */
        OS(String friendlyName) {
            this.friendlyName = friendlyName;
        }

        /**
         * 获取OS的友好名称
         * @return 友好名称
         */
        public String getFriendlyName() {
            return friendlyName;
        }
    }

    /**
     * 获取当前操作系统类型
     * @return 操作系统类型
     * @see OS
     * @since 2025-04-20,Alpha 0.2.7
     * @author SimonMihoyo
     */
    public static OS getOS()
    {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win"))
        {
            return OS.WINDOWS;
        }
        else if (osName.contains("mac"))
        {
            return OS.MACOS;
        }
        else if (osName.contains("linux"))
        {
            return OS.LINUX;
        }
        else if (osName.contains("sunos"))
        {
            return OS.SOLARIS;
        }
        else if (osName.contains("harmony"))
        {
            return OS.HARMONY;
        }
        else
        {
            return OS.UNKNOWN;
        }
    }

    /**
     * 获取当前操作系统类型名称
     * @return 操作系统类型名称
     * @see #getOS()
     * @since 2025-05-26,ReAlpha 0.6.2
     * @author SimonMihoyo
     */
    public static String getOSName()
    {
        return getOS().getFriendlyName();
    }

    /**
     * 获取配置文件路径
     * @param relativePath 相对路径
     * @return 配置文件路径
     * @see #getOS()
     * @since 2025-04-20,Alpha 0.2.7
     * @author SimonMihoyo
     */
    // todo : 完善配置文件路径获取(Solaris,HarmonyOS)
    public static String getConfigPath(String relativePath) {
        String basePath = switch (getOS()) {
            case WINDOWS -> System.getenv("APPDATA") + "\\com.SFO.authentication\\";
            case MACOS -> System.getProperty("user.home") + "/Library/Application Support/com.SFO.authentication/";
            default -> System.getProperty("user.home") + "/.config/com.SFO.authentication/";
        };
        return basePath + relativePath;
    }


}