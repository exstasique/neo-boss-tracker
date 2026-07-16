package dev.andrii.bosstracker.service;

import dev.andrii.bosstracker.discord.message.StatusMessageUpdater;
import dev.andrii.bosstracker.model.BossChannel;

public class KillRegistrationService {

    private final BossStateService stateService;
    private final StatusMessageUpdater updater;

    public KillRegistrationService(BossStateService stateService,
                           StatusMessageUpdater updater) {
        this.stateService = stateService;
        this.updater = updater;
    }

    public void register(BossChannel channel, long killTime) {
        stateService.bossKilled(channel, killTime);
        updater.updateNow();
    }

    public void registerNow(BossChannel channel) {
        register(channel, System.currentTimeMillis());
    }
}