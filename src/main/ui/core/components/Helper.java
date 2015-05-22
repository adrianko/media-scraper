package main.ui.core.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public static Map<String, String> parseHTTPRequest(InputStream is) {
        Map<String, String> params = new HashMap<>();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;

        try {
            while ((line = br.readLine()) != null) {
                String[] pair = line.split("=");
                params.put(pair[0], pair[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return params;
    }
    
}
