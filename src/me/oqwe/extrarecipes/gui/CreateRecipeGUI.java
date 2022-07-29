package me.oqwe.extrarecipes.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.oqwe.extrarecipes.Main;
import me.oqwe.extrarecipes.Recipe;
import me.oqwe.extrarecipes.util.StackUtil;

public class CreateRecipeGUI implements Listener {

	// 49 is close button, 33 is save button, 24 is result
	
	private static final Inventory gui = Bukkit.createInventory(null, 54, "Create new recipe");
	private static final int[] allowedslots = new int[] { 10, 11, 12, 19, 20, 21, 24, 28, 29, 30 };
	private static final int[] craftingmatrix = new int[] { 10, 11, 12, 19, 20, 21, 28, 29, 30 };
	private static final int[] blockedslots = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 14, 15, 16, 17, 18, 22, 23,
			25, 26, 27, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };

	public static void resetGUI() {

		ItemStack pane = StackUtil.stack(Material.BLACK_STAINED_GLASS_PANE, " ");

		for (int i = 0; i < 54; i++)
			gui.setItem(i, pane);

		for (var i : allowedslots) {
			gui.setItem(i, new ItemStack(Material.AIR));
		}

		ItemStack close = StackUtil.stack(Material.BARRIER, ChatColor.RED+"Close");

		gui.setItem(49, close);

		ItemStack craft = StackUtil.stack(Material.CRAFTING_TABLE, ChatColor.YELLOW+"Save recipe");

		gui.setItem(33, craft);

	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {

		if (!e.getView().getTitle().equals("Create new recipe")) {
			return;
		}
		
		if (e.getRawSlot() == 49) {
			// close
			e.getWhoClicked().closeInventory();
			resetGUI();
			e.getWhoClicked().sendMessage(ChatColor.RED+"Recipe was not saved");
		}
		
		if (e.getRawSlot() == 33) {
			
			
			// TODO save to file and load into bukkit
			saveRecipe(e.getWhoClicked());
			
		}

		boolean cancel = false;

		for (var i : blockedslots) {
			if (e.getRawSlot() == i)
				cancel = true;
		}

		e.setCancelled(cancel);

	}
	
	public void saveRecipe(HumanEntity player) {
		// save to file and load into bukkit
		
		// first check that recipe has a result
		if (gui.getItem(24) == null) {
			player.sendMessage(ChatColor.RED+"You must enter a result before the recipe can be saved");
			return;
		}
		
		// make a list of the ingredients in left to right top to bottom order
		List<String> list = new ArrayList<String>();
		int air = 0;
		for (var i : craftingmatrix) {
			if (gui.getItem(i) == null) {
				list.add(new ItemStack(Material.AIR).getType().toString());
				air++;
			} else
				list.add(gui.getItem(i).getType().toString());					
		}
		
		// check that there is at least one item in the crafting matrix
		if (air >= 9) {
			player.sendMessage(ChatColor.RED+"There must be at least one item in the crafting matrix to save the recipe");
			return;
		}
			
		// if size is not 9 then something is wrong and i dont know what
		if (list.size() != 9)
			return;
		
		boolean duplicate = false;
		for (var recipe : Main.getRecipes()) {
			if (recipe.getIngredients().equals(list) && recipe.getRecipe().getResult().equals(gui.getItem(24)))
				duplicate = true;
		}
		if (duplicate) {
			player.sendMessage(ChatColor.RED+"That recipe already exists");
			return;
		}
		
		// create recipe
		Recipe recipe = new Recipe(gui.getItem(24), list, UUID.randomUUID());
		
		// save to file
		Main.getDataFile().getConfig().set(recipe.getID().toString()+".recipe", list);
		Main.getDataFile().getConfig().set(recipe.getID().toString()+".result", recipe.getRecipe().getResult().getType().toString());
		Main.getDataFile().save();
		
		// create recipe and load it
		Main.getRecipes().add(recipe);
		
		player.closeInventory();
		resetGUI();
		player.sendMessage(ChatColor.YELLOW+"Saved recipe");
		
		
	}
	
	
	public static void open(Player player) {
		player.openInventory(gui);
	}

}
