package me.oqwe.extrarecipes.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StackUtil {

	public static ItemStack stack(Material material, String displayName) {
		
		ItemStack stack = new ItemStack(material);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(displayName);
		stack.setItemMeta(meta);
		return stack;
		
	}
	
}
