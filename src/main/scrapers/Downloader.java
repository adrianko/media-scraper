package main.scrapers;

import main.scrapers.tv.TVHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;

public abstract class Downloader {

    private static boolean running() {
        boolean found = false;

        try {
            Process p = Runtime.getRuntime().exec((TVHelper.isWindows ? "tasklist.exe" : "ps aux"));
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.contains(TVHelper.settings.get("dl_exe"))) {
                    found = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return found;
    }

    private static void start() {
        try {
            Runtime.getRuntime().exec(TVHelper.settings.get("dl_exe_path") + TVHelper.settings.get("dl_exe"));
            Thread.sleep(4000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void enqueue(String magnet) {
        if (!running()) {
            start();
        }

        send(getAddURL(magnet));
    }

    protected static void send(String url) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Basic " + new String(Base64.getEncoder().encode((TVHelper.settings
                    .get("dl_user") + ":" + TVHelper.settings.get("dl_pass")).getBytes())));
            con.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static String getAddLabelURL(String label, String hash) {
        return "http://" + TVHelper.settings.get("dl_host") + ":" + TVHelper.settings.get("dl_port") + "/gui/?action=" +
            "setprops&s=label&hash=" + hash + "&v=" + label + "&t=" + System.currentTimeMillis();
    }

    private static String getAddURL(String magnet) {
        try {
            return "http://" + TVHelper.settings.get("dl_host") + ":" + TVHelper.settings.get("dl_port") + "/gui/?action=" +
                "add-url&s=" + URLEncoder.encode(magnet, "US-ASCII") + "&t=" + System.currentTimeMillis();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }

    public abstract void setLabel(String magnet);
    
}