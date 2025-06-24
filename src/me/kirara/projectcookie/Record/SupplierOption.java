package me.kirara.projectcookie.Record;

import org.jetbrains.annotations.NotNull;

public record SupplierOption(int id, String name, String contact, String phone, String address, String description,
                             String postalCode, String simplifiedCode, String businessScope) {
    @Override
    public @NotNull String toString() {
        return name + " (" + contact + ")";
    }
}
