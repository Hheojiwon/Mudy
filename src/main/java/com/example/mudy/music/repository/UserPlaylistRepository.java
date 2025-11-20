package com.example.mudy.music.repository;

import com.example.mudy.music.model.FavoriteTrack;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class UserPlaylistRepository {

    private final Map<String, List<FavoriteTrack>> userPlaylists = new ConcurrentHashMap<>();

    public void addTrack(String userId, FavoriteTrack track) {
        List<FavoriteTrack> playlist = userPlaylists.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>());
        boolean exists = playlist.stream().anyMatch(t -> t.getUrl().equals(track.getUrl()));
        if (!exists) {
            playlist.add(track);
        }
    }

    public List<FavoriteTrack> getTracks(String userId) {
        return userPlaylists.getOrDefault(userId, List.of());
    }

    public boolean removeTrack(String userId, int index) {
        List<FavoriteTrack> playlist = userPlaylists.get(userId);
        if (playlist != null && index >= 0 && index < playlist.size()) {
            playlist.remove(index);
            return true;
        }
        return false;
    }
}