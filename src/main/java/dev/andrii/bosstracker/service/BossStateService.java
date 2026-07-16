package dev.andrii.bosstracker.service;

import dev.andrii.bosstracker.model.BossChannel;
import dev.andrii.bosstracker.model.BossState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class BossStateService {

    private static final long SPAWN_TICK = TimeUnit.MINUTES.toMillis(15);

    private final Map<BossChannel, BossState> states = new ConcurrentHashMap<>();

    public void bossKilled(BossChannel channel, long killTime) {
        BossState state = new BossState(killTime);
        states.put(channel, state);
    }

    public BossState getState(BossChannel channel) {
        return states.get(channel);
    }

    public long getTimeUntilNextSpawnTick(BossState state) {
        long minRespawnTime = state.getRespawnMinTime();
        long maxRespawnTime = state.getRespawnMaxTime();
        long now = System.currentTimeMillis();

        if (now < minRespawnTime) {
            return minRespawnTime - now;
        }

        if (now > maxRespawnTime) {
            return 0;
        }

        long elapsedTicks = (now - minRespawnTime) / SPAWN_TICK;
        long nextTick = minRespawnTime + (elapsedTicks + 1) * SPAWN_TICK;

        return Math.min(nextTick, maxRespawnTime) - now;
    }

    public long getCurrentSpawnTick(BossState state) {
        long minRespawnTime = state.getRespawnMinTime();
        long maxRespawnTime = state.getRespawnMaxTime();
        long now = System.currentTimeMillis();

        if (now < minRespawnTime || now > maxRespawnTime) {
            return 0;
        }

        long elapsedTicks = (now - minRespawnTime) / SPAWN_TICK;

        return minRespawnTime + elapsedTicks * SPAWN_TICK;
    }
}