package com.example.mudy.music.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.example.mudy.music.handler.TrackScheduler;
import com.example.mudy.music.handler.AudioPlayerSendHandler;

// 각 서버(Guild)별 음악 재생을 관리하는 클래스
public class GuildMusicManager {

    private final AudioPlayer audioPlayer;
    private final TrackScheduler scheduler;

    public GuildMusicManager(DefaultAudioPlayerManager manager) {
        this.audioPlayer = new DefaultAudioPlayer(manager);
        this.scheduler = new TrackScheduler(this.audioPlayer);
        this.audioPlayer.addListener(this.scheduler);
    }

    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(audioPlayer);
    }

    public TrackScheduler getScheduler() {
        return scheduler;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }
}
