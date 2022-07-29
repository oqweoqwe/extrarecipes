package me.oqwe.extrarecipes.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import me.oqwe.extrarecipes.Main;
import me.oqwe.extrarecipes.Recipe;

public class DataFile {

	protected Main main;
	private File file;
	private FileConfiguration config;

	public DataFile(Main main) {

		this.main = main;

		file = new File(main.getDataFolder(), "recipes.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();

				// set file configuration as default and save
				config = getDefaultConfig();
				save();
				config = YamlConfiguration.loadConfiguration(file);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		config = YamlConfiguration.loadConfiguration(file);

	}
	
	public List<Recipe> loadRecipes() {
		List<Recipe> list = new ArrayList<Recipe>();
		
		for (String s : config.getKeys(false)) {
			list.add(new Recipe(new ItemStack(Material.getMaterial(config.getString(s+".result"))), config.getStringList(s+".recipe"), UUID.fromString(s)));
		}		
		
		return list;
	}

	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(String id) {
		try {
			config.set(id, null);
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void resetFile() {
		config = getDefaultConfig();
		save();
		config = YamlConfiguration.loadConfiguration(file);
	}

	private FileConfiguration getDefaultConfig() {
		FileConfiguration config = new YamlConfiguration();

		List<String> recipe = new ArrayList<String>();
		recipe.add("GOLD_BLOCK");
		recipe.add("GOLD_BLOCK");
		recipe.add("GOLD_BLOCK");
		recipe.add("GOLD_BLOCK");
		recipe.add("APPLE");
		recipe.add("GOLD_BLOCK");
		recipe.add("GOLD_BLOCK");
		recipe.add("GOLD_BLOCK");
		recipe.add("GOLD_BLOCK");
		
		UUID id = UUID.randomUUID();
		
		config.set(id.toString()+".recipe", recipe);
		config.set(id.toString()+".result", "ENCHANTED_GOLDEN_APPLE");
		

		return config;
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public File getFile() {
		return file;
	}
	
}
