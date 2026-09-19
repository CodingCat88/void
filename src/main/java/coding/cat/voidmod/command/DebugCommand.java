package coding.cat.voidmod.command;

import coding.cat.voidmod.component.VoidComponents;
import coding.cat.voidmod.component.VoidPlayerComponent;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public final class DebugCommand {

    private static final int PERMISSION_LEVEL = 2;

    private DebugCommand() {
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                registerCommands(dispatcher));
    }

    private static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("debugshader")
                .requires(source -> source.hasPermissionLevel(PERMISSION_LEVEL))
                .then(CommandManager.literal("on")
                        .executes(context -> setLayerState(context, true, context.getSource().getPlayerOrThrow()))
                        .then(CommandManager.argument("target", EntityArgumentType.player())
                                .executes(context -> setLayerState(context, true, EntityArgumentType.getPlayer(context, "target")))))
                .then(CommandManager.literal("off")
                        .executes(context -> setLayerState(context, false, context.getSource().getPlayerOrThrow()))
                        .then(CommandManager.argument("target", EntityArgumentType.player())
                                .executes(context -> setLayerState(context, false, EntityArgumentType.getPlayer(context, "target"))))));
    }

    private static int setLayerState(CommandContext<ServerCommandSource> context, boolean inLayer, ServerPlayerEntity target) {
        VoidPlayerComponent component = (VoidPlayerComponent) target.getComponent(VoidComponents.PLAYER);
        component.inLayer = inLayer;
        VoidComponents.PLAYER.sync(target);
        return 1;
    }
}
