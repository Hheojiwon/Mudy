package com.example.mudy.music.service;

import com.example.mudy.music.constants.MusicResponseMessage;
import com.example.mudy.music.repository.GuildSettingsRepository;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.stereotype.Service;

@Service
public class MusicVolumeService {

    private final GuildSettingsRepository guildSettingsRepository;
    private final MusicPlayService musicPlayService;

    public MusicVolumeService(GuildSettingsRepository guildSettingsRepository, MusicPlayService musicPlayService) {
        this.guildSettingsRepository = guildSettingsRepository;
        this.musicPlayService = musicPlayService;
    }

    public String setVolume(Guild guild, int volume) {
        if (volume < 0 || volume > 100) {
            return MusicResponseMessage.MUSIC_VOLUME_INVALID.get();
        }

        guildSettingsRepository.setVolume(guild.getIdLong(), volume);

        musicPlayService.applyVolume(guild, volume);

        return MusicResponseMessage.MUSIC_VOLUME_SET.format(volume);
    }
}
