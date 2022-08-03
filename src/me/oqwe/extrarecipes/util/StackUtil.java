package me.oqwe.extrarecipes.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StackUtil {

	/**
	 * Create an itemstack
	 * 
	 * @param material Material of the stack
	 * @param displayName Display name of the stack
	 * @return The created itemstack
	 */
	public static ItemStack stack(Material material, String displayName) {
		
		ItemStack stack = new ItemStack(material);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(displayName);
		stack.setItemMeta(meta);
		return stack;
		
	}
	
}
