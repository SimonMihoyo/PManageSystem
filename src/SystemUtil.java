/**
 * PManageSystem SystemUtil.java
 * <p>
 * Copyright (C) 2025 SimonMihoyo
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * <p>
 * @since 2025-04-20
 * @author SimonMihoyo
 */

public class SystemUtil
{
    public enum OS
    {
        WINDOWS, MACOS, LINUX
    }

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
        else
        {
            return OS.LINUX;
        }
    }

    public static String getConfigPath(String relativePath) {
        String basePath;
        switch (getOS()) {
            case WINDOWS:
                basePath = System.getenv("APPDATA") + "\\com.SFO.authentication\\";
                break;
            case MACOS:
                basePath = System.getProperty("user.home") + "/Library/Application Support/com.SFO.authentication/";
                break;
            default:
                basePath = System.getProperty("user.home") + "/.config/com.SFO.authentication/";
                break;
        }
        return basePath + relativePath;
    }


}