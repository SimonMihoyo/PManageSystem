package me.kirara.projectcookie.Data;

public class SystemType
{
    private final int id; // 新增ID字段
    private final String Name;
    private final String Description;

    // 新增带ID的构造函数
    public SystemType(int id, String name, String description) {
        this.id = id;
        Name = name;
        Description = description;
    }

    // 保留原有构造函数
    public SystemType(String name, String description) {
        this.id = 0; // 添加时ID未知
        Name = name;
        Description = description;
    }

    // 新增ID的getter
    public int getId() {
        return id;
    }

    public String getName()
    {
        return Name;
    }

    public String getDescription()
    {
        return Description;
    }

    @Override
    public String toString()
    {
        return Name + " - " + Description;
    }
}
