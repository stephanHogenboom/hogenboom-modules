package util;

import util.ValidatorKotlin;
import org.junit.Assert;
import org.junit.Test;

public class ValidatorTest {

    @Test
    public void testIsNumeric() {
        ValidatorKotlin val = new ValidatorKotlin();
        Assert.assertTrue(val.isNumeric("1233234345"));
        Assert.assertTrue(val.isNumeric("12332.34345"));
        Assert.assertTrue(val.isNumeric("1233234345"));
        Assert.assertFalse(val.isNumeric("h"));
        Assert.assertFalse(val.isNumeric("123323,4345"));
        Assert.assertFalse(val.isNumeric("123323h345"));
        Assert.assertFalse(val.isNumeric("1233_23_4.345"));

        Assert.assertTrue(val.isNumeric("1233234345", "1212343", "132143.2145"));
        Assert.assertFalse(val.isNumeric("123323F4345", "1212343", "132143.2145"));
    }
}
