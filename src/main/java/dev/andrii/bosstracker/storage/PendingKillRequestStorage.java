package dev.andrii.bosstracker.storage;

import dev.andrii.bosstracker.model.BossChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PendingKillRequestStorage {

    private final Map<Long, BossChannel> pendingRequests = new ConcurrentHashMap<>();

    public void put(long userId, BossChannel channel) {
        pendingRequests.put(userId, channel);
    }

    public BossChannel get(long userId) {
        return pendingRequests.get(userId);
    }

    public BossChannel remove(long userId) {
        return pendingRequests.remove(userId);
    }
}