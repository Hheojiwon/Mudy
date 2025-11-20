package com.example.mudy.music.listener;

import com.example.mudy.music.command.MusicCommand;
import com.example.mudy.music.constants.MusicResponseMessage;
import com.example.mudy.music.service.MusicService;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

@Component
public class MusicCommandListener extends ListenerAdapter {

    private final MusicService musicService;
    private final Map<String, Consumer<SlashCommandInteractionEvent>> commandHandlers;

    public MusicCommandListener(MusicService musicService) {
        this.musicService = musicService;
        this.commandHandlers = initCommandHandlers();
    }

    private Map<String, Consumer<SlashCommandInteractionEvent>> initCommandHandlers() {
        return Map.of(
                MusicCommand.PLAY.getName(), this::handlePlay,
                MusicCommand.STOP.getName(), this::handleStop,
                MusicCommand.PAUSE.getName(), this::handlePause,
                MusicCommand.RESUME.getName(), this::handleResume,
                MusicCommand.SKIP.getName(), this::handleSkip,
                MusicCommand.NOW_PLAYING.getName(), this::handleNowPlaying,
                MusicCommand.FAVORITE_ADD.getName(), this::handleFavoriteAdd,
                MusicCommand.FAVORITE_LIST.getName(), this::handleFavoriteList
        );
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Consumer<SlashCommandInteractionEvent> handler = commandHandlers.get(event.getName());
        if (handler != null) {
            handler.accept(event);
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

        musicService.playStudyPlaylist(event.getGuild(), voiceChannel);
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
        MessageEmbed embed = musicService.getNowPlaying(event.getGuild());
        if (embed == null) {
            event.reply(MusicResponseMessage.MUSIC_NO_TRACK.get()).setEphemeral(true).queue();
        } else {
            event.replyEmbeds(embed).queue();
        }
    }

    private void handleFavoriteAdd(SlashCommandInteractionEvent event) {
        if (!validateAndReply(event)) return;
        String result = musicService.addToFavorite(event.getGuild(), event.getUser().getId());
        event.reply(result).setEphemeral(true).queue();
    }

    private void handleFavoriteList(SlashCommandInteractionEvent event) {
        String userName = event.getMember().getEffectiveName();
        MessageEmbed embed = musicService.getFavoriteList(event.getUser().getId(), userName);
        event.replyEmbeds(embed).queue();
    }
}
