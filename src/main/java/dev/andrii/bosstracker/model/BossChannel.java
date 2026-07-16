package dev.andrii.bosstracker.model;

import java.util.HashMap;
import java.util.Map;

public enum BossChannel {

    CH1(1, "1️⃣"),
    CH2(2, "2️⃣"),
    CH3(3, "3️⃣");

    private final int number;
    private final String emoji;
    private static final Map<Integer, BossChannel> CHANNELS = new HashMap<>();

    static {
        for (BossChannel channel : BossChannel.values()) {
            CHANNELS.put(channel.number, channel);
        }
    }

    BossChannel(int number, String emoji) {
        this.number = number;
        this.emoji = emoji;
    }

    public int getNumber() {
        return number;
    }

    public String getEmoji() {
        return emoji;
    }

    public static BossChannel getChannelByNumber(int number) {
        return CHANNELS.get(number);
    }
}