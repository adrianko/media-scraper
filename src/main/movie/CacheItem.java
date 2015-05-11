package main.movie;

public class CacheItem {

    private int id;
    private String title;
    private String magnet;

    public CacheItem(int id, String title, String magnet) {
        this.id = id;
        this.title = title;
        this.magnet = magnet;
    }

    public int getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMagnet() {
        return magnet;
    }

}
