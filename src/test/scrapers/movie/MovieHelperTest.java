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
    public void sanitizeStringSpacePrefix() {
        String exp = "aaaa";
        String act = MovieHelper.sanitizeString("  aaaa");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void sanitizeStringSpaceSuffix() {
        String exp = "aaaa";
        String act = MovieHelper.sanitizeString("aaaa    ");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void sanitizeStringSpacePrefixSuffix() {
        String exp = "aaaa";
        String act = MovieHelper.sanitizeString("    aaaa    ");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void sanitizeStringSymbolBetween() {
        String exp = "aa-aa";
        String act = MovieHelper.sanitizeString("aa:aa");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void sanitizeStringDoubleSymbolBetween() {
        String exp = "aa-aa";
        String act = MovieHelper.sanitizeString("aa:;aa");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void sanitizeStringSymbolBetweenPrefix() {
        String exp = "aa-aa";
        String act = MovieHelper.sanitizeString("(aa:;aa");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void sanitizeStringSymbolBetweenSuffix() {
        String exp = "aa-aa";
        String act = MovieHelper.sanitizeString("aa:;aa)");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void sanitizeStringSymbolBetweenPrefixSuffix() {
        String exp = "aa-aa";
        String act = MovieHelper.sanitizeString("(aa:;aa)");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void sanitizeStringTitleNoSymbols() {
        String exp = "some-title-2009";
        String act = MovieHelper.sanitizeString("some title (2009)");

        Assert.assertEquals(exp, act);
    }

    @Test
    public void sanitizeStringTitleSymbols() {
        String exp = "some-title-2-another-title-2010";
        String act = MovieHelper.sanitizeString("Some Title 2: Another Title (2010)");

        Assert.assertEquals(exp, act);
    }

}