package main.movie.orm;

public class Movie {
    
    private String title;
    private int year;
    private boolean found = false;
    
    public Movie(String t, int y) {
        setTitle(t);
        setYear(y);
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String t) {
        title = t;
    }
    
    public int getYear() {
        return year;
    }
    
    public void setYear(int y) {
        year = y;
    }
    
    public boolean found() {
        return found;
    }
    
    public void markFound() {
        found = true;
    }
    
}
