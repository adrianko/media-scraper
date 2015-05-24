package test.ui.core.components;

import com.sun.net.httpserver.Headers;
import main.ui.core.components.Helper;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HelperTest {
    
    @Test
    public void sha1Alpha() {
        String exp = "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8";
        String act = Helper.sha1("password");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void sha1Numeric() {
        String exp = "b835c672565363230d65528838a0206f53bef988";
        String act = Helper.sha1("859023789474");

        Assert.assertEquals(exp, act);
    }
    
    @Test
    public void sha1Symbol() {
        String exp = "1931f83e8760fa9c4f7ee74156b90828cb19e570";
        String act = Helper.sha1("&(*@£&($*&(*£");

        Assert.assertEquals(exp, act);
    }
    
    @Test
    public void sha1AlphaNumeric() {
        String exp = "d33b97f878d22fbf0b978b26ea04a8d2f544e7f9";
        String act = Helper.sha1("S9H3b3H0Lk");
        
        Assert.assertEquals(exp, act);
    }
    
    @Test 
    public void sha1AlphaSymbol() {
        String exp = "23ad839d22975a0ad75bae82f7bde9b93b9113eb";
        String act = Helper.sha1("!PY£h$ioK)OR@nR");

        Assert.assertEquals(exp, act);
    }
    
    @Test
    public void sha1NumericSymbol() {
        String exp = "b85c6dfe2ab47452916e2d1d4c3a9c4bdbb32459";
        String act = Helper.sha1("798^%)(&%8^06)^&7(");

        Assert.assertEquals(exp, act);
    }
    
    @Test
    public void sha1AlphaNumericSymbol() {
        String exp = "1e65a9370e914afc9042ddfcaa3dc1819cdf5034";
        String act = Helper.sha1("5E=9CKkc*{tPj74R");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrievePOSTDataNoParam() {
        Map<String, String> exp = new HashMap<>();

        Headers h = new Headers();
        h.put("Content-type", Collections.singletonList("application/x-www-form-urlencoded"));
        Map<String, String> act = Helper.retrievePOSTData(new ByteArrayInputStream("".getBytes()), h);

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrievePOSTDataSingleParamAlpha() {
        Map<String, String> exp = new HashMap<>();
        exp.put("abc", "def");

        Headers h = new Headers();
        h.put("Content-type", Collections.singletonList("application/x-www-form-urlencoded"));
        Map<String, String> act = Helper.retrievePOSTData(new ByteArrayInputStream("abc=def".getBytes()), h);

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrievePOSTDataSingleParamNumeric() {

    }

    @Test
    public void retrievePOSTDataSingleParamSymbol() {

    }

    @Test
    public void retrievePOSTDataSingleParamAlphaNumeric() {

    }

    @Test
    public void retrievePOSTDataSingleParamNumericSymbol() {

    }

    @Test
    public void retrievePOSTDataSingleParamAlphaSymbol() {

    }

    @Test
    public void retrievePOSTDataSingleParamAlphaNumericSymbol() {

    }

    @Test
    public void retrievePOSTDataMultiParamAlpha() {
        Map<String, String> exp = new HashMap<>();
        exp.put("abc", "def");
        exp.put("ghi", "jkl");

        Headers h = new Headers();
        h.put("Content-type", Collections.singletonList("application/x-www-form-urlencoded"));
        Map<String, String> act = Helper.retrievePOSTData(new ByteArrayInputStream("abc=def\nghi=jkl".getBytes()), h);

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrievePOSTDataMultiParamNumeric() {

    }

    @Test
    public void retrievePOSTDataMultiParamSymbol() {

    }

    @Test
    public void retrievePOSTDataMultiParamAlphaNumeric() {

    }

    @Test
    public void retrievePOSTDataMultiParamNumericSymbol() {

    }

    @Test
    public void retrievePOSTDataMultiParamAlphaSymbol() {

    }

    @Test
    public void retrievePOSTDataMultiParamAlphaNumericSymbol() {

    }

    @Test
    public void retrievePOSTDataWrongContentTypeWithParam() {
        Map<String, String> exp = new HashMap<>();

        Headers h = new Headers();
        h.put("Content-type", Collections.singletonList("multipart/form-data; boundary=----WebKitFormBoundaryjZH"));
        Map<String, String> act = Helper.retrievePOSTData(new ByteArrayInputStream("abc=def".getBytes()), h);

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrieveGETDataNoParam() {
        Map<String, String> exp = new HashMap<>();
        Map<String, String> act = Helper.retrieveGETData("http://somewebsite.com/index.php");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrieveGETDataSingleParamAlpha() {
        Map<String, String> exp = new HashMap<>();
        exp.put("abc", "def");

        Map<String, String> act = Helper.retrieveGETData("http://somewebsite.com/index.php?abc=def");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrieveGETDataMultiParamAlpha() {
        Map<String, String> exp = new HashMap<>();
        exp.put("abc", "def");
        exp.put("ghi", "jkl");

        Map<String, String> act = Helper.retrieveGETData("http://somewebsite.com/index.php?abc=def&ghi=jkl");

        Assert.assertEquals(exp, act);
    }

}
