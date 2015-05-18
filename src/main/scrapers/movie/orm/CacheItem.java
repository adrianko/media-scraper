package main.scrapers.movie.orm;

public class CacheItem {

    private long id;
    private String title;
    private String magnet;

    public CacheItem(long id, String title, String magnet) {
        this.id = id;
        this.title = title;
        this.magnet = magnet;
    }

    public long getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMagnet() {
        return magnet;
    }

}
