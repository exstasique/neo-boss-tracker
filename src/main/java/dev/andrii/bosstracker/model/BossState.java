package dev.andrii.bosstracker.model;

import java.util.concurrent.TimeUnit;

public class BossState {

    private static final long MIN_RESPAWN_DELAY = TimeUnit.HOURS.toMillis(2);
    private static final long MAX_RESPAWN_DELAY = TimeUnit.HOURS.toMillis(5);

    private final long lastKillTime;
    private final long respawnMinTime;
    private final long respawnMaxTime;

    private long lastNotifiedTickTime;

    public BossState(long lastKillTime) {
        this.lastKillTime = lastKillTime;
        this.respawnMinTime = lastKillTime + MIN_RESPAWN_DELAY;
        this.respawnMaxTime = lastKillTime + MAX_RESPAWN_DELAY;
    }

    public long getLastKillTime() {
        return lastKillTime;
    }

    public long getRespawnMinTime() {
        return respawnMinTime;
    }

    public long getRespawnMaxTime() {
        return respawnMaxTime;
    }

    public long getLastNotifiedTickTime() {
        return lastNotifiedTickTime;
    }

    public void setLastNotifiedTickTime(long lastNotifiedTickTime) {
        this.lastNotifiedTickTime = lastNotifiedTickTime;
    }
}