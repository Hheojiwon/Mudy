package com.example.mudy.music.service;

import com.example.mudy.music.constants.MusicResponseMessage;
import com.example.mudy.music.manager.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.concurrent.TimeUnit;

// 현재 재생 정보 전담 서비스
@Service
public class MusicInfoService {

    private final MusicPlayService musicPlayService;

    public MusicInfoService(MusicPlayService musicPlayService) {
        this.musicPlayService = musicPlayService;
    }

    // 현재 재생 중인 곡 정보 반환
    public MessageEmbed getNowPlaying(Guild guild) {
        GuildMusicManager musicManager = musicPlayService.getGuildMusicManager(guild);
        AudioTrack track = musicManager.getAudioPlayer().getPlayingTrack();

        if (track == null) {
            return null;
        }

        return new EmbedBuilder()
                .setColor(Color.CYAN)
                .setTitle(MusicResponseMessage.MUSIC_NOW_PLAYING_TITLE.get())
                .setDescription("**[" + track.getInfo().title + "](" + track.getInfo().uri + ")**")
                .addField(MusicResponseMessage.MUSIC_NOW_PLAYING_ARTIST.get(), track.getInfo().author, true)
                .addField(MusicResponseMessage.MUSIC_NOW_PLAYING_DURATION.get(), formatDuration(track.getDuration()), true)
                .build();
    }

    // 시간 포맷팅 (mm:ss)
    private String formatDuration(long millis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) % 60);
    }
}
