package com.example.mudy.music.service;

import com.example.mudy.music.constants.MusicConstants;
import com.example.mudy.music.constants.MusicResponseMessage;
import com.example.mudy.music.manager.GuildMusicManager;
import com.example.mudy.music.model.FavoriteTrack;
import com.example.mudy.music.repository.UserPlaylistRepository;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MusicService {

    private final DefaultAudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final UserPlaylistRepository userPlaylistRepository;

    public MusicService(UserPlaylistRepository userPlaylistRepository) {
        this.userPlaylistRepository = userPlaylistRepository;
        this.playerManager = new DefaultAudioPlayerManager();
        this.musicManagers = new HashMap<>();

        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

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

    public void playStudyPlaylist(Guild guild, VoiceChannel voiceChannel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        musicManager.getScheduler().clearQueue();
        guild.getAudioManager().openAudioConnection(voiceChannel);
        for (String trackUrl : MusicConstants.STUDY_PLAYLIST) {

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
    }

    // 봇 음성채널에서 나가기
    public void stop(Guild guild) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        musicManager.getScheduler().clearQueue();
        musicManager.getAudioPlayer().stopTrack();
        AudioManager audioManager = guild.getAudioManager();
        audioManager.closeAudioConnection();
    }

    public void pause(Guild guild) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        musicManager.getAudioPlayer().setPaused(true);
    }

    public void resume(Guild guild) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        musicManager.getAudioPlayer().setPaused(false);
    }

    public void skipTrack(Guild guild) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        musicManager.getScheduler().nextTrack();
    }

    private String formatDuration(long millis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) % 60);
    }

    public MessageEmbed getNowPlaying(Guild guild) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
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

    public String addToFavorite(Guild guild, String userId) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        AudioTrack track = musicManager.getAudioPlayer().getPlayingTrack();

        if (track == null) return MusicResponseMessage.MUSIC_NO_TRACK.get();

        FavoriteTrack favoriteTrack = new FavoriteTrack(
                track.getInfo().title,
                track.getInfo().author,
                track.getInfo().uri,
                track.getDuration()
        );

        userPlaylistRepository.addTrack(userId, favoriteTrack);

        return MusicResponseMessage.MUSIC_FAVORITE_ADDED.format(track.getInfo().title);
    }

    public MessageEmbed getFavoriteList(String userId, String userName) {
        List<FavoriteTrack> tracks = userPlaylistRepository.getTracks(userId);
        if (tracks.isEmpty()) {
            return new EmbedBuilder().setColor(Color.RED)
                    .setDescription(MusicResponseMessage.MUSIC_FAVORITE_EMPTY.get()).build();
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.YELLOW)
                .setTitle(MusicResponseMessage.MUSIC_FAVORITE_LIST_TITLE.format(userName));

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tracks.size(); i++) {
            FavoriteTrack track = tracks.get(i);
            sb.append("**").append(i + 1).append(".** ")
                    .append("[").append(track.getTitle()).append("](").append(track.getUrl()).append(") ")
                    .append("(`").append(formatDuration(track.getLength())).append("`)\n");
        }
        embed.setDescription(sb.toString());
        return embed.build();
    }
}
