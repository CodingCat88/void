package coding.cat.voidmod.events;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class StasisRods {

    private static final String ROD_ID = "StasisRodId";

    private static final int MAX_RODS_PER_PLATE = 2;

    private static final Map<UUID, UUID> STASIS_RODS = new HashMap<>();

    private static final Map<UUID, BlockPos> ROD_PLATES = new HashMap<>();

    private static final Map<BlockPos, Set<UUID>> PLATE_RODS = new HashMap<>();

    public static void register() {

        UseItemCallback.EVENT.register((player, world, hand) -> {

            ItemStack stack = player.getStackInHand(hand);

            if (!(stack.getItem() instanceof FishingRodItem)) {
                return TypedActionResult.pass(stack);
            }

            if (world.isClient()) {
                return TypedActionResult.pass(stack);
            }

            ServerWorld serverWorld = (ServerWorld) world;

            HitResult hitResult =
                    player.raycast(5.0D, 1.0F, false);

            if (hitResult.getType() == HitResult.Type.BLOCK) {

                BlockHitResult blockHit =
                        (BlockHitResult) hitResult;

                BlockPos pos =
                        blockHit.getBlockPos().toImmutable();

                if (world.getBlockState(pos).isIn(BlockTags.PRESSURE_PLATES)) {

                    UUID rodId = getOrCreateRodId(stack);

                    BlockPos existingPlate =
                            ROD_PLATES.get(rodId);

                    /*
                     * Clicking the same pressure plate
                     * removes this rod's existing stasis.
                     */
                    if (pos.equals(existingPlate)) {

                        removeExistingStasis(
                                serverWorld,
                                rodId
                        );

                    } else {

                        /*
                         * Check the destination before removing
                         * the rod's existing stasis.
                         */
                        Set<UUID> rodsOnPlate =
                                PLATE_RODS.get(pos);

                        if (rodsOnPlate != null
                                && rodsOnPlate.size() >= MAX_RODS_PER_PLATE) {

                            if (player instanceof ServerPlayerEntity serverPlayer) {
                                serverPlayer.sendMessage(
                                        Text.literal(
                                                "This pressure plate already has two stases."
                                        ),
                                        true
                                );
                            }

                            return TypedActionResult.pass(stack);
                        }

                        removeExistingStasis(
                                serverWorld,
                                rodId
                        );
                    }

                    Set<UUID> rodsOnPlate =
                            PLATE_RODS.computeIfAbsent(
                                    pos,
                                    ignored -> new HashSet<>()
                            );

                    if (rodsOnPlate.size() >= MAX_RODS_PER_PLATE) {

                        if (player instanceof ServerPlayerEntity serverPlayer) {
                            serverPlayer.sendMessage(
                                    Text.literal(
                                            "This pressure plate already has two stases."
                                    ),
                                    true
                            );
                        }

                        return TypedActionResult.pass(stack);
                    }

                    rodsOnPlate.add(rodId);

                    ROD_PLATES.put(
                            rodId,
                            pos
                    );

                    /*
                     * Invisible armor stand acts as the
                     * physical representation of the stasis.
                     */
                    ArmorStandEntity armorStand =
                            new ArmorStandEntity(
                                    EntityType.ARMOR_STAND,
                                    serverWorld
                            );

                    armorStand.refreshPositionAndAngles(
                            pos.getX() + 0.5,
                            pos.getY(),
                            pos.getZ() + 0.5,
                            0.0F,
                            0.0F
                    );

                    armorStand.setInvisible(true);
                    armorStand.setInvulnerable(true);
                    armorStand.setNoGravity(true);

                    serverWorld.spawnEntity(armorStand);

                    STASIS_RODS.put(
                            rodId,
                            armorStand.getUuid()
                    );

                    if (player instanceof ServerPlayerEntity serverPlayer) {
                        serverPlayer.sendMessage(
                                Text.literal("Stasis created."),
                                true
                        );
                    }

                    return TypedActionResult.success(stack);
                }
            }

            /*
             * Normal fishing rod cast.
             *
             * Release this rod's existing stasis.
             */
            UUID rodId = getRodId(stack);

            if (rodId != null) {
                removeExistingStasis(
                        serverWorld,
                        rodId
                );
            }

            return TypedActionResult.pass(stack);
        });
    }

    private static UUID getOrCreateRodId(ItemStack stack) {

        NbtCompound nbt;

        NbtComponent customData =
                stack.get(DataComponentTypes.CUSTOM_DATA);

        if (customData != null) {
            nbt = customData.copyNbt();
        } else {
            nbt = new NbtCompound();
        }

        if (nbt.contains(ROD_ID)) {
            return nbt.getUuid(ROD_ID);
        }

        UUID id = UUID.randomUUID();

        nbt.putUuid(
                ROD_ID,
                id
        );

        stack.set(
                DataComponentTypes.CUSTOM_DATA,
                NbtComponent.of(nbt)
        );

        return id;
    }

    private static UUID getRodId(ItemStack stack) {

        NbtComponent customData =
                stack.get(DataComponentTypes.CUSTOM_DATA);

        if (customData == null) {
            return null;
        }

        NbtCompound nbt =
                customData.copyNbt();

        if (!nbt.contains(ROD_ID)) {
            return null;
        }

        return nbt.getUuid(ROD_ID);
    }

    private static void removeExistingStasis(
            ServerWorld world,
            UUID rodId
    ) {

        UUID armorStandId =
                STASIS_RODS.remove(rodId);

        if (armorStandId != null) {

            var entity =
                    world.getEntity(armorStandId);

            if (entity != null) {
                entity.discard();
            }
        }

        BlockPos platePos =
                ROD_PLATES.remove(rodId);

        if (platePos != null) {

            Set<UUID> rodsOnPlate =
                    PLATE_RODS.get(platePos);

            if (rodsOnPlate != null) {

                rodsOnPlate.remove(rodId);

                if (rodsOnPlate.isEmpty()) {
                    PLATE_RODS.remove(platePos);
                }
            }
        }
    }
}