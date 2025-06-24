package me.kirara.projectcookie.Data;

public class TypeOption {
    private final int id;
    private final String name;

    public TypeOption(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}