package com.example.mudy.music.listener;

import com.example.mudy.music.command.MusicCommand;
import com.example.mudy.music.constants.MusicResponseMessage;
import com.example.mudy.music.constants.MusicTheme;
import com.example.mudy.music.service.FavoriteService;
import com.example.mudy.music.service.MusicInfoService;
import com.example.mudy.music.service.MusicPlayService;
import com.example.mudy.music.service.MusicVolumeService;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Component
public class MusicCommandListener extends ListenerAdapter {

    private final MusicPlayService musicService;
    private final FavoriteService favoriteService;
    private final MusicInfoService musicInfoService;
    private final MusicVolumeService musicVolumeService;
    private final Map<String, Consumer<SlashCommandInteractionEvent>> commandHandlers;

    public MusicCommandListener(MusicPlayService musicService,
                                FavoriteService favoriteService,
                                MusicInfoService musicInfoService,
                                MusicVolumeService musicVolumeService) {
        this.musicService = musicService;
        this.favoriteService = favoriteService;
        this.musicInfoService = musicInfoService;
        this.musicVolumeService = musicVolumeService;
        this.commandHandlers = initCommandHandlers();
    }

    private Map<String, Consumer<SlashCommandInteractionEvent>> initCommandHandlers() {
        Map<String, Consumer<SlashCommandInteractionEvent>> map = new HashMap<>();

        map.put(MusicCommand.PLAY.getName(), this::handlePlay);
        map.put(MusicCommand.STOP.getName(), this::handleStop);
        map.put(MusicCommand.PAUSE.getName(), this::handlePause);
        map.put(MusicCommand.RESUME.getName(), this::handleResume);
        map.put(MusicCommand.SKIP.getName(), this::handleSkip);
        map.put(MusicCommand.NOW_PLAYING.getName(), this::handleNowPlaying);

        map.put(MusicCommand.FAVORITE_ADD.getName(), this::handleFavoriteAdd);
        map.put(MusicCommand.FAVORITE_LIST.getName(), this::handleFavoriteList);
        map.put(MusicCommand.FAVORITE_REMOVE.getName(), this::handleFavoriteRemove);

        map.put(MusicCommand.VOLUME.getName(), this::handleVolume);
        map.put(MusicCommand.FAVORITE_MOVE.getName(), this::handleFavoriteMove);

        return map;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Consumer<SlashCommandInteractionEvent> handler = commandHandlers.get(event.getName());
        if (handler != null) {
            try {
                handler.accept(event);
            } catch (Exception e) {
                e.printStackTrace();
                if (!event.isAcknowledged()) {
                    event.reply("❌ 오류가 발생했습니다.").setEphemeral(true).queue();
                }
            }
        }
    }

    private boolean validateAndReply(SlashCommandInteractionEvent event) {
        String validationError = musicService.validateVoiceState(event.getMember());
        if (validationError != null) {
            event.reply(validationError).setEphemeral(true).queue();
            return false;
        }
        return true;
    }

    private void handlePlay(SlashCommandInteractionEvent event) {
        if (!validateAndReply(event)) return;

        GuildVoiceState voiceState = event.getMember().getVoiceState();
        VoiceChannel voiceChannel = voiceState.getChannel().asVoiceChannel();

        String themeName = event.getOption("theme").getAsString();
        MusicTheme selectedTheme = MusicTheme.valueOf(themeName);

        musicService.playStudyPlaylist(event.getGuild(), voiceChannel, selectedTheme);

        event.reply(MusicResponseMessage.MUSIC_STUDY_PLAYLIST_START.get()).queue();
    }

    private void handleStop(SlashCommandInteractionEvent event) {
        if (!validateAndReply(event)) return;
        musicService.stop(event.getGuild());
        event.reply(MusicResponseMessage.MUSIC_STOP.get()).queue();
    }

    private void handlePause(SlashCommandInteractionEvent event) {
        if (!validateAndReply(event)) return;
        musicService.pause(event.getGuild());
        event.reply(MusicResponseMessage.MUSIC_PAUSE.get()).queue();
    }

    private void handleResume(SlashCommandInteractionEvent event) {
        if (!validateAndReply(event)) return;
        musicService.resume(event.getGuild());
        event.reply(MusicResponseMessage.MUSIC_RESUME.get()).queue();
    }

    private void handleSkip(SlashCommandInteractionEvent event) {
        if (!validateAndReply(event)) return;
        musicService.skipTrack(event.getGuild());
        event.reply(MusicResponseMessage.MUSIC_SKIP.get()).queue();
    }

    private void handleNowPlaying(SlashCommandInteractionEvent event) {
        MessageEmbed embed = musicInfoService.getNowPlaying(event.getGuild());
        if (embed == null) {
            event.reply(MusicResponseMessage.MUSIC_NO_TRACK.get()).setEphemeral(true).queue();
        } else {
            event.replyEmbeds(embed).queue();
        }
    }

    private void handleFavoriteAdd(SlashCommandInteractionEvent event) {
        if (!validateAndReply(event)) return;
        String result = favoriteService.addToFavorite(event.getGuild(), event.getUser().getId());
        event.reply(result).setEphemeral(true).queue();
    }

    private void handleFavoriteList(SlashCommandInteractionEvent event) {
        String userName = event.getMember().getEffectiveName();
        MessageEmbed embed = favoriteService.getFavoriteList(event.getUser().getId(), userName);
        event.replyEmbeds(embed).queue();
    }

    private void handleFavoriteRemove(SlashCommandInteractionEvent event) {
        int number = event.getOption("number").getAsInt();
        String result = favoriteService.removeFromFavorite(event.getUser().getId(), number);
        event.reply(result).setEphemeral(true).queue();
    }

    private void handleVolume(SlashCommandInteractionEvent event) {
        if (!validateAndReply(event)) return;

        int volume = event.getOption("level").getAsInt();
        String result = musicVolumeService.setVolume(event.getGuild(), volume);

        event.reply(result).queue();
    }

    private void handleFavoriteMove(SlashCommandInteractionEvent event) {
        int from = event.getOption("from").getAsInt();
        int to = event.getOption("to").getAsInt();

        String result = favoriteService.moveFavoriteTrack(event.getUser().getId(), from, to);

        event.reply(result).setEphemeral(true).queue();
    }
}
