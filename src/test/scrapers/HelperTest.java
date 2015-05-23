package test.scrapers;

import main.scrapers.Helper;
import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Pattern;

public class HelperTest {

    private static final String IP_ADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    
    @Test
    public void getCurrentIPFormat() {
        String act = Helper.getCurrentIP();
        
        Assert.assertTrue(Pattern.compile(IP_ADDRESS_PATTERN).matcher(act).find());
    }

}
