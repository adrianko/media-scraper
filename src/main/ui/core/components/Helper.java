package main.ui.core.components;

import com.sun.net.httpserver.Headers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Helper {
    
    public static String sha1(String message) {
        StringBuilder stringBuffer = new StringBuilder();
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(message.getBytes());

            for (byte aB : md.digest()) {
                stringBuffer.append(Integer.toString((aB & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        return stringBuffer.toString();
    }

    public static Map<String, String> retrievePOSTData(InputStream is, Headers h) {
        Map<String, String> params = new HashMap<>();
        boolean contentType = h.entrySet().stream().filter(l -> l.getKey().equals("Content-type") && l.getValue()
                .contains("application/x-www-form-urlencoded")).findAny().isPresent();

        if (contentType) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;

            try {
                while ((line = br.readLine()) != null) {
                    splitParamPair(line, params);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return params;
    }

    public static Map<String, String> retrieveGETData(String url) {
        Map<String, String> params = new HashMap<>();

        if (url.contains("?")) {
            String[] paramString = url.split("\\?");

            if (paramString.length > 1) {
                String pairs = paramString[1];

                if (pairs.contains("&")) {
                    Arrays.stream(pairs.split("&")).forEach(pair -> splitParamPair(pair, params));
                } else {
                    splitParamPair(pairs, params);
                }
            }
        }

        return params;
    }

    public static Map<String, String> splitParamPair(String pair, Map<String, String> params) {
        if (pair.contains("=")) {
            String[] singlePair = pair.split("=");
            params.put(singlePair[0], singlePair[1]);
        }

        return params;
    }

}
