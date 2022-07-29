package me.oqwe.extrarecipes.command;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.oqwe.extrarecipes.command.sub.CreateCommand;
import me.oqwe.extrarecipes.command.sub.ViewCommand;
import net.md_5.bungee.api.ChatColor;

public class ExtraRecipes implements CommandExecutor {

	private Set<ViewCommand> active = new HashSet<ViewCommand>();
	
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
		
		switch (args[0]) {
		
		case "create":
			CreateCommand.run(player, args);
			return true;
		
		case "view":
			ViewCommand.run((Player) sender, args);
			return true;
		
		default:
			sender.sendMessage(ChatColor.RED+"Unknown usage, try /er help");
			break;
		
		}
			
		
		return true;
	}
	
}
