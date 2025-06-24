package me.kirara.projectcookie;

public class TabPanelInitializer
{
    private static final TabPanelInitializer instance = new TabPanelInitializer();

    private TabPanelInitializer() {
    }

    public static TabPanelInitializer getInstance()
    {
        return instance;
    }


}
