package me.oqwe.extrarecipes.command.sub;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.oqwe.extrarecipes.gui.ShowRecipeGUI;

public class ViewCommand implements Listener {

	// when run: create instance of gui
	// gui constructor -> create list of inventories and index
	// gui -> boolean: ignore close = false per standard (when opening new
	// inventory, set to true and reset after)
	// gui -> logic to next and previous inventory
	// gui -> close logic
	// gui -> delete logic

	// map off players in view mode and their gui instances
	private static Map<Player, ShowRecipeGUI> map = new HashMap<Player, ShowRecipeGUI>();

	public static void run(Player player, String[] args) {

		if (!player.hasPermission("extrarecipes.view")) {
			player.sendMessage(ChatColor.RED+"You do not have permission to use this command");
			return;
		}
		
		if (map.containsKey(player)) {
			map.replace(player, null);
			map.remove(player);
		}
		
		map.put(player, new ShowRecipeGUI(player));

		try {
			map.get(player).open();
		} catch (IndexOutOfBoundsException e) {
		}

	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {

		// if inventory has right title and player is in map then call correct method on
		// instance from map
		if (!e.getView().getTitle().contains("Recipe #"))
			return;
		if (!map.containsKey(e.getWhoClicked()))
			return;

		e.setCancelled(true);

		if (e.getRawSlot() == 49) {
			map.get(e.getWhoClicked()).close();
		}

		if (e.getRawSlot() == 48) {
			map.get(e.getWhoClicked()).previous();
		}

		if (e.getRawSlot() == 50) {
			map.get(e.getWhoClicked()).next();
		}

		if (e.getRawSlot() == 53 && e.getWhoClicked().hasPermission("extrarecipes.delete")) {
			map.get(e.getWhoClicked()).delete();
		}

	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {

		if (!(e.getPlayer() instanceof Player))
			return;

		Player player = (Player) e.getPlayer();

		// get instance from map and check boolean ignore close:
		// true -> do nothing
		// false -> remove from map

		if (map.containsKey(player) && !map.get(player).getIgnoreClose()) {
			map.replace(player, null);
			map.remove(player);
		}

	}

}
