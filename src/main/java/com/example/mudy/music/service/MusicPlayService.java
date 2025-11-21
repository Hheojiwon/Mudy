package com.example.mudy.music.service;

import com.example.mudy.music.constants.MusicConstants;
import com.example.mudy.music.constants.MusicResponseMessage;
import com.example.mudy.music.manager.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

// 음악 재생 전담 서비스
@Service
public class MusicPlayService {

    private final DefaultAudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public MusicPlayService() {
        this.playerManager = new DefaultAudioPlayerManager();
        this.musicManagers = new HashMap<>();

        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    // 서버별 음악 매니저 가져오기
    public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }

    // 음성 채널 검증
    public String validateVoiceState(Member member) {
        if (member == null) {
            return MusicResponseMessage.ERROR_SERVER_ONLY.get();
        }

        GuildVoiceState voiceState = member.getVoiceState();
        if (voiceState == null || !voiceState.inAudioChannel()) {
            return MusicResponseMessage.ERROR_NOT_IN_VOICE.get();
        }

        return null;
    }

    // 스터디 플레이리스트 재생
    public void playStudyPlaylist(Guild guild, VoiceChannel voiceChannel) {
        GuildMusicManager musicManager = getGuildMusicManager(guild);
        musicManager.getScheduler().clearQueue();
        guild.getAudioManager().openAudioConnection(voiceChannel);

        for (String trackUrl : MusicConstants.STUDY_PLAYLIST) {
            loadTrack(musicManager, trackUrl);
        }
    }

    // 트랙 로딩
    private void loadTrack(GuildMusicManager musicManager, String trackUrl) {
        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.getScheduler().queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (AudioTrack track : playlist.getTracks()) {
                    musicManager.getScheduler().queue(track);
                }
            }

            @Override
            public void noMatches() {
                System.err.println(MusicResponseMessage.MUSIC_NO_MATCHES.format(trackUrl));
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                System.err.println(MusicResponseMessage.MUSIC_LOAD_FAILED.format(exception.getMessage()));
            }
        });
    }

    // 재생 정지
    public void stop(Guild guild) {
        GuildMusicManager musicManager = getGuildMusicManager(guild);
        musicManager.getScheduler().clearQueue();
        musicManager.getAudioPlayer().stopTrack();
        guild.getAudioManager().closeAudioConnection();
    }

    // 일시정지
    public void pause(Guild guild) {
        GuildMusicManager musicManager = getGuildMusicManager(guild);
        musicManager.getAudioPlayer().setPaused(true);
    }

    // 재개
    public void resume(Guild guild) {
        GuildMusicManager musicManager = getGuildMusicManager(guild);
        musicManager.getAudioPlayer().setPaused(false);
    }

    // 스킵
    public void skipTrack(Guild guild) {
        GuildMusicManager musicManager = getGuildMusicManager(guild);
        musicManager.getScheduler().nextTrack();
    }
}
