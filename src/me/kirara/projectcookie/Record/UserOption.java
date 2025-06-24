package me.kirara.projectcookie.Record;

import org.jetbrains.annotations.NotNull;

public record UserOption(int id, String username, String realName, int typeId, String typeName) {

    @Override
    public @NotNull String toString() {
        return username;
    }
}
