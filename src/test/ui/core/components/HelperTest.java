package test.ui.core.components;

import main.ui.core.components.Helper;
import org.junit.Assert;
import org.junit.Test;

public class HelperTest {
    
    @Test
    public void sha1Password() {
        String exp = "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8";
        String act = Helper.sha1("password");

        Assert.assertEquals(exp, act);
    }
    
    @Test
    public void sha1S9H3b3H0Lk() {
        String exp = "d33b97f878d22fbf0b978b26ea04a8d2f544e7f9";
        String act = Helper.sha1("S9H3b3H0Lk");
        
        Assert.assertEquals(exp, act);
    }
    
}
