package main.scrapers;

public class Base {

    public static String os = System.getProperty("os.name");

    public static String path = os.contains("Windows") && Base.class.getResource(".").getPath().startsWith("/") ?
            (Base.class.getResource(".").getPath() + "../../../../../").substring(1) :
            Base.class.getResource(".").getPath() + "../../../../../";
    
}
