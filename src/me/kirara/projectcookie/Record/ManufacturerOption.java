package me.kirara.projectcookie.Record;

import org.jetbrains.annotations.NotNull;

public record ManufacturerOption(int id, String name, String contact, String phone, String address, String description,
                                 String postalCode, String simplifiedCode, String vBusinecssScope) {
    @Override
    public @NotNull String toString() {
        return name + " (" + contact + ")";
    }
}
