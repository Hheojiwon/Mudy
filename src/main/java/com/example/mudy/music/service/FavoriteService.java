package com.example.mudy.music.service;

import com.example.mudy.music.constants.MusicResponseMessage;
import com.example.mudy.music.manager.GuildMusicManager;
import com.example.mudy.music.model.FavoriteTrack;
import com.example.mudy.music.repository.UserPlaylistRepository;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

// 즐겨찾기 전담 서비스
@Service
public class FavoriteService {

    private final MusicPlayService musicPlayService;
    private final UserPlaylistRepository userPlaylistRepository;

    public FavoriteService(MusicPlayService musicPlayService,
                           UserPlaylistRepository userPlaylistRepository) {
        this.musicPlayService = musicPlayService;
        this.userPlaylistRepository = userPlaylistRepository;
    }

    // 현재 곡을 즐겨찾기에 추가
    public String addToFavorite(Guild guild, String userId) {
        GuildMusicManager musicManager = musicPlayService.getGuildMusicManager(guild);
        AudioTrack track = musicManager.getAudioPlayer().getPlayingTrack();

        if (track == null) {
            return MusicResponseMessage.MUSIC_NO_TRACK.get();
        }

        FavoriteTrack favoriteTrack = new FavoriteTrack(
                track.getInfo().title,
                track.getInfo().author,
                track.getInfo().uri,
                track.getDuration()
        );

        userPlaylistRepository.addTrack(userId, favoriteTrack);

        return MusicResponseMessage.MUSIC_FAVORITE_ADDED.format(track.getInfo().title);
    }

    // 즐겨찾기 목록 조회
    public MessageEmbed getFavoriteList(String userId, String userName) {
        List<FavoriteTrack> tracks = userPlaylistRepository.getTracks(userId);

        if (tracks.isEmpty()) {
            return new EmbedBuilder()
                    .setColor(Color.RED)
                    .setDescription(MusicResponseMessage.MUSIC_FAVORITE_EMPTY.get())
                    .build();
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

    // 즐겨찾기에서 제거
    public String removeFromFavorite(String userId, int index) {
        boolean removed = userPlaylistRepository.removeTrack(userId, index - 1);

        if (removed) {
            return MusicResponseMessage.MUSIC_FAVORITE_REMOVED.format(index);
        } else {
            return MusicResponseMessage.MUSIC_FAVORITE_INVALID_INDEX.get();
        }
    }

    // 시간 포맷팅 (mm:ss)
    private String formatDuration(long millis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) % 60);
    }
}
