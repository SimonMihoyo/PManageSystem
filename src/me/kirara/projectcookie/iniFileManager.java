/**
 * PManageSystem IniFileManager.java
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * @author SimonMihoyo
 */

package me.kirara.projectcookie;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * INI配置文件管理器
 * <p>
 * 提供对INI配置文件的读写操作
 *
 * @author SimonMihoyo
 * @version ReAlpha 0.6.4
 * @since ReAlpha 0.6.4
 */
public class iniFileManager {
    private static volatile iniFileManager instance;
    private final LinkedHashMap<String, LinkedHashMap<String, String>> sections;
    private final static String iniPath = SystemUtil.getConfigPath("settings.ini");
    private final Path filePath;

    // 私有构造函数，防止外部实例化
    private iniFileManager(String iniPath) {
        this.sections = new LinkedHashMap<>();
        // 初始化默认节（空字符串表示默认节）
        this.sections.put("", new LinkedHashMap<>());
        this.filePath = Path.of(iniPath);
        try {
            if (exists(iniPath)) {
                load(this.filePath);
            } else {
                create(iniPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 无参构造函数调用带参数的构造函数
    private iniFileManager() {
        this(iniPath);
    }

    /**
     * 匹配ini文件地址
     * @param input ini路径
     * @return 是否匹配成功
     */
    // todo： 考虑是否移出
    public static boolean matchIniPath(String input) {
        // 去除可能存在的引号
        if (input.startsWith("\"") && input.endsWith("\"")) {
            input = input.substring(1, input.length() - 1);
        }
        // 正则表达式：匹配Windows或Unix风格路径，且以.ini结尾
        String regex = "^([a-zA-Z]:)?[/\\\\].*\\.ini$";
        return input.matches(regex);
    }

    // 处理命令行参数并返回 iniFileManager 实例
    public static iniFileManager getInstanceFromArgs(String[] args) {
        if (Arrays.stream(args).anyMatch(iniFileManager::matchIniPath)) {
            // 处理至少有一个匹配的情况
            return getInstance(args[1]);
        } else {
            // 处理所有参数都不匹配的情况
            return getInstance();
        }
    }

    // 获取单例实例的方法
    public static iniFileManager getInstance() {
        if (instance == null) {  // 第一次检查，减少同步开销
            synchronized (iniFileManager.class) {
                if (instance == null) {  // 第二次检查，确保线程安全
                    instance = new iniFileManager();
                }
            }
        }
        return instance;
    }

    // 获取指定路径的单例实例的方法
    public static iniFileManager getInstance(String iniPath) {
        if (instance == null) {  // 第一次检查，减少同步开销
            synchronized (iniFileManager.class) {
                if (instance == null) {  // 第二次检查，确保线程安全
                    instance = new iniFileManager(iniPath);
                }
            }
        }
        return instance;
    }

    /**
     * 从文件加载INI配置
     * @param path 文件路径
     */
    private void load(Path path) throws IOException {
        String currentSection = "";
        for (String line : Files.readAllLines(path)) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith(";") || line.startsWith("#")) {
                continue; // 跳过空行和注释
            }

            // 处理节（section）
            if (line.startsWith("[") && line.endsWith("]")) {
                currentSection = line.substring(1, line.length() - 1).trim();
                sections.putIfAbsent(currentSection, new LinkedHashMap<>());
                continue;
            }

            // 处理键值对
            int equalsIndex = line.indexOf('=');
            if (equalsIndex != -1) {
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
    private void save(Path path) throws IOException {
        StringBuilder content = new StringBuilder();

        // 先处理默认节（空字符串section）
        LinkedHashMap<String, String> defaultSection = sections.get("");
        if (defaultSection != null && !defaultSection.isEmpty()) {
            appendSection(content, "", defaultSection);
        }

        // 处理其他节
        for (Map.Entry<String, LinkedHashMap<String, String>> entry : sections.entrySet()) {
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
    private void appendSection(StringBuilder sb, String section, LinkedHashMap<String, String> keyValues) {
        if (!section.isEmpty()) {
            sb.append("[").append(section).append("]\n");
        }
        for (Map.Entry<String, String> entry : keyValues.entrySet()) {
            sb.append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
        }
        sb.append("\n");
    }

    /**
     * 获取配置值
     * @param section 节名称（空字符串表示默认节）
     * @param key 键名称
     */
    public String get(String section, String key) {
        LinkedHashMap<String, String> sectionData = sections.get(section);
        return (sectionData != null) ? sectionData.get(key) : null;
    }

    /**
     * 设置配置值
     * @param section 节名称（空字符串表示默认节）
     * @param key 键名称
     * @param value 值
     */
    public void set(String section, String key, String value) {
        sections.putIfAbsent(section, new LinkedHashMap<>());
        sections.get(section).put(key, value);
        try {
            save(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断INI文件是否存在
     * @param iniPath INI文件路径
     */
    private boolean exists(String iniPath) {
        File file = new File(iniPath);
        return file.exists();
    }

    /**
     * 创建新的INI文件
     * @param iniPath INI文件路径
     */
    private void create(String iniPath) throws IOException {
        Path path = Path.of(iniPath);
        Files.createDirectories(path.getParent());
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        load(path);
        set("System", "FirstUse", "true");
        set("Locale", "Language", "zh");
        set("Locale", "Country", "CN");
    }
}