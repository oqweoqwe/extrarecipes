package me.oqwe.extrarecipes.command.sub;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import me.oqwe.extrarecipes.gui.CreateRecipeGUI;

public class CreateCommand implements Listener {

	private static final int[] blockedslots = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 14, 15, 16, 17, 18, 22, 23,
			25, 26, 27, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };
	private static final int[] allowedslots = new int[] { 10, 11, 12, 19, 20, 21, 24, 28, 29, 30 };
	
	// players and the gui instance with which they are associated
	private static Map<Player, CreateRecipeGUI> map = new HashMap<Player, CreateRecipeGUI>();
	
	public static void run(Player player, String[] args) {
		
		if (!player.hasPermission("extrarecipes.create")) {
			player.sendMessage(ChatColor.RED+"You do not have permission to use this command");
			return;
		}
		
		if (map.containsKey(player)) {
			map.replace(player, null);
			map.remove(player);
		}
		
		map.put(player, new CreateRecipeGUI(player));
		
	}
	
	// handle inventory interactions
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		
		if (!(e.getWhoClicked() instanceof Player)) 
			return;
		
		if (!e.getView().getTitle().equals("Create new recipe"))
			return;
		
		if (e.getRawSlot() == 49) {
			// close
			e.getWhoClicked().closeInventory();
			map.get(e.getWhoClicked()).close();
		}
		
		if (e.getRawSlot() == 33) {
			
			map.get(e.getWhoClicked()).saveRecipe();
			
		}

		boolean cancel = false;

		for (var i : blockedslots) {
			if (e.getRawSlot() == i)
				cancel = true;
		}

		e.setCancelled(cancel);

	}
	
	// give back items if a recipe is not saved
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		
		if (!e.getView().getTitle().equals("Create new recipe"))
			return;
		
		if (map.get(e.getPlayer()).getIgnoreClose())
			return;
		
		for (var i : allowedslots) {
			
			// if not air
			if (e.getInventory().getItem(i) != null && !e.getInventory().getItem(i).isSimilar(new ItemStack(Material.AIR))) {
				if (e.getInventory().firstEmpty() == -1) {
					// inventory full
					Bukkit.getServer().getWorld(e.getPlayer().getWorld().getName()).dropItem(e.getPlayer().getLocation(), e.getInventory().getItem(i));
				}
				e.getPlayer().getInventory().addItem(e.getInventory().getItem(i));
			}
			
		}
		
		e.getPlayer().sendMessage(ChatColor.RED+"Recipe not saved");
		
	}
	
}
