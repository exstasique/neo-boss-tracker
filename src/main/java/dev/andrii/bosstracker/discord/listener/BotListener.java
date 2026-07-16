package dev.andrii.bosstracker.discord.listener;

import dev.andrii.bosstracker.discord.handler.BossKillHandler;
import dev.andrii.bosstracker.discord.handler.ManualKillHandler;
import dev.andrii.bosstracker.discord.handler.StatusMessageHandler;
import dev.andrii.bosstracker.model.BossChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotListener extends ListenerAdapter {

    private final BossKillHandler bossKillHandler;
    private final ManualKillHandler manualKillHandler;
    private final StatusMessageHandler messageHandler;

    public BotListener(BossKillHandler bossKillHandler,
                       ManualKillHandler manualKillHandler,
                       StatusMessageHandler messageHandler) {
        this.bossKillHandler = bossKillHandler;
        this.manualKillHandler = manualKillHandler;
        this.messageHandler = messageHandler;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "kill" -> bossKillHandler.handleKillCommand(event);
            case "status" -> messageHandler.handleStatusCommand(event);
            default -> event.reply("Unknown command").setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        switch (event.getComponentId()) {
            case "kill_1" -> bossKillHandler.handleKillButton(BossChannel.CH1, event);
            case "kill_2" -> bossKillHandler.handleKillButton(BossChannel.CH2, event);
            case "kill_3" -> bossKillHandler.handleKillButton(BossChannel.CH3, event);
            case "manual_kill" -> manualKillHandler.handleManualKillButton(event);
            default -> {
            }
        }
    }


    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if ("manual_kill_select".equals(event.getComponentId())) {
            manualKillHandler.handleManualKillSelect(event);
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        manualKillHandler.handleMessage(event);
    }
}