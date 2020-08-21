package cz.MilanT.drazby.libraries;

import cz.MilanT.drazby.libraries.reflection.MinecraftReflectionProvider;
import cz.MilanT.drazby.libraries.reflection.ReflectionUtil;
import org.bukkit.inventory.ItemStack;

public class Minecraft {
    public static String getItemFullName(ItemStack itemStack) {
        final String[] item = {itemStack.getType().name()};
        ReflectionUtil.newCall().getMethod(MinecraftReflectionProvider.CRAFT_ITEMSTACK, "asNMSCopy", ItemStack.class)
                .get().passIfValid(reflectionMethod -> {
            Object nmsItemStack = reflectionMethod.invokeIfValid(null, itemStack);
            item[0] = ReflectionUtil.newCall().getMethod(MinecraftReflectionProvider.NMS_ITEMSTACK, "getName").get().invokeIfValid(nmsItemStack);
        });
        return item[0];
    }
}
