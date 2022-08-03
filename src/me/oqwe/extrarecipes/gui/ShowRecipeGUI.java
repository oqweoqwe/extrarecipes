package me.oqwe.extrarecipes.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.oqwe.extrarecipes.Main;
import me.oqwe.extrarecipes.Recipe;
import me.oqwe.extrarecipes.util.StackUtil;

public class ShowRecipeGUI implements Listener {

	private final int[] craftingmatrix = new int[] { 10, 11, 12, 19, 20, 21, 28, 29, 30 };
	private boolean ignoreClose = false;
	private int index = 0;
	private List<Inventory> guis;
	private final Player player;
	
	public ShowRecipeGUI(Player player) {
		
		this.player = player;
		
		guis = prepareInventories();
		
		try {
			open();
		} catch (IndexOutOfBoundsException e) {
			player.sendMessage(ChatColor.YELLOW+"There are no custom recipes");
		}
		
	}
	
	/**
	 * Moves to the next inventory if one exists
	 */
	public void next() {
		if (index+1 < guis.size()) {
			index++;
			try {
				open();
			} catch (IndexOutOfBoundsException e) {
			}
			
		}
	}
	
	/**
	 * Close inventory
	 */
	public void close() {
		player.closeInventory();
	}
	
	/**
	 * Moves to the previous inventory if one exists
	 */
	public void previous() {
		if (index > 0) {
			index--;
			try {
				open();
			} catch (IndexOutOfBoundsException e) {
			}			
		}
	}
	
	/**
	 * Delete inventory from memory and local storage and unload it from bukkit
	 */
	public void delete() {
		
		Recipe rec = null;
		for (var recipe : Main.getRecipes())
			if (recipe.getID().equals(getUUIDFromPersistentData())) {
				Bukkit.removeRecipe(recipe.getKey());
				rec = recipe;
			}
		
		Main.getRecipes().remove(rec);
		
		Main.getDataFile().delete(getUUIDFromPersistentData().toString());
		
		index = 0;
		guis = prepareInventories();
		
		if (guis.size() == 0) {
			ignoreClose = false;
			player.closeInventory();
			player.sendMessage(ChatColor.YELLOW+"Deleted recipe");
			return;
		}
		
		try {
			open();
		} catch (IndexOutOfBoundsException e) {
		}
		
		player.sendMessage(ChatColor.YELLOW+"Deleted recipe");
		
	}
	
	/**
	 * @return The UUID of the recipe that is hidden in the persistent data of the result itemstack
	 */
	private UUID getUUIDFromPersistentData() {
		return UUID.fromString(guis.get(index).getItem(24).getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "recipeuuid"), PersistentDataType.STRING));
	}
	
	/**
	 * Opens the inventory currently indicated by the index for the player
	 */
	public void open() throws IndexOutOfBoundsException {
		ignoreClose = true;
		player.openInventory(guis.get(index));
		ignoreClose = false;
	}
	
	/**
	 * Generates a list of inventories representing all custom recipis
	 * 
	 * @param recipes List of recipe objects
	 * @return List of inventories in order of creation
	 */
	private List<Inventory> prepareInventories() {
		List<Inventory> inventories = new ArrayList<Inventory>();
		List<Recipe> recipes = Main.getRecipes();

		for (int i = 0; i < recipes.size(); i++) {
			
			Inventory inv = Bukkit.createInventory(null, 54, "Recipe #"+(i+1));

			// first fill with panes
			ItemStack pane = StackUtil.stack(Material.BLACK_STAINED_GLASS_PANE, " ");
			for (int j = 0; j < 54; j++)
				inv.setItem(j, pane);
			
			if (player.hasPermission("extrarecipes.delete")) {
				// set delete button
				ItemStack delete = StackUtil.stack(Material.TNT, ChatColor.RED + "Delete this recipe");
				inv.setItem(53, delete);
			}			

			// set close button
			ItemStack close = StackUtil.stack(Material.BARRIER, ChatColor.RED + "Close");
			inv.setItem(49, close);

			// setup arrows
			if (i > 0) {
				// set previous arrow
				ItemStack previous = StackUtil.stack(Material.ARROW, ChatColor.YELLOW + "Previous recipe");
				inv.setItem(48, previous);
			}
			if (i < recipes.size() - 1) {
				// set next arrow
				ItemStack next = StackUtil.stack(Material.ARROW, ChatColor.YELLOW + "Next recipe");
				inv.setItem(50, next);

			}

			// set result, the UUID of the recipe is hidden in this itemstacks persistent data container because i couldn't come up with
			// a better way to do it and this is kinda fun
			ItemStack result = new ItemStack(
					Material.getMaterial(recipes.get(i).getRecipe().getResult().getType().toString()));
			ItemMeta meta = result.getItemMeta();
			meta.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), "recipeuuid"), PersistentDataType.STRING, recipes.get(i).getID().toString());
			result.setItemMeta(meta);
			inv.setItem(24, result);

			// set items in crafting matrix
			for (var l : craftingmatrix) {

				inv.setItem(l,
						new ItemStack(Material.getMaterial(recipes.get(i).getIngredients().get(matrixSlotToIndex(l)))));

			}
			inventories.add(inv);

		}

		return inventories;
	}
	
	public List<Inventory> getGuis() {
		return guis;
	}
	
	public boolean getIgnoreClose() {
		return ignoreClose;
	}

	// this is not very sexy but it was the easiest way to do what i wanted
	private static int matrixSlotToIndex(int i) {
		switch (i) {

		case 10:
			return 0;
		case 11:
			return 1;
		case 12:
			return 2;
		case 19:
			return 3;
		case 20:
			return 4;
		case 21:
			return 5;
		case 28:
			return 6;
		case 29:
			return 7;
		case 30:
			return 8;
		default:
			return -1;

		}
	}

}
