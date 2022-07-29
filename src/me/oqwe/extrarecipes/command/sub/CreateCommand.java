package me.oqwe.extrarecipes.command.sub;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.oqwe.extrarecipes.gui.CreateRecipeGUI;

public class CreateCommand {

	public static void run(Player player, String[] args) {
		
		if (!player.hasPermission("extrarecipes.create")) {
			player.sendMessage(ChatColor.RED+"You do not have permission to use this command");
			return;
		}
		
		CreateRecipeGUI.open(player);
		
	}
	
}
