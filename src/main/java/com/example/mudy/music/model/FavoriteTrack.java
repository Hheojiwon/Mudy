package com.example.mudy.music.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FavoriteTrack {
    private final String title;
    private final String author;
    private final String url;
    private final long length;
}
