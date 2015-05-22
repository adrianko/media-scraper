package test.ui.core.components;

import main.ui.core.components.Helper;
import org.junit.Assert;
import org.junit.Test;

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
    
}
