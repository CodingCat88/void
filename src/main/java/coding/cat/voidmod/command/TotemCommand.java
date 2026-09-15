package coding.cat.voidmod.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class TotemCommand {

    public static void register() {

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {

                    TotemLinkCommand.register(dispatcher);

                }
        );
    }
}