package dev.latvian.kubejs.item.ingredient;

import dev.latvian.kubejs.item.BoundItemStackJS;
import dev.latvian.kubejs.item.ItemStackJS;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author LatvianModder
 */
public class OreDictionaryIngredientJS implements IngredientJS
{
	public final String oreName;
	public final int oreID;

	public OreDictionaryIngredientJS(String ore)
	{
		oreName = ore;
		oreID = OreDictionary.getOreID(oreName);
	}

	@Override
	public boolean test(ItemStackJS stack)
	{
		if (stack.isEmpty())
		{
			return false;
		}

		for (int id : OreDictionary.getOreIDs(stack.itemStack()))
		{
			if (oreID == id)
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public Set<ItemStackJS> getStacks()
	{
		Set<ItemStackJS> set = new LinkedHashSet<>();

		for (ItemStack stack : OreDictionary.getOres(oreName))
		{
			if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE)
			{
				NonNullList<ItemStack> list = NonNullList.create();
				stack.getItem().getSubItems(CreativeTabs.SEARCH, list);

				for (ItemStack stack1 : list)
				{
					set.add(new BoundItemStackJS(stack1));
				}
			}
			else
			{
				set.add(new BoundItemStackJS(stack.copy()).count(1));
			}
		}

		return set;
	}

	@Override
	public Ingredient createVanillaIngredient()
	{
		return new OreIngredient(oreName);
	}
}