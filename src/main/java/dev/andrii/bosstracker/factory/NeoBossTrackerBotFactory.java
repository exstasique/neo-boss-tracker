package dev.andrii.bosstracker.factory;

import dev.andrii.bosstracker.NeoBossTrackerBotApplication;
import dev.andrii.bosstracker.config.ApplicationSettings;
import dev.andrii.bosstracker.discord.handler.BossKillHandler;
import dev.andrii.bosstracker.discord.handler.ManualKillHandler;
import dev.andrii.bosstracker.discord.handler.StatusMessageHandler;
import dev.andrii.bosstracker.discord.listener.BotListener;
import dev.andrii.bosstracker.discord.message.StatusMessageBuilder;
import dev.andrii.bosstracker.discord.message.StatusMessageUpdater;
import dev.andrii.bosstracker.discord.notification.SpawnNotifier;
import dev.andrii.bosstracker.service.BossStateService;
import dev.andrii.bosstracker.service.KillRegistrationService;
import dev.andrii.bosstracker.storage.PendingKillRequestStorage;
import dev.andrii.bosstracker.storage.StatusMessageStorage;
import dev.andrii.bosstracker.util.TimeUtil;

public class NeoBossTrackerBotFactory {

    private final ApplicationSettings settings;

    public NeoBossTrackerBotFactory(ApplicationSettings settings) {
        this.settings = settings;
    }

    public NeoBossTrackerBotApplication create() {
        final BossStateService service = new BossStateService();
        final TimeUtil timeUtil = new TimeUtil(settings.getTimezone());
        final StatusMessageStorage storage = new StatusMessageStorage();
        final StatusMessageBuilder builder = new StatusMessageBuilder(timeUtil, service);
        final StatusMessageHandler handler = new StatusMessageHandler(storage, builder);

        final StatusMessageUpdater updater = createStatusMessageUpdater(service, storage, builder);
        final BotListener listener = createBotListener(timeUtil, service, updater, handler);

        return new NeoBossTrackerBotApplication(settings.getToken(), listener, updater);
    }

    private StatusMessageUpdater createStatusMessageUpdater(BossStateService service,
                                                            StatusMessageStorage storage,
                                                            StatusMessageBuilder builder) {
        final SpawnNotifier notifier = new SpawnNotifier(service, storage);

        return new StatusMessageUpdater(settings.getUpdateInterval(), notifier, storage, builder);
    }

    private BotListener createBotListener(TimeUtil timeUtil,
                                          BossStateService stateService,
                                          StatusMessageUpdater messageUpdater,
                                          StatusMessageHandler messageHandler) {
        final PendingKillRequestStorage requestStorage = new PendingKillRequestStorage();
        final KillRegistrationService killService = new KillRegistrationService(stateService, messageUpdater);
        final BossKillHandler bossKillHandler = new BossKillHandler(timeUtil, killService);
        final ManualKillHandler manualKillHandler = new ManualKillHandler(timeUtil, killService, requestStorage);

        return new BotListener(bossKillHandler, manualKillHandler, messageHandler);
    }
}