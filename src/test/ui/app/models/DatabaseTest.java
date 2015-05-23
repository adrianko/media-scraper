package test.ui.app.models;

import main.ui.app.models.Database;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;

public class DatabaseTest {
    
    @Test
    public void getConnectionInstanceType() {
        Connection act = Database.getConnection("settings");

        Assert.assertNotNull(act);
    }
    
}
