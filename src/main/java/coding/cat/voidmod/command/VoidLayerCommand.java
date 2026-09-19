package coding.cat.voidmod.command;

import coding.cat.voidmod.component.VoidComponents;
import coding.cat.voidmod.component.VoidPlayerComponent;
import coding.cat.voidmod.events.OnPlayerDeath;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class VoidLayerCommand {

    public static void register() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) ->
                        register(dispatcher, registryAccess)
        );
    }

    private static void register(
            CommandDispatcher<ServerCommandSource> dispatcher,
            CommandRegistryAccess registryAccess
    ) {
        dispatcher.register(
                literal("voidlayer")
                        .requires(source -> source.hasPermissionLevel(2))

                        .then(literal("send")
                                .then(argument("player", EntityArgumentType.player())
                                        .executes(context -> {
                                            ServerPlayerEntity player =
                                                    EntityArgumentType.getPlayer(context, "player");

                                            VoidPlayerComponent component =
                                                    (VoidPlayerComponent) player.getComponent(
                                                            VoidComponents.PLAYER
                                                    );

                                            component.inLayer = true;
                                            VoidComponents.PLAYER.sync(player);

                                            context.getSource().sendFeedback(
                                                    () -> net.minecraft.text.Text.literal(
                                                            "Sent " + player.getName().getString()
                                                                    + " into the Void layer."
                                                    ),
                                                    true
                                            );

                                            return 1;
                                        })
                                )
                        )

                        .then(literal("pull")
                                .then(argument("player", EntityArgumentType.player())
                                        .executes(context -> {
                                            ServerPlayerEntity player =
                                                    EntityArgumentType.getPlayer(context, "player");

                                            VoidPlayerComponent component =
                                                    (VoidPlayerComponent) player.getComponent(
                                                            VoidComponents.PLAYER
                                                    );

                                            component.inLayer = false;
                                            VoidComponents.PLAYER.sync(player);

                                            OnPlayerDeath.removeCountdown(player.getUuid());

                                            context.getSource().sendFeedback(
                                                    () -> net.minecraft.text.Text.literal(
                                                            "Pulled " + player.getName().getString()
                                                                    + " out of the Void layer."
                                                    ),
                                                    true
                                            );

                                            return 1;
                                        })
                                )
                        )
        );
    }
}