package me.kirara.projectcookie;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

/**
 * 本地化管理器
 * <p>
 * 负责管理资源文件和本地化字符串
 * @author SimonMihoyo
 * @version ReAlpha 0.6.3
 * @since ReAlpha 0.6.3
 * @see iniFileManager
 * @see ResourceBundle
 */
public class LocalizationManager {
    private static final String BASE_NAME = "Locale.Strings"; // 资源文件基础名称
    private static LocalizationManager instance; // 单例实例
    private ResourceBundle bundle; // 资源束
    private final Locale locale;

    // 私有构造函数，初始化 ResourceBundle
    private LocalizationManager() {
        // 获取 iniFileManager 配置的语言和国家
        String language = iniFileManager.getInstance().get("Locale", "Language");
        String country = iniFileManager.getInstance().get("Locale", "Country");

        // 初始化语言环境

        if (language != null && !language.isEmpty() && country != null && !country.isEmpty()) {
            locale = Locale.of(language, country); // 使用自定义语言环境
        } else {
            locale = Locale.getDefault(); // 回退到默认语言环境
        }

        // 加载资源文件
        try {
            bundle = ResourceBundle.getBundle(BASE_NAME, locale);
        } catch (MissingResourceException e) {
            // 如果资源文件不存在，回退到默认资源文件
            bundle = ResourceBundle.getBundle(BASE_NAME, Locale.getDefault());
        }
    }

    // 获取单例实例
    public static synchronized LocalizationManager getInstance() {
        if (instance == null) {
            instance = new LocalizationManager();
        }
        return instance;
    }

    // 获取本地化字符串
    public String getLocalizedString(String key) {
        if (key == null || key.isEmpty()) {
            return "";
        }
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            // 如果键不存在，返回键本身或默认值rena
            return key;
        }
    }

    public Locale getLocale()
    {
        return locale;
    }
}

