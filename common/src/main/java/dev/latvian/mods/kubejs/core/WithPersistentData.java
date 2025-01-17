package dev.latvian.mods.kubejs.core;

import dev.latvian.mods.rhino.util.RemapForJS;
import net.minecraft.nbt.CompoundTag;

public interface WithPersistentData {
	@RemapForJS("getPersistentData")
	default CompoundTag kjs$getPersistentData() {
		throw new NoMixinException();
	}
}
