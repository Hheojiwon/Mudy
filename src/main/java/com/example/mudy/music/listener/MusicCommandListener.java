package com.example.mudy.music.listener;

import com.example.mudy.music.command.MusicCommand;
import com.example.mudy.music.constants.MusicResponseMessage;
import com.example.mudy.music.service.MusicService;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class MusicCommandListener extends ListenerAdapter {

    private final MusicService musicService;

    public MusicCommandListener(MusicService musicService) {
        this.musicService = musicService;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (event.getName().equals(MusicCommand.PLAY.getName())) {
            handlePlay(event);
        } else if (event.getName().equals(MusicCommand.STOP.getName())) {
            handleStop(event);
        } else if (event.getName().equals(MusicCommand.PAUSE.getName())) {
            handlePause(event);
        } else if (event.getName().equals(MusicCommand.RESUME.getName())) {
            handleResume(event);
        } else if (event.getName().equals(MusicCommand.SKIP.getName())) {
            handleSkip(event);
        }
    }

    private void handlePlay(SlashCommandInteractionEvent event) {
        Member member = event.getMember();

        String validationError = musicService.validateVoiceState(member);
        if (validationError != null) {
            event.reply(validationError).setEphemeral(true).queue();
            return;
        }

        GuildVoiceState voiceState = member.getVoiceState();
        VoiceChannel voiceChannel = voiceState.getChannel().asVoiceChannel();

        musicService.playStudyPlaylist(event.getGuild(), voiceChannel);

        event.reply(MusicResponseMessage.MUSIC_STUDY_PLAYLIST_START.get()).queue();
    }

    private void handleStop(SlashCommandInteractionEvent event) {
        String validationError = musicService.validateVoiceState(event.getMember());
        if (validationError != null) {
            event.reply(validationError).setEphemeral(true).queue();
            return;
        }
        musicService.stop(event.getGuild());
        event.reply(MusicResponseMessage.MUSIC_STOP.get()).queue();
    }

    private void handlePause(SlashCommandInteractionEvent event) {
        String validationError = musicService.validateVoiceState(event.getMember());
        if (validationError != null) {
            event.reply(validationError).setEphemeral(true).queue();
        }
        musicService.pause(event.getGuild());
        event.reply(MusicResponseMessage.MUSIC_PAUSE.get()).queue();
    }

    private void handleResume(SlashCommandInteractionEvent event) {
        String validationError = musicService.validateVoiceState(event.getMember());
        if (validationError != null) {
            event.reply(validationError).setEphemeral(true).queue();
        }
        musicService.resume(event.getGuild());
        event.reply(MusicResponseMessage.MUSIC_RESUME.get()).queue();
    }

    private void handleSkip(SlashCommandInteractionEvent event) {
        String validationError = musicService.validateVoiceState(event.getMember());
        if (validationError != null) {
            event.reply(validationError).setEphemeral(true).queue();
            return;
        }
        musicService.skipTrack(event.getGuild());
        event.reply(MusicResponseMessage.MUSIC_SKIP.get()).queue();
    }
}
