package test.ui.core.components;

import com.sun.net.httpserver.Headers;
import main.ui.core.components.Helper;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        Map<String, String> exp = new HashMap<>();
        exp.put("123", "456");

        Headers h = new Headers();
        h.put("Content-type", Collections.singletonList("application/x-www-form-urlencoded"));
        Map<String, String> act = Helper.retrievePOSTData(new ByteArrayInputStream("123=456".getBytes()), h);

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrievePOSTDataSingleParamSymbol() {
        Map<String, String> exp = new HashMap<>();
        exp.put("abc", "()");

        Headers h = new Headers();
        h.put("Content-type", Collections.singletonList("application/x-www-form-urlencoded"));
        Map<String, String> act = Helper.retrievePOSTData(new ByteArrayInputStream("abc=%28%29".getBytes()), h);

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrievePOSTDataSingleParamAlphaNumeric() {
        Map<String, String> exp = new HashMap<>();
        exp.put("a1b2c3", "d4e5f6");

        Headers h = new Headers();
        h.put("Content-type", Collections.singletonList("application/x-www-form-urlencoded"));
        Map<String, String> act = Helper.retrievePOSTData(new ByteArrayInputStream("a1b2c3=d4e5f6".getBytes()), h);

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrievePOSTDataSingleParamNumericSymbol() {
        Map<String, String> exp = new HashMap<>();
        exp.put("123", "()456");

        Headers h = new Headers();
        h.put("Content-type", Collections.singletonList("application/x-www-form-urlencoded"));
        Map<String, String> act = Helper.retrievePOSTData(new ByteArrayInputStream("123=%28%29456".getBytes()), h);

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrievePOSTDataSingleParamAlphaSymbol() {
        Map<String, String> exp = new HashMap<>();
        exp.put("abc", "()def");

        Headers h = new Headers();
        h.put("Content-type", Collections.singletonList("application/x-www-form-urlencoded"));
        Map<String, String> act = Helper.retrievePOSTData(new ByteArrayInputStream("abc=%28%29def".getBytes()), h);

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrievePOSTDataSingleParamAlphaNumericSymbol() {
        Map<String, String> exp = new HashMap<>();
        exp.put("a1b2", "(d4e5)");

        Headers h = new Headers();
        h.put("Content-type", Collections.singletonList("application/x-www-form-urlencoded"));
        Map<String, String> act = Helper.retrievePOSTData(new ByteArrayInputStream("a1b2=%28d4e5%29".getBytes()), h);

        Assert.assertEquals(exp, act);
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
        Map<String, String> exp = new HashMap<>();
        exp.put("12", "34");
        exp.put("56", "78");

        Headers h = new Headers();
        h.put("Content-type", Collections.singletonList("application/x-www-form-urlencoded"));
        Map<String, String> act = Helper.retrievePOSTData(new ByteArrayInputStream("12=34\n56=78".getBytes()), h);

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrievePOSTDataMultiParamSymbol() {
        Map<String, String> exp = new HashMap<>();
        exp.put("ab", "()");
        exp.put("cd", "^$");

        Headers h = new Headers();
        h.put("Content-type", Collections.singletonList("application/x-www-form-urlencoded"));
        Map<String, String> act = Helper.retrievePOSTData(new ByteArrayInputStream(
                "ab=%28%29\ncd=%5E%24".getBytes()), h);

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrievePOSTDataMultiParamAlphaNumeric() {
        Map<String, String> exp = new HashMap<>();
        exp.put("12ab", "34cd");
        exp.put("56ef", "78gh");

        Headers h = new Headers();
        h.put("Content-type", Collections.singletonList("application/x-www-form-urlencoded"));
        Map<String, String> act = Helper.retrievePOSTData(new ByteArrayInputStream(
                "12ab=34cd\n56ef=78gh".getBytes()), h);

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrievePOSTDataMultiParamNumericSymbol() {
        Map<String, String> exp = new HashMap<>();
        exp.put("12", "(34)");
        exp.put("56", "^78$");

        Headers h = new Headers();
        h.put("Content-type", Collections.singletonList("application/x-www-form-urlencoded"));
        Map<String, String> act = Helper.retrievePOSTData(new ByteArrayInputStream(
                "12=%2834%29\n56=%5E78%24".getBytes()), h);

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrievePOSTDataMultiParamAlphaSymbol() {
        Map<String, String> exp = new HashMap<>();
        exp.put("ab", "(ef)");
        exp.put("cd", "^gh$");

        Headers h = new Headers();
        h.put("Content-type", Collections.singletonList("application/x-www-form-urlencoded"));
        Map<String, String> act = Helper.retrievePOSTData(new ByteArrayInputStream(
                "ab=%28ef%29\ncd=%5Egh%24".getBytes()), h);

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrievePOSTDataMultiParamAlphaNumericSymbol() {
        Map<String, String> exp = new HashMap<>();
        exp.put("a1b2", "(e3f4)");
        exp.put("c5d6", "^g7h8$");

        Headers h = new Headers();
        h.put("Content-type", Collections.singletonList("application/x-www-form-urlencoded"));
        Map<String, String> act = Helper.retrievePOSTData(new ByteArrayInputStream(
                "a1b2=%28e3f4%29\nc5d6=%5Eg7h8%24".getBytes()), h);

        Assert.assertEquals(exp, act);
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
    public void retrieveGETDataSingleParamNumeric() {
        Map<String, String> exp = new HashMap<>();
        exp.put("123", "456");

        Map<String, String> act = Helper.retrieveGETData("http://somewebsite.com/index.php?123=456");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrieveGETDataSingleParamSymbol() {
        Map<String, String> exp = new HashMap<>();
        exp.put("abc", "()^$");

        Map<String, String> act = Helper.retrieveGETData("http://somewebsite.com/index.php?abc=%28%29%5E%24");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrieveGETDataSingleParamAlphaNumeric() {
        Map<String, String> exp = new HashMap<>();
        exp.put("a1b2c3", "d4e5f6");

        Map<String, String> act = Helper.retrieveGETData("http://somewebsite.com/index.php?a1b2c3=d4e5f6");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrieveGETDataSingleParamNumericSymbol() {
        Map<String, String> exp = new HashMap<>();
        exp.put("123", "(456)^$");

        Map<String, String> act = Helper.retrieveGETData("http://somewebsite.com/index.php?123=%28456%29%5E%24");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrieveGETDataSingleParamAlphaSymbol() {
        Map<String, String> exp = new HashMap<>();
        exp.put("abc", "(def)^$");

        Map<String, String> act = Helper.retrieveGETData("http://somewebsite.com/index.php?abc=%28def%29%5E%24");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrieveGETDataSingleParamAlphaNumericSymbol() {
        Map<String, String> exp = new HashMap<>();
        exp.put("a1b2c3", "(d4e5)^$");

        Map<String, String> act = Helper.retrieveGETData("http://somewebsite.com/index.php?a1b2c3=%28d4e5%29%5E%24");

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

    @Test
    public void retrieveGETDataMultiParamNumeric() {
        Map<String, String> exp = new HashMap<>();
        exp.put("12", "34");
        exp.put("56", "78");

        Map<String, String> act = Helper.retrieveGETData("http://somewebsite.com/index.php?12=34&56=78");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrieveGETDataMultiParamSymbol() {
        Map<String, String> exp = new HashMap<>();
        exp.put("ab", "()");
        exp.put("cd", "^$");

        Map<String, String> act = Helper.retrieveGETData("http://somewebsite.com/index.php?ab=%28%29&cd=%5E%24");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrieveGETDataMultiParamAlphaNumeric() {
        Map<String, String> exp = new HashMap<>();
        exp.put("a1", "b2");
        exp.put("c3", "d4");

        Map<String, String> act = Helper.retrieveGETData("http://somewebsite.com/index.php?a1=b2&c3=d4");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrieveGETDataMultiParamNumericSymbol() {
        Map<String, String> exp = new HashMap<>();
        exp.put("12", "(34)");
        exp.put("56", "^78$");

        Map<String, String> act = Helper.retrieveGETData("http://somewebsite.com/index.php?12=%2834%29&56=%5E78%24");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrieveGETDataMultiParamAlphaSymbol() {
        Map<String, String> exp = new HashMap<>();
        exp.put("ab", "(cd)");
        exp.put("ef", "^gh$");

        Map<String, String> act = Helper.retrieveGETData("http://somewebsite.com/index.php?ab=%28cd%29&ef=%5Egh%24");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void retrieveGETDataMultiParamAlphaNumericSymbol() {
        Map<String, String> exp = new HashMap<>();
        exp.put("a1b2", "(c3d4)");
        exp.put("e5f6", "^g7h8$");

        Map<String, String> act = Helper.retrieveGETData(
                "http://somewebsite.com/index.php?a1b2=%28c3d4%29&e5f6=%5Eg7h8%24");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void splitParamPairExists() {
        Map<String, String> exp = new HashMap<>();
        exp.put("abc", "def");

        Map<String, String> act = Helper.splitParamPair("abc=def", new HashMap<>());

        Assert.assertEquals(exp, act);
    }

    @Test
    public void splitParamPairNotExists() {
        Map<String, String> exp = new HashMap<>();
        Map<String, String> act = Helper.splitParamPair("abcdef", new HashMap<>());

        Assert.assertEquals(exp, act);
    }
    
    @Test
    public void checkAPIRouteExists() {
        class Sample {}
        class Example {}
        class API {}
        
        List<Class> routes = Arrays.asList(Sample.class, Example.class, API.class);
        List<String> request = Arrays.asList("example", "method");
        
        Optional<Class> act = Helper.checkAPIRoute(routes, request);
        
        Assert.assertTrue(act.isPresent() && act.get().equals(Example.class));
    }

    @Test
    public void checkAPIRouteNotExists() {
        class Sample {}
        class Example {}
        class API {}

        List<Class> routes = Arrays.asList(Sample.class, Example.class, API.class);
        List<String> request = Arrays.asList("hello", "method");

        Optional<Class> act = Helper.checkAPIRoute(routes, request);

        Assert.assertFalse(act.isPresent());
    }
    
    @Test
    public void checkAPISubRouteExists() {
        class Sample {}
        class Example {
            public void method() {}
        }
        class API {}

        List<Class> routes = Arrays.asList(Sample.class, Example.class, API.class);
        List<String> request = Arrays.asList("example", "method");

        Optional<Method> act = Helper.checkAPISubRoute(new Example(), request);

        Assert.assertTrue(act.isPresent());
    }

}
