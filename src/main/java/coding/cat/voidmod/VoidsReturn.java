package coding.cat.voidmod;

import coding.cat.voidmod.block.VoidBlocks;
import coding.cat.voidmod.chat.LayerChatHandler;
import coding.cat.voidmod.command.DebugCommand;
import coding.cat.voidmod.command.VoidLayerCommand;
import coding.cat.voidmod.events.OnPlayerDeath;
import coding.cat.voidmod.events.VoidLayerRestrictions;
import coding.cat.voidmod.item.VoidItems;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoidsReturn implements ModInitializer {
	public static final String MOD_ID = "void";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Did you hear the word?");
		VoidItems.hi();
		ServerPlayerEvents.ALLOW_DEATH.register(new OnPlayerDeath());
		OnPlayerDeath.register();
		VoidLayerRestrictions.register();
		VoidBlocks.registerVoidBlocks();
		DebugCommand.register();
		LayerChatHandler.register();
		VoidLayerCommand.register();
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
