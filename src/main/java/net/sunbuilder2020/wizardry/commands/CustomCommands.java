package net.sunbuilder2020.wizardry.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sunbuilder2020.wizardry.spells.playerData.PlayerSpellsProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber
public class CustomCommands {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("spells")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("add")
                                .then(Commands.argument("playerNames", EntityArgument.players())
                                        .then(Commands.argument("spell", StringArgumentType.word())
                                                .executes(context -> executeAddSpell(context,
                                                        EntityArgument.getPlayers(context, "playerNames"),
                                                        StringArgumentType.getString(context, "spell")))))
                        ).then(Commands.literal("remove")
                                .then(Commands.argument("playerNames", EntityArgument.players())
                                        .then(Commands.argument("spell", StringArgumentType.word())
                                                .executes(context -> executeRemoveSpell(context,
                                                        EntityArgument.getPlayers(context, "playerNames"),
                                                        StringArgumentType.getString(context, "spell")))))
                        ).then(Commands.literal("get")
                                .then(Commands.argument("playerNames", EntityArgument.player())
                                        .executes(context -> executeGetSpells(context,
                                                EntityArgument.getPlayer(context, "playerNames"))))
                        ).then(Commands.literal("clear")
                                .then(Commands.argument("playerNames", EntityArgument.player())
                                        .executes(context -> executeClearSpells(context,
                                                EntityArgument.getPlayer(context, "playerNames"))))
                        ).then(Commands.literal("set")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("spells", StringArgumentType.greedyString())
                                                .executes(context -> executeSetSpells(context,
                                                        EntityArgument.getPlayer(context, "player"),
                                                        StringArgumentType.getString(context, "spells"))
                                                )))
                        )
        );
    }

    private static int executeAddSpell(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> targets, String spellID) {
        AtomicInteger successCount = new AtomicInteger();


        targets.forEach(target -> target.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(spells -> {
            spells.addSpell(spellID);
            successCount.getAndIncrement();
        }));

        if (successCount.get() > 0) {
            context.getSource().sendSuccess(() -> Component.translatable("text.wizardry.commands.add_spells.success", successCount.get()), true);
        } else {
            context.getSource().sendFailure(Component.translatable("text.wizardry.commands.add_spells.failure"));
        }

        return successCount.get();
    }

    private static int executeRemoveSpell(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> targets, String spell) {
        AtomicInteger successCount = new AtomicInteger();

        targets.forEach(target -> target.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(spells -> {
            spells.removeSpell(spell);
            successCount.getAndIncrement();
        }));

        if (successCount.get() > 0) {
            context.getSource().sendSuccess(() -> Component.translatable("text.wizardry.commands.remove_spells.success", successCount.get()), true);
        } else {
            context.getSource().sendFailure(Component.translatable("text.wizardry.commands.remove_spells.failure"));
        }

        return successCount.get();
    }

    private static int executeGetSpells(CommandContext<CommandSourceStack> context, ServerPlayer target) {
        AtomicReference<List<String>> playerSpells = new AtomicReference<>(new ArrayList<>());

        target.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(spells -> {
            playerSpells.set(spells.getSpells());
        });

        if (!playerSpells.get().isEmpty()) {
            context.getSource().sendSuccess(() ->
                    Component.translatable("text.wizardry.commands.get_spells.success", target.getDisplayName().getString())
                            .append(String.join(", ", playerSpells.get())), true);

            return 1;
        } else {
            context.getSource().sendFailure(Component.translatable("text.wizardry.commands.get_spells.failure", target.getDisplayName().getString()));

            return 0;
        }
    }

    private static int executeSetSpells(CommandContext<CommandSourceStack> context, ServerPlayer target, String spellsAsString) {
        List<String> spells = Arrays.asList(spellsAsString.split(" "));

        AtomicBoolean succeeded = new AtomicBoolean(false);

        target.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(playerSpells -> {
            playerSpells.setSpells(spells);

            succeeded.set(true);
        });

        if (succeeded.get()) {
            context.getSource().sendSuccess(() -> Component.translatable("text.wizardry.commands.set_spells.success",
                    target.getDisplayName().getString()), true);

            return 1;
        } else {
            context.getSource().sendFailure(Component.translatable("text.wizardry.commands.set_spells.failure"));

            return 0;
        }
    }

    private static int executeClearSpells(CommandContext<CommandSourceStack> context, ServerPlayer target) {
        AtomicBoolean succeeded = new AtomicBoolean(false);

        target.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(playerSpells -> {
            playerSpells.setSpells(new ArrayList<>());

            succeeded.set(true);
        });

        if (succeeded.get()) {
            context.getSource().sendSuccess(() -> Component.translatable("text.wizardry.commands.clear_spells.success",
                    target.getDisplayName().getString()), true);

            return 1;
        } else {
            context.getSource().sendFailure(Component.translatable("text.wizardry.commands.clear_spells.failure"));

            return 0;
        }
    }
}
