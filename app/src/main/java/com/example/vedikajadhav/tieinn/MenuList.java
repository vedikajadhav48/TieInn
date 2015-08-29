package com.example.vedikajadhav.tieinn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vedika Jadhav on 8/23/2015.
 */
public class MenuList {
    /**
     * An array of menu items.
     */
    public static List<Menu> ITEMS = new ArrayList<Menu>();

    /**
     * A map of items, by ID.
     */
    public static Map<String, Menu> ITEM_MAP = new HashMap<String, Menu>();

    static {
        // Add 5 sample items.
        addItem(new Menu("1", "Discussion Board"));
        addItem(new Menu("2", "Courses"));
        addItem(new Menu("3", "Account"));
        addItem(new Menu("4", "Contact Us"));
        addItem(new Menu("5", "Logout"));
    }

    private static void addItem(Menu item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class Menu {
        public String id;
        private String mMenuName;

        public Menu(String id, String menuName) {
            this.id = id;
            this.mMenuName = menuName;
        }

        @Override
        public String toString() {
            return mMenuName;
        }

        public String getMenuName() {
            return mMenuName;
        }

        public void setMenuName(String menuName) {
            mMenuName = menuName;
        }
    }
}
