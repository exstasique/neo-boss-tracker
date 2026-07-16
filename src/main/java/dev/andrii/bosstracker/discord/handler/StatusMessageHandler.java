package dev.andrii.bosstracker.discord.handler;

import dev.andrii.bosstracker.discord.message.StatusMessageBuilder;
import dev.andrii.bosstracker.storage.StatusMessageStorage;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class StatusMessageHandler {

    private final StatusMessageStorage storage;
    private final StatusMessageBuilder builder;

    public StatusMessageHandler(StatusMessageStorage storage,
                                StatusMessageBuilder builder) {
        this.storage = storage;
        this.builder = builder;
    }

    public void handleStatusCommand(SlashCommandInteractionEvent event) {
        MessageEmbed embed = builder.buildStatusMessage();

        event.replyEmbeds(embed)
            .addActionRow(
                Button.primary("kill_1", "💀 CH1"),
                Button.primary("kill_2", "💀 CH2"),
                Button.primary("kill_3", "💀 CH3"),
                Button.secondary("manual_kill", "🕒 Указать время")
            )
            .queue(hook ->
                hook.retrieveOriginal().queue(storage::setMessage)
            );
    }
}