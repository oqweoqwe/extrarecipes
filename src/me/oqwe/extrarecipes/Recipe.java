package me.oqwe.extrarecipes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class Recipe {

	private NamespacedKey key;
	private ShapedRecipe recipe;
	private UUID id;
	private boolean loadSucces = true;
	private ItemStack result;
	private List<String> ingredients;
	
	/**
	 * Create an object for storing data for each recipe
	 * 
	 * @param result The itemstack that is the recipes result
	 * @param ingredients A list of strings that represent the ingredients in l-to-r, top-to-bottom order
	 * @param id Randomly generated UUID of this recipe
	 */
	public Recipe(ItemStack result, List<String> ingredients, UUID id) {
	
		this.result = result;
		this.ingredients = ingredients;
		this.id = id;
		this.key = new NamespacedKey(Main.getInstance(), id.toString());
		
		this.recipe = getRecipeFromList(ingredients);
		
		Bukkit.addRecipe(recipe);
				
	}
	
	/**
	 * Creates the shaped recipe to be added to bukkit
	 * 
	 * @param list A list of strings that represent the ingredients in l-to-r, top-to-bottom order
	 * @return A shaped recipe to be added to bukkit
	 */
	private ShapedRecipe getRecipeFromList(List<String> list) {
		ShapedRecipe recipe = new ShapedRecipe(key, result);

		// iterate over list and assign a unique integer to each material, save this to a map
		Map<Material, Integer> letters = new HashMap<Material, Integer>();
		for (var s : list) {
			Material mat = Material.getMaterial(s);
			if (mat == null && s != "") {
				Bukkit.getLogger().warning("Error when loading a recipe into memory, couldn't find the material "+s);
				loadSucces = false;
				return null;
			}
			if (!letters.containsKey(mat)) {
				// material is not in list, find first available letter
				if (letters.isEmpty()) 
					letters.put(mat, 0);
				else {
					
					for (int i = 0; i<10;i++) {
						if (!letters.containsValue(i)) {
							letters.put(mat, i);
							break;
						}
					}
					
				}
			}
		}
		
		// iterate over list and create a string representing the shape
		StringBuilder sb = new StringBuilder();
		for (var s : list) {
			Material mat = Material.getMaterial(s);
			if (s.equalsIgnoreCase(""))
				sb.append(" ");
			else
				sb.append(letters.get(mat));
		}
		if (sb.toString().length() != 9) {
			Bukkit.getLogger().warning("Error when loading recipe  into memory, there should be exactly 9 elements in the recipe list in the recipes.yml.");
			loadSucces = false;
			return null;
		}
		// create shape from the stringbuilder
		recipe.shape(sb.toString().substring(0, 3),sb.toString().substring(3, 6),sb.toString().substring(6, 9));
		
		// set ingredients, checking if any keys are null representing no material in that slot
		for (var entry : letters.entrySet()) {
			if (entry.getKey() != null)
				recipe.setIngredient(Integer.toString(entry.getValue()).charAt(0), entry.getKey());
		}
		
		return recipe;
	}

	public boolean getLoadSucces() {
		return loadSucces;
	}
	
	public ShapedRecipe getRecipe() {
		return recipe;
	}
	
	public NamespacedKey getKey() {
		return key;
	}
	
	public UUID getID() {
		return id;
	}
	
	public List<String> getIngredients() {
		return ingredients;
	}
	
}
