package main.movie;

public class Movie {
    
    private String title;
    private int year;
    
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
    
}
