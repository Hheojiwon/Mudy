package com.example.mudy.music.repository;

import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class GuildSettingsRepository {

    private final Map<Long, Integer> guildVolumes = new ConcurrentHashMap<>();

    public void setVolume(long guildId, int volume) {
        guildVolumes.put(guildId, volume);
    }

    public int getVolume(long guildId) {
        return guildVolumes.getOrDefault(guildId, 100);
    }
}
