package com.example.mudy.music.constants;

import lombok.Getter;

@Getter
public enum MusicTheme {

    POP("팝송","https://soundcloud.com/glchan11/sets/qcykddjzdjjv"),
    K_POP("케이팝","https://soundcloud.com/xmth7mbl4pqu/sets/qgljlrwlsjfv"),
    PIANO("피아노","https://soundcloud.com/o48x6rpamtfo/sets/yoytisnfs3ti");

    private final String name;
    private final String url;

    MusicTheme(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
