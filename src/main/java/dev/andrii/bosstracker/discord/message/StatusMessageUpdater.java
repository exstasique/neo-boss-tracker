package dev.andrii.bosstracker.discord.message;

import dev.andrii.bosstracker.discord.notification.SpawnNotifier;
import dev.andrii.bosstracker.storage.StatusMessageStorage;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StatusMessageUpdater {

    private final int updateInterval;
    private final SpawnNotifier spawnNotifier;
    private final StatusMessageStorage messageStorage;
    private final StatusMessageBuilder messageBuilder;
    private final ScheduledExecutorService scheduler;

    public StatusMessageUpdater(int updateInterval,
                                SpawnNotifier spawnNotifier,
                                StatusMessageStorage messageStorage,
                                StatusMessageBuilder messageBuilder) {
        this.updateInterval = updateInterval;
        this.spawnNotifier = spawnNotifier;
        this.messageStorage = messageStorage;
        this.messageBuilder = messageBuilder;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        scheduler.scheduleAtFixedRate(
            this::updateNow,
            0,
            updateInterval,
            TimeUnit.SECONDS
        );
    }

    public void updateNow() {
        Message message = messageStorage.getMessage();
        if (message == null) {
            return;
        }
        MessageEmbed updated = messageBuilder.buildStatusMessage();
        message.editMessageEmbeds(updated).queue();
        spawnNotifier.checkNotifications();
    }
}