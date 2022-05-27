package com.gobara.musicplayerapp;

import java.io.Serializable;

public class MediaModel implements Serializable {
    String path;
    String name;
    String duration ;

    public MediaModel(String path, String name,String duration) {
        this.path = path;
        this.name = name;
        this.duration = duration;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String title)
    {
        this.name = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
