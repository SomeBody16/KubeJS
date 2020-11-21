package dev.latvian.kubejs.recipe.minecraft;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.kubejs.recipe.RecipeJS;
import dev.latvian.kubejs.util.ListJS;
import dev.latvian.kubejs.util.MapJS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class ShapedRecipeJS extends RecipeJS
{
	private final List<String> pattern = new ArrayList<>();
	private final List<String> key = new ArrayList<>();

	@Override
	public void create(ListJS args)
	{
		if (args.size() < 3)
		{
			if (args.size() < 2)
			{
				throw new RecipeExceptionJS("Shaped recipe requires 3 arguments - result, pattern and keys!");
			}

			outputItems.add(parseResultItem(args.get(0)));
			ListJS vertical = ListJS.orSelf(args.get(1));

			if (vertical.isEmpty())
			{
				throw new RecipeExceptionJS("Shaped recipe pattern is empty!");
			}

			int id = 0;

			for (Object o : vertical)
			{
				StringBuilder horizontalPattern = new StringBuilder();
				ListJS horizontal = ListJS.orSelf(o);

				for (Object item : horizontal)
				{
					IngredientJS ingredient = IngredientJS.of(item);

					if (!ingredient.isEmpty())
					{
						String currentId = String.valueOf((char) ('A' + (id++)));
						horizontalPattern.append(currentId);
						inputItems.add(ingredient);
						key.add(currentId);
					}
					else
					{
						horizontalPattern.append(" ");
					}
				}

				pattern.add(horizontalPattern.toString());
			}

			return;
		}

		outputItems.add(parseResultItem(args.get(0)));

		ListJS pattern1 = ListJS.orSelf(args.get(1));

		if (pattern1.isEmpty())
		{
			throw new RecipeExceptionJS("Shaped recipe pattern is empty!");
		}

		for (Object p : pattern1)
		{
			pattern.add(String.valueOf(p));
		}

		MapJS key1 = MapJS.of(args.get(2));

		if (key1 == null || key1.isEmpty())
		{
			throw new RecipeExceptionJS("Shaped recipe key map is empty!");
		}

		for (String k : key1.keySet())
		{
			inputItems.add(parseIngredientItem(key1.get(k), k));
			key.add(k);
		}
	}

	@Override
	public void deserialize()
	{
		outputItems.add(parseResultItem(json.get("result")));

		for (JsonElement e : json.get("pattern").getAsJsonArray())
		{
			pattern.add(e.getAsString());
		}

		if (pattern.isEmpty())
		{
			throw new RecipeExceptionJS("Shaped recipe pattern is empty!");
		}

		for (Map.Entry<String, JsonElement> entry : json.get("key").getAsJsonObject().entrySet())
		{
			inputItems.add(parseIngredientItem(entry.getValue(), entry.getKey()));
			key.add(entry.getKey());
		}

		if (key.isEmpty())
		{
			throw new RecipeExceptionJS("Shaped recipe key map is empty!");
		}
	}

	@Override
	public void serialize()
	{
		if (serializeOutputs)
		{
			json.add("result", outputItems.get(0).toResultJson());
		}

		if (serializeInputs)
		{
			JsonArray patternJson = new JsonArray();

			for (String s : pattern)
			{
				patternJson.add(s);
			}

			json.add("pattern", patternJson);

			JsonObject keyJson = new JsonObject();

			for (int i = 0; i < key.size(); i++)
			{
				keyJson.add(key.get(i), inputItems.get(i).toJson());
			}

			json.add("key", keyJson);
		}
	}
}