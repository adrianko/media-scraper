package main.scrapers.movie;

import main.scrapers.Downloader;

public class MovieDownloader extends Downloader {

    public void setLabel(String magnet) {
        send(getAddLabelURL("movie", magnet.substring(magnet.lastIndexOf(":") + 1)));
    }

}