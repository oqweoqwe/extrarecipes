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

	// players and the gui instance with which they are associated
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

		if (map.containsKey(player) && !map.get(player).getIgnoreClose()) {
			map.replace(player, null);
			map.remove(player);
		}

	}

}
