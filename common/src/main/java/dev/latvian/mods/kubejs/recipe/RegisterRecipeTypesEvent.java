package dev.latvian.mods.kubejs.recipe;

import dev.architectury.registry.registries.Registries;
import dev.latvian.mods.kubejs.DevProperties;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import dev.latvian.mods.kubejs.recipe.minecraft.ShapedRecipeJS;
import dev.latvian.mods.kubejs.recipe.minecraft.ShapelessRecipeJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public record RegisterRecipeTypesEvent(Map<ResourceLocation, RecipeTypeJS> map) {
	public void register(RecipeTypeJS type) {
		map.put(Registries.getId(type.serializer, Registry.RECIPE_SERIALIZER_REGISTRY), type);
		KubeJS.LOGGER.info("Registered custom recipe handler for type " + type);
	}

	public void register(ResourceLocation id, Supplier<RecipeJS> f) {
		try {
			register(new RecipeTypeJS(Objects.requireNonNull(KubeJSRegistries.recipeSerializers().get(id)), f));
		} catch (NullPointerException e) {
			if (DevProperties.get().logErroringRecipes) {
				ConsoleJS.SERVER.warn("Failed to register handler for recipe type " + id + " as it doesn't exist!");
			}
		}
	}

	public void ignore(ResourceLocation id) {
		try {
			register(new IgnoredRecipeTypeJS(Objects.requireNonNull(KubeJSRegistries.recipeSerializers().get(id))));
		} catch (NullPointerException e) {
			if (DevProperties.get().logErroringRecipes) {
				ConsoleJS.SERVER.warn("Failed to ignore recipe type " + id + " as it doesn't exist!");
			}
		}
	}

	public void registerShaped(ResourceLocation id) {
		register(id, ShapedRecipeJS::new);
	}

	public void registerShapeless(ResourceLocation id) {
		register(id, ShapelessRecipeJS::new);
	}

	private void handleMissingSerializer(ResourceLocation id) {
		if (DevProperties.get().logInvalidRecipeHandlers) {
			throw new NullPointerException("Cannot find recipe serializer: " + id);
		} else if (DevProperties.get().logErroringRecipes) {
			KubeJS.LOGGER.warn("Skipping recipe handler for serializer " + id + " as it does not exist!");
		}
	}
}