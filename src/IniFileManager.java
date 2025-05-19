// PManageSystem IniFileManager.java
// **********************************************
// *
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class IniFileManager
{
    private final LinkedHashMap<String, LinkedHashMap<String, String>> sections;

    /**
     * 构造函数
     */
    public IniFileManager()
    {
        this.sections = new LinkedHashMap<>();
        // 初始化默认节（空字符串表示默认节）
        this.sections.put("", new LinkedHashMap<>());
    }

    /**
     * 从文件加载INI配置
     * @param path 文件路径
     */
    public void load(Path path) throws IOException
    {
        String currentSection = "";
        for (String line : Files.readAllLines(path))
        {
            line = line.trim();
            if (line.isEmpty() || line.startsWith(";") || line.startsWith("#"))
            {
                continue; // 跳过空行和注释
            }

            // 处理节（section）
            if (line.startsWith("[") && line.endsWith("]"))
            {
                currentSection = line.substring(1, line.length() - 1).trim();
                sections.putIfAbsent(currentSection, new LinkedHashMap<>());
                continue;
            }

            // 处理键值对
            int equalsIndex = line.indexOf('=');
            if (equalsIndex != -1)
            {
                String key = line.substring(0, equalsIndex).trim();
                String value = line.substring(equalsIndex + 1).trim();
                sections.get(currentSection).put(key, value);
            }
        }
    }

    /**
     * 保存INI配置到文件
     * @param path 文件路径
     */
    public void save(Path path) throws IOException
    {
        StringBuilder content = new StringBuilder();

        // 先处理默认节（空字符串section）
        LinkedHashMap<String, String> defaultSection = sections.get("");
        if (defaultSection != null && !defaultSection.isEmpty())
        {
            appendSection(content, "", defaultSection);
        }

        // 处理其他节
        for (Map.Entry<String, LinkedHashMap<String, String>> entry : sections.entrySet())
        {
            String section = entry.getKey();
            if (section.isEmpty()) continue; // 默认节已处理
            appendSection(content, section, entry.getValue());
        }

        Files.writeString(path, content.toString());
    }

    /**
     * 追加节内容到StringBuilder
     * @param sb StringBuilder
     * @param section 节名称
     * @param keyValues 键值对
     */
    private void appendSection(StringBuilder sb, String section, LinkedHashMap<String, String> keyValues)
    {
        if (!section.isEmpty())
        {
            sb.append("[").append(section).append("]\n");
        }
        for (Map.Entry<String, String> entry : keyValues.entrySet())
        {
            sb.append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
        }
        sb.append("\n");
    }

    /**
     * 获取配置值
     * @param section 节名称（空字符串表示默认节）
     * @param key 键名称
     */
    public String get(String section, String key)
    {
        LinkedHashMap<String, String> sectionData = sections.get(section);
        return (sectionData != null) ? sectionData.get(key) : null;
    }

    /**
     * 设置配置值
     * @param section 节名称（空字符串表示默认节）
     * @param key 键名称
     * @param value 值
     */
    public void set(String section, String key, String value)
    {
        sections.putIfAbsent(section, new LinkedHashMap<>());
        sections.get(section).put(key, value);
    }

    public boolean exists(String iniPath)
    {
        File file = new File(iniPath);
        return file.exists();
    }

    /**
     * 创建新的INI文件
     * @param iniPath INI文件路径
     */
    public void create(String iniPath) throws IOException
    {
        Path path = Path.of(iniPath);
        Files.createDirectories(path.getParent());
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        load(path);
        set("System", "FirstUse", "true");
        save(path);
    }
}