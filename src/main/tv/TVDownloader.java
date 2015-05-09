package main.tv;

import main.Downloader;

public class TVDownloader extends Downloader {

    public void setLabel(String magnet) {
        send(getAddLabelURL("tv", magnet.substring(magnet.lastIndexOf(":") + 1)));
    }

}