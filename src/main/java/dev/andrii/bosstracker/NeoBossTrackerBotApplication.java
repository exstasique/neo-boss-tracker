package dev.andrii.bosstracker;

import dev.andrii.bosstracker.discord.message.StatusMessageUpdater;
import dev.andrii.bosstracker.discord.listener.BotListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class NeoBossTrackerBotApplication {

    private final String token;
    private final BotListener listener;
    private final StatusMessageUpdater updater;

    public NeoBossTrackerBotApplication(String token,
                                        BotListener listener,
                                        StatusMessageUpdater updater) {
        this.token = token;
        this.listener = listener;
        this.updater = updater;
    }

    public void start() {
        try {
            JDA jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(listener)
                .build();
            jda.awaitReady();
            registerCommands(jda);
            updater.start();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Bot startup interrupted", e);
        }
    }

    private void registerCommands(JDA jda) {
        jda.updateCommands()
            .addCommands(
                Commands.slash("kill", "Mark boss as killed")
                    .addOptions(
                        new OptionData(OptionType.INTEGER, "channel", "Channel number", true)
                            .addChoice("Channel 1", 1)
                            .addChoice("Channel 2", 2)
                            .addChoice("Channel 3", 3)
                    )
                    .addOption(OptionType.STRING, "time", "Optional kill time (HH:mm)", false),
                Commands.slash("status", "Show boss status")
            )
            .queue();
    }
}