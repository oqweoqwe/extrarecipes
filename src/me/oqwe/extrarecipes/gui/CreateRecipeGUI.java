package me.oqwe.extrarecipes.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.oqwe.extrarecipes.Main;
import me.oqwe.extrarecipes.Recipe;
import me.oqwe.extrarecipes.util.StackUtil;

public class CreateRecipeGUI implements Listener {

	// 49 is close button, 33 is save button, 24 is result
	
	private Inventory gui;
	private final int[] allowedslots = new int[] { 10, 11, 12, 19, 20, 21, 24, 28, 29, 30 };
	private final int[] craftingmatrix = new int[] { 10, 11, 12, 19, 20, 21, 28, 29, 30 };
	private boolean ignoreClose = false;
	
	private final Player player;
	
	public CreateRecipeGUI(Player player) {
		this.player = player;
		gui = Bukkit.createInventory(null, 54, "Create new recipe");
		
		resetGUI();
		
		open();
	}
	
	public void open() {
		player.openInventory(gui);
	}

	/**
	 * Reset the create recipe inventory
	 */
	public void resetGUI() {
		
		ItemStack pane = StackUtil.stack(Material.BLACK_STAINED_GLASS_PANE, " ");

		// fill with panes
		for (int i = 0; i < 54; i++)
			gui.setItem(i, pane);

		// set allowed slots as air
		for (var i : allowedslots)
			gui.setItem(i, new ItemStack(Material.AIR));

		ItemStack close = StackUtil.stack(Material.BARRIER, ChatColor.RED+"Close");

		gui.setItem(49, close);

		ItemStack craft = StackUtil.stack(Material.CRAFTING_TABLE, ChatColor.YELLOW+"Save recipe");

		gui.setItem(33, craft);

	}
	
	/**
	 * Close inventory
	 */
	public void close() {
		ignoreClose = true;
		player.closeInventory();
	}
	
	/**
	 * Saves the recipe that the player has created, including checks for npe and so on
	 * 
	 * @param player The player that is creating a recipe
	 */
	public void saveRecipe() {
		
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
		
		// check that recipe doesn't alredy exist
		boolean duplicate = false;
		for (var recipe : Main.getRecipes()) {
			if (recipe.getIngredients().equals(list) && recipe.getRecipe().getResult().equals(gui.getItem(24)))
				duplicate = true;
		}
		if (duplicate) {
			player.sendMessage(ChatColor.RED+"That recipe already exists");
			return;
		}
		
		// create a new recipe object, the constructor adds the recipe to bukkit
		Recipe recipe = new Recipe(gui.getItem(24), list, UUID.randomUUID());
		
		// save to file
		Main.getDataFile().getConfig().set(recipe.getID().toString()+".recipe", list);
		Main.getDataFile().getConfig().set(recipe.getID().toString()+".result", recipe.getRecipe().getResult().getType().toString());
		Main.getDataFile().save();
		
		// store recipe object in list
		Main.getRecipes().add(recipe);
		
		close();
		resetGUI();
		player.sendMessage(ChatColor.YELLOW+"Saved recipe");
		
		
	}

	public boolean getIgnoreClose() {
		return ignoreClose;
	}
	
}
