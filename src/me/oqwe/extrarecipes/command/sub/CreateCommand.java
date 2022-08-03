package me.oqwe.extrarecipes.command.sub;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.oqwe.extrarecipes.gui.CreateRecipeGUI;

public class CreateCommand implements Listener {

	private static final int[] blockedslots = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 14, 15, 16, 17, 18, 22, 23,
			25, 26, 27, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };
	
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

		map.get(player).open();
		
	}
	
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
			e.getWhoClicked().sendMessage(ChatColor.RED+"Recipe was not saved");
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
	
}
