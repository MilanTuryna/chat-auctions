package cz.MilanT.drazby.libraries.reflection;

import org.bukkit.Bukkit;

/**
 * Created by Jan on 18-02-2017.
 */
public class MinecraftReflectionProvider {
    public static final String MC_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    public static final Class<?> CRAFT_ITEMSTACK = getCBClass("inventory.CraftItemStack");
    public static final Class<?> NMS_ITEM = getNMSClass("Item");
    public static final Class<?> NMS_ITEMSTACK = getNMSClass("ItemStack");

    public static Class<?> getCBClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + MC_VERSION + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + MC_VERSION + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
