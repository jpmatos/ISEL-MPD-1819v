package org.isel.jingle.model;

public class TrackRank {
    private final String name;
    private final String url;
    private final int duration;
    private final int rank;

    public TrackRank(String name, String url, int duration, int rank) {
        this.name = name;
        this.url = url;
        this.duration = duration;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getDuration() {
        return duration;
    }

    public int getRank() {
        return rank;
    }
}
