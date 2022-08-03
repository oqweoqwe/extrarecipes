package me.oqwe.extrarecipes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.oqwe.extrarecipes.command.ExtraRecipes;
import me.oqwe.extrarecipes.command.sub.CreateCommand;
import me.oqwe.extrarecipes.command.sub.ViewCommand;
import me.oqwe.extrarecipes.util.DataFile;

public class Main extends JavaPlugin {

	// TODO implement support for stacked items
	
	private static Main instance;
	private static DataFile dataFile;
	private static List<Recipe> recipes = new ArrayList<Recipe>();
	
	public void onEnable() {
		
		if (!getDataFolder().exists())
			getDataFolder().mkdirs();
		
		instance = this;
		dataFile = new DataFile(this);
		recipes = dataFile.loadRecipes();
				
		registerCommands();
		registerListeners();
		
	}
	
	public void reload() {
		// reloadConfig(); -no config at this moment
		for (var recipe : recipes) {
			Bukkit.removeRecipe(recipe.getKey());
		}
		recipes = dataFile.loadRecipes();
		
	}
	
	// unload custom recipes
	public void onDisable() {
		for (var recipe : recipes)
			Bukkit.removeRecipe(recipe.getKey());
	}
	
	private void registerCommands() {
		getCommand("extrarecipes").setExecutor(new ExtraRecipes());
	}
	
	private void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new CreateCommand(), this);
		pm.registerEvents(new ViewCommand(), this);
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public static DataFile getDataFile() {
		return dataFile;
	}
	
	public static List<Recipe> getRecipes() {
		return recipes;
	}
	
}
