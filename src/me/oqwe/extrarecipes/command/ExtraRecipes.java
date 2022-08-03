package me.oqwe.extrarecipes.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.oqwe.extrarecipes.Main;
import me.oqwe.extrarecipes.command.sub.CreateCommand;
import me.oqwe.extrarecipes.command.sub.ViewCommand;

public class ExtraRecipes implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED+"You must be a player to use this command");
			return true;
		}
		
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED+"Unknown usage, try /er help");
			return true;
		}
		
		Player player = (Player) sender;
		
		// switch to select subcommand
		switch (args[0]) {
		
		case "create":
			CreateCommand.run(player, args);
			return true;
		
		case "view":
			ViewCommand.run((Player) sender, args);
			return true;
			
		case "help":
			
			if (!sender.hasPermission("extrarecipes.help")) {
				sender.sendMessage(ChatColor.RED+"You do not have permission to use this command");
				return true;
			}
			
			sender.sendMessage(cc("&6ExtraRecipes &eversion "+ Main.getInstance().getDescription().getVersion() + " by &6oqwe"));
			sender.sendMessage(cc("&7Create a new custom recipe"));
			sender.sendMessage(cc("&7- &6/er create"));
			sender.sendMessage(cc("&7See all existing custom recipes"));
			sender.sendMessage(cc("&7- &6/er view"));
			sender.sendMessage(cc("&7Delete a custom recipe"));
			sender.sendMessage(cc("&7- &6/er view &7- if you have the correct permission, a button to delete recipes will appear in the menu"));
			return true;
		
		default:
			sender.sendMessage(ChatColor.RED+"Unknown usage, try /er help");
			return true;
		
		}
			
	}

	private String cc(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
}
