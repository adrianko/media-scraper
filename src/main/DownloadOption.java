package main;

public class DownloadOption {

    private String name;
    private String magnet;
    private String size;
    private double byteSize;

    public DownloadOption(String n, String m, String s) {
        name = n;
        magnet = m;
        size = s;
        byteSize = byteConversion(size);
    }

    public String getName() {
        return name;
    }

    public String getMagnet() {
        return magnet;
    }

    public String getSize() {
        return size;
    }

    public double getByteSize() {
        return byteSize;
    }

    public double byteConversion(String filesize) {
        double mul;

        switch (filesize.substring(filesize.length() - 2)) {
            case "KB":
                mul = 1024.0;
                break;
            case "MB":
                mul = 1048576.0;
                break;
            case "GB":
                mul = 1073741824.0;
                break;
            default:
                mul = 1;
                break;
        }

        return Double.valueOf(filesize.substring(0, filesize.length() - 2)) * mul;
    }

}