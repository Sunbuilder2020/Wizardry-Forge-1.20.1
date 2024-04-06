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
import net.sunbuilder2020.wizardry.spells.PlayerSpellsProvider;

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
                        ).then(Commands.literal("set")
                                .then(Commands.argument("playerNames", EntityArgument.players())
                                        .then(Commands.argument("spell", StringArgumentType.word())
                                                .executes(context -> executeAddSpell(context,
                                                        EntityArgument.getPlayers(context, "playerNames"),
                                                        StringArgumentType.getString(context, "spell")))))
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

    private static int executeAddSpell(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> targets, String spell) {
        AtomicInteger successCount = new AtomicInteger();

        targets.forEach(target -> target.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(spells -> {
            spells.addSpell(spell);
            successCount.getAndIncrement();
        }));

        if (successCount.get() > 0) {
            context.getSource().sendSuccess(() -> Component.literal("Successfully added the '" + spell + "' spell to " + successCount.get() + " players."), true);
        } else {
            context.getSource().sendFailure(Component.literal("Failed to add the spell. Make sure the players are valid and try again."));
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
            context.getSource().sendSuccess(() -> Component.literal("Successfully removed the '" + spell + "' spell from " + successCount.get() + " players."), true);
        } else {
            context.getSource().sendFailure(Component.literal("Failed to add the spell. Make sure the players are valid and try again."));
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
                    Component.literal(target.getDisplayName().getString() + " has the Spells: " + String.join(", ", playerSpells.get())),
                    true);

            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("Failed to get Spells. " + target.getDisplayName().getString() + " probably has no Spells."));

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
            context.getSource().sendSuccess(() -> Component.literal("Successfully set " + target.getDisplayName().getString() + "'s spells."), true);

            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("Failed to set Spells. " + "Make sure " + target.getDisplayName().getString() + " is valid and try again."));

            return 0;
        }
    }
}
