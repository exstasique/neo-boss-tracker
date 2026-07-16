package dev.andrii.bosstracker.discord.handler;

import dev.andrii.bosstracker.model.BossChannel;
import dev.andrii.bosstracker.service.KillRegistrationService;
import dev.andrii.bosstracker.util.TimeUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class BossKillHandler {

    private final TimeUtil timeUtil;
    private final KillRegistrationService service;

    public BossKillHandler(TimeUtil timeUtil,
                           KillRegistrationService service) {
        this.timeUtil = timeUtil;
        this.service = service;
    }

    public void handleKillCommand(SlashCommandInteractionEvent event) {
        int channelNumber = event.getOption("channel").getAsInt();
        BossChannel channel = BossChannel.getChannelByNumber(channelNumber);
        service.register(channel, getKillTime(event));
        event.reply("💀 Boss killed on channel " + channelNumber).queue();
    }

    private long getKillTime(SlashCommandInteractionEvent event) {
        OptionMapping timeOption = event.getOption("time");

        return timeOption == null
            ? System.currentTimeMillis()
            : timeUtil.parseTime(timeOption.getAsString());
    }

    public void handleKillButton(BossChannel channel, ButtonInteractionEvent event) {
        event.deferEdit().queue();
        service.registerNow(channel);
    }
}