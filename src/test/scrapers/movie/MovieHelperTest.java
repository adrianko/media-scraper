package test.scrapers.movie;

import main.scrapers.movie.MovieHelper;
import org.junit.Assert;
import org.junit.Test;

public class MovieHelperTest {

    @Test
    public void sanitizeStringNoClean() {
        String exp = "aa0bb1cc";
        String act = MovieHelper.sanitizeString("Aa0Bb1Cc");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void sanitizeStringSpaces() {
        String exp = "a-a-0-b-b-1-c-c";
        String act = MovieHelper.sanitizeString("a a 0 b b 1 c c");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void sanitizeStringDashPrefix() {
        String exp = "aaaa";
        String act = MovieHelper.sanitizeString("  aaaa");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void sanitizeStringDashSuffix() {
        String exp = "aaaa";
        String act = MovieHelper.sanitizeString("aaaa    ");

        Assert.assertEquals(exp, act);
    }

}