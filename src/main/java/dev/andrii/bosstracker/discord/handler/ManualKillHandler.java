package dev.andrii.bosstracker.discord.handler;

import dev.andrii.bosstracker.model.BossChannel;
import dev.andrii.bosstracker.service.KillRegistrationService;
import dev.andrii.bosstracker.storage.PendingKillRequestStorage;
import dev.andrii.bosstracker.util.TimeUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.time.format.DateTimeParseException;
import java.util.concurrent.TimeUnit;

public class ManualKillHandler {

    private static final String SUCCESS_MESSAGE = "✅ Время успешно обновлено.";
    private static final String INVALID_TIME_MESSAGE = "❌ Неверный формат времени. Используйте HH:mm.";

    private final TimeUtil timeUtil;
    private final KillRegistrationService service;
    private final PendingKillRequestStorage storage;

    public ManualKillHandler(TimeUtil timeUtil,
                             KillRegistrationService service,
                             PendingKillRequestStorage storage) {
        this.timeUtil = timeUtil;
        this.service = service;
        this.storage = storage;
    }

    public void handleManualKillButton(ButtonInteractionEvent event) {
        StringSelectMenu.Builder menu = StringSelectMenu.create("manual_kill_select");

        for (BossChannel channel : BossChannel.values()) {
            menu.addOption(
                "Канал " + channel.getNumber(),
                String.valueOf(channel.getNumber())
            );
        }

        event.reply("Выберите канал:")
            .addActionRow(menu.build())
            .setEphemeral(true)
            .queue();
    }

    public void handleManualKillSelect(StringSelectInteractionEvent event) {
        int channelNumber = Integer.parseInt(event.getValues().get(0));
        BossChannel channel = BossChannel.getChannelByNumber(channelNumber);
        storage.put(event.getUser().getIdLong(), channel);

        event.editMessage("""
                Канал %d выбран.

                Теперь отправьте время в формате HH:mm.
                """.formatted(channelNumber))
            .setComponents()
            .queue();
    }

    public void handleMessage(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        BossChannel channel = storage.get(event.getAuthor().getIdLong());

        if (channel == null) {
            return;
        }
        processKillTime(event.getMessage(), channel);
    }

    private void processKillTime(Message message, BossChannel channel) {
        try {
            String input = message.getContentRaw();
            service.register(channel, timeUtil.parseTime(input));
            storage.remove(message.getAuthor().getIdLong());

            sendTemporaryMessage(message.getChannel(), SUCCESS_MESSAGE);
        } catch (DateTimeParseException e) {
            sendTemporaryMessage(message.getChannel(), INVALID_TIME_MESSAGE);
        } finally {
            message.delete().queue();
        }
    }

    private void sendTemporaryMessage(MessageChannel channel, String content) {
        channel.sendMessage(content)
            .queue(reply -> reply.delete().queueAfter(10, TimeUnit.SECONDS));
    }
}