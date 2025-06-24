package me.kirara.projectcookie.Data;

public class ManufacturerData
{
    private final String name;
    private final String contact;
    private final String phone;
    private final String address;
    private final String description;
    private final String postalCode;
    private final String simplifiedCode;
    private final String vBusinecssScope;

    public ManufacturerData(String name, String contact, String phone, String address, String description, String postalCode, String simplifiedCode, String vBusinecssScope)
    {
        this.name = name;
        this.contact = contact;
        this.phone = phone;
        this.address = address;
        this.description = description;
        this.postalCode = postalCode;
        this.simplifiedCode = simplifiedCode;
        this.vBusinecssScope = vBusinecssScope;
    }

    public String getName()
    {
        return name;
    }

    public String getContact()
    {
        return contact;
    }

    public String getPhone()
    {
        return phone;
    }

    public String getAddress()
    {
        return address;
    }

    public String getDescription()
    {
        return description;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public String getSimplifiedCode()
    {
        return simplifiedCode;
    }

    public String getvBusinecssScope()
    {
        return vBusinecssScope;
    }
}
