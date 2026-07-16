package dev.andrii.bosstracker.discord.message;

import dev.andrii.bosstracker.model.BossChannel;
import dev.andrii.bosstracker.model.BossState;
import dev.andrii.bosstracker.service.BossStateService;
import dev.andrii.bosstracker.util.TimeUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class StatusMessageBuilder {

    private static final String EMPTY_LINE = "\u200B\n";

    private final TimeUtil timeUtil;
    private final BossStateService bossService;

    public StatusMessageBuilder(TimeUtil timeUtil, BossStateService bossService) {
        this.timeUtil = timeUtil;
        this.bossService = bossService;
    }

    public MessageEmbed buildStatusMessage() {
        EmbedBuilder embed = createEmbed();

        for (BossChannel channel : BossChannel.values()) {
            embed.addField(
                channel.getEmoji() + " Канал",
                buildChannelStatus(channel),
                true
            );
        }

        return embed.build();
    }

    private EmbedBuilder createEmbed() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("🌳 Императорское Древо");
        embed.setColor(Color.CYAN);

        return embed;
    }

    private String buildChannelStatus(BossChannel channel) {
        BossState state = bossService.getState(channel);

        return state == null
            ? EMPTY_LINE + "❌ Нет данных"
            : formatState(state);
    }

    private String formatState(BossState state) {
        long lastKillTime = state.getLastKillTime();
        long respawnTime = bossService.getTimeUntilNextSpawnTick(state);

        return EMPTY_LINE + """
            🕒 **Убит**
            %s (%s)

            ⏳ **Респ**
            %s
            """.formatted(
            timeUtil.formatTime(lastKillTime),
            timeUtil.timeAgo(lastKillTime),
            timeUtil.formatDuration(respawnTime)
        );
    }
}