package com.jackie.bookpagerview.bean;

/**
 * Created by Jackie on 2018/4/17.
 */

public class MenuItem {

    public MenuItem(String text, boolean isSelected, int icon, int iconSelected) {
        this.text = text;
        this.isSelected = isSelected;
        this.icon = icon;
        this.iconSelected = iconSelected;
    }

    public boolean isSelected;
    public String text;
    public int icon;
    public int iconSelected;
}
