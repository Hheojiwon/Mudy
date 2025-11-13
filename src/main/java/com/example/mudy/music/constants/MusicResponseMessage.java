package com.example.mudy.music.constants;

public enum MusicResponseMessage {

    ERROR_SERVER_ONLY("❌ 서버에서만 사용 가능합니다."),
    ERROR_NOT_IN_VOICE("❌ 음성 채널에 먼저 입장해주세요!"),

    MUSIC_LOADING("🎵 재생 중..."),
    MUSIC_PLAY_SUCCESS("✅ 재생 시작: **%s**"),
    MUSIC_PLAY_FAILED("❌ %s"),

    MUSIC_STOP("⏹️ 재생을 정지했습니다."),
    MUSIC_SKIP("⏭️ 다음 곡으로 넘어갑니다."),
    MUSIC_PAUSE_TOGGLE("⏸️ 일시정지/재개"),

    MUSIC_VOLUME_SET("🔊 볼륨: %d%%"),
    MUSIC_VOLUME_INVALID("❌ 볼륨은 0-100 사이여야 합니다."),

    MUSIC_NO_TRACK("현재 재생 중인 곡이 없습니다."),
    MUSIC_NO_MATCHES("검색 결과가 없습니다: %s"),
    MUSIC_LOAD_FAILED("재생 실패: %s"),

    MUSIC_STUDY_PLAYLIST_START("🎵 공부하기 좋은 조용한 음악을 재생합니다.");

    private final String message;

    MusicResponseMessage(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}
