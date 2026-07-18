package dev.andrii.bosstracker.discord.notification;

import dev.andrii.bosstracker.model.BossChannel;
import dev.andrii.bosstracker.model.BossState;
import dev.andrii.bosstracker.service.BossStateService;
import dev.andrii.bosstracker.storage.StatusMessageStorage;
import net.dv8tion.jda.api.entities.Message;

import java.util.concurrent.TimeUnit;

public class SpawnNotifier {

    private final BossStateService stateService;
    private final StatusMessageStorage statusStorage;

    public SpawnNotifier(BossStateService stateService,
                         StatusMessageStorage statusStorage) {
        this.stateService = stateService;
        this.statusStorage = statusStorage;
    }

    public void checkNotifications() {
        for (BossChannel channel : BossChannel.values()) {
            BossState state = stateService.getState(channel);

            if (state == null) {
                continue;
            }

            checkNotification(channel, state);
        }
    }

    private void checkNotification(BossChannel channel, BossState state) {
        long currentTickTime = stateService.getCurrentSpawnTick(state);
        long lastNotifiedTickTime = state.getLastNotifiedTickTime();

        if (currentTickTime == 0 || currentTickTime == lastNotifiedTickTime) {
            return;
        }

        sendNotification(channel);
        state.setLastNotifiedTickTime(currentTickTime);
    }

    private void sendNotification(BossChannel channel) {
        Message statusMessage = statusStorage.getMessage();

        if (statusMessage == null) {
            return;
        }

        statusMessage.getChannel()
            .sendMessage("""
                🌳 Возможно появление **Императорского Древа** на %d канале!
                """.formatted(channel.getNumber()))
            .queue(message ->
                message.delete().queueAfter(3, TimeUnit.MINUTES));
    }
}