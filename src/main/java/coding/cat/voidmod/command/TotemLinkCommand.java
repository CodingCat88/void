package coding.cat.voidmod.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class TotemLinkCommand {

    private static final String LINK_ID = "TotemLinkId";
    private static final String USE_LINK = "TotemUseLink";

    private static final Gson GSON =
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

    private static final Path CONFIG_DIRECTORY =
            FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve("voidmod");

    private static final Path CONFIG_FILE =
            CONFIG_DIRECTORY.resolve("totem-links.json");

    private static final Map<String, ContainerLocation> LINKED_CONTAINERS =
            new HashMap<>();

    public static void register(
            CommandDispatcher<ServerCommandSource> dispatcher
    ) {

        loadConfig();

        dispatcher.register(
                CommandManager.literal("totem")
                        .then(
                                CommandManager.literal("link")
                                        .then(
                                                CommandManager.argument(
                                                                "linkId",
                                                                StringArgumentType.word()
                                                        )
                                                        .executes(context -> {

                                                            String linkId =
                                                                    StringArgumentType.getString(
                                                                            context,
                                                                            "linkId"
                                                                    );

                                                            return linkTotem(
                                                                    context.getSource(),
                                                                    linkId,
                                                                    false
                                                            );
                                                        })
                                                        .then(
                                                                CommandManager.literal("use")
                                                                        .executes(context -> {

                                                                            String linkId =
                                                                                    StringArgumentType.getString(
                                                                                            context,
                                                                                            "linkId"
                                                                                    );

                                                                            return linkTotem(
                                                                                    context.getSource(),
                                                                                    linkId,
                                                                                    true
                                                                            );
                                                                        })
                                                        )
                                        )
                        )
        );
    }

    private static int linkTotem(
            ServerCommandSource source,
            String linkId,
            boolean use
    ) {

        if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
            return 0;
        }

        ItemStack heldTotem =
                player.getMainHandStack();

        if (!heldTotem.isOf(Items.TOTEM_OF_UNDYING)) {

            player.sendMessage(
                    Text.literal("nope"),
                    false
            );

            return 0;
        }

        /*
         * /totem link <id>
         *
         * Assign the ID to the held totem.
         */
        if (!use) {

            setLinkId(
                    heldTotem,
                    linkId
            );

            setUseLink(
                    heldTotem,
                    false
            );

            player.sendMessage(
                    Text.literal("oki"),
                    false
            );

            return 1;
        }

        /*
         * /totem link <id> use
         *
         * Find the container being looked at.
         */
        BlockHitResult hitResult =
                getTargetedBlock(player);

        if (hitResult == null) {

            player.sendMessage(
                    Text.literal("nope"),
                    false
            );

            return 0;
        }

        BlockPos containerPos =
                hitResult.getBlockPos();

        ServerWorld world =
                player.getServerWorld();

        BlockEntity blockEntity =
                world.getBlockEntity(containerPos);

        if (!(blockEntity instanceof Inventory inventory)) {

            player.sendMessage(
                    Text.literal("nope"),
                    false
            );

            return 0;
        }

        /*
         * Find the matching linked totem.
         */
        boolean foundMatchingTotem = false;

        for (int slot = 0; slot < inventory.size(); slot++) {

            ItemStack stack =
                    inventory.getStack(slot);

            if (!stack.isOf(Items.TOTEM_OF_UNDYING)) {
                continue;
            }

            String storedLinkId =
                    getLinkId(stack);

            if (linkId.equals(storedLinkId)) {

                foundMatchingTotem = true;
                break;
            }
        }

        if (!foundMatchingTotem) {

            player.sendMessage(
                    Text.literal("nope"),
                    false
            );

            return 0;
        }

        /*
         * Mark the held totem as the trigger totem.
         */
        setLinkId(
                heldTotem,
                linkId
        );

        setUseLink(
                heldTotem,
                true
        );

        /*
         * Save the container permanently.
         */
        LINKED_CONTAINERS.put(
                linkId,
                new ContainerLocation(
                        world.getRegistryKey()
                                .getValue()
                                .toString(),
                        containerPos
                )
        );

        saveConfig();

        player.sendMessage(
                Text.literal("oki"),
                false
        );

        return 1;
    }

    private static BlockHitResult getTargetedBlock(
            ServerPlayerEntity player
    ) {

        HitResult hitResult =
                player.raycast(
                        5.0D,
                        1.0F,
                        false
                );

        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return null;
        }

        return (BlockHitResult) hitResult;
    }

    private static void setLinkId(
            ItemStack stack,
            String linkId
    ) {

        NbtCompound nbt =
                getCustomData(stack);

        nbt.putString(
                LINK_ID,
                linkId
        );

        setCustomData(
                stack,
                nbt
        );
    }

    private static String getLinkId(
            ItemStack stack
    ) {

        NbtComponent customData =
                stack.get(DataComponentTypes.CUSTOM_DATA);

        if (customData == null) {
            return null;
        }

        NbtCompound nbt =
                customData.copyNbt();

        if (!nbt.contains(LINK_ID)) {
            return null;
        }

        return nbt.getString(LINK_ID);
    }

    private static void setUseLink(
            ItemStack stack,
            boolean use
    ) {

        NbtCompound nbt =
                getCustomData(stack);

        nbt.putBoolean(
                USE_LINK,
                use
        );

        setCustomData(
                stack,
                nbt
        );
    }

    public static boolean isUseLink(
            ItemStack stack
    ) {

        NbtComponent customData =
                stack.get(DataComponentTypes.CUSTOM_DATA);

        if (customData == null) {
            return false;
        }

        return customData
                .copyNbt()
                .getBoolean(USE_LINK);
    }

    public static String getStoredLinkId(
            ItemStack stack
    ) {

        return getLinkId(stack);
    }

    public static ContainerLocation getLinkedContainer(
            String linkId
    ) {

        return LINKED_CONTAINERS.get(linkId);
    }

    public static boolean consumeLinkedTotem(
            ServerPlayerEntity player,
            String linkId
    ) {

        ContainerLocation location =
                LINKED_CONTAINERS.get(linkId);

        if (location == null) {
            return false;
        }

        ServerWorld targetWorld =
                getWorld(
                        player,
                        location.dimension()
                );

        if (targetWorld == null) {
            return false;
        }

        BlockPos pos =
                location.position();

        targetWorld.getChunk(
                pos.getX() >> 4,
                pos.getZ() >> 4
        );

        BlockEntity blockEntity =
                targetWorld.getBlockEntity(pos);

        if (!(blockEntity instanceof Inventory inventory)) {
            return false;
        }

        for (int slot = 0; slot < inventory.size(); slot++) {

            ItemStack stack =
                    inventory.getStack(slot);

            if (!stack.isOf(Items.TOTEM_OF_UNDYING)) {
                continue;
            }

            String storedLinkId =
                    getLinkId(stack);

            if (!linkId.equals(storedLinkId)) {
                continue;
            }

            /*
             * The stored totem is the one that gets consumed.
             */
            stack.decrement(1);

            inventory.markDirty();

            /*
             * Remove the one-use link.
             */
            LINKED_CONTAINERS.remove(linkId);

            saveConfig();

            return true;
        }

        return false;
    }

    private static ServerWorld getWorld(
            ServerPlayerEntity player,
            String dimension
    ) {

        Identifier identifier =
                Identifier.tryParse(dimension);

        if (identifier == null) {
            return null;
        }

        RegistryKey<World> worldKey =
                RegistryKey.of(
                        RegistryKeys.WORLD,
                        identifier
                );

        return player
                .getServer()
                .getWorld(worldKey);
    }

    private static NbtCompound getCustomData(
            ItemStack stack
    ) {

        NbtComponent customData =
                stack.get(DataComponentTypes.CUSTOM_DATA);

        if (customData != null) {
            return customData.copyNbt();
        }

        return new NbtCompound();
    }

    private static void setCustomData(
            ItemStack stack,
            NbtCompound nbt
    ) {

        stack.set(
                DataComponentTypes.CUSTOM_DATA,
                NbtComponent.of(nbt)
        );
    }

    private static void loadConfig() {

        LINKED_CONTAINERS.clear();

        if (!Files.exists(CONFIG_FILE)) {
            return;
        }

        try (Reader reader =
                     Files.newBufferedReader(CONFIG_FILE)) {

            JsonObject root =
                    GSON.fromJson(
                            reader,
                            JsonObject.class
                    );

            if (root == null) {
                return;
            }

            for (String linkId : root.keySet()) {

                JsonObject object =
                        root.getAsJsonObject(linkId);

                String dimension =
                        object.get("dimension")
                                .getAsString();

                int x =
                        object.get("x")
                                .getAsInt();

                int y =
                        object.get("y")
                                .getAsInt();

                int z =
                        object.get("z")
                                .getAsInt();

                LINKED_CONTAINERS.put(
                        linkId,
                        new ContainerLocation(
                                dimension,
                                new BlockPos(
                                        x,
                                        y,
                                        z
                                )
                        )
                );
            }

        } catch (Exception ignored) {
        }
    }

    private static void saveConfig() {

        try {

            Files.createDirectories(
                    CONFIG_DIRECTORY
            );

            JsonObject root =
                    new JsonObject();

            for (Map.Entry<String, ContainerLocation> entry :
                    LINKED_CONTAINERS.entrySet()) {

                ContainerLocation location =
                        entry.getValue();

                JsonObject object =
                        new JsonObject();

                object.addProperty(
                        "dimension",
                        location.dimension()
                );

                object.addProperty(
                        "x",
                        location.position().getX()
                );

                object.addProperty(
                        "y",
                        location.position().getY()
                );

                object.addProperty(
                        "z",
                        location.position().getZ()
                );

                root.add(
                        entry.getKey(),
                        object
                );
            }

            try (Writer writer =
                         Files.newBufferedWriter(CONFIG_FILE)) {

                GSON.toJson(
                        root,
                        writer
                );
            }

        } catch (IOException ignored) {
        }
    }

    public record ContainerLocation(
            String dimension,
            BlockPos position
    ) {
    }
}