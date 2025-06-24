package me.kirara.projectcookie.Data;

public class SupplierData
{
    private final String vSupplierName;
    private final String cContanctMan;
    private final String cContanctPhone;
    private final String vContanctAddress;
    private final String Description;
    private final String cPostalCode;
    private final String SimplifiedCode;
    private final String BusinessScope;

    public SupplierData(String SupplierName, String Man, String Phone, String Address, String Description, String PostalCode, String Code, String BusinessScope)
    {
        this.vSupplierName = SupplierName;
        this.cContanctMan = Man;
        this.cContanctPhone = Phone;
        this.vContanctAddress = Address;
        this.Description = Description;
        this.cPostalCode = PostalCode;
        this.SimplifiedCode = Code;
        this.BusinessScope = BusinessScope;
    }

    public String getSupplierName()
    {
        return vSupplierName;
    }

    public String getContactMan()
    {
        return cContanctMan;
    }

    public String getContactPhone()
    {
        return cContanctPhone;
    }

    public String getContactAddress()
    {
        return vContanctAddress;
    }

    public String getDescription()
    {
        return Description;
    }

    public String getPostalCode()
    {
        return cPostalCode;
    }

    public String getSimplifiedCode()
    {
        return SimplifiedCode;
    }

    public String getBusinessScope()
    {
        return BusinessScope;
    }
}
