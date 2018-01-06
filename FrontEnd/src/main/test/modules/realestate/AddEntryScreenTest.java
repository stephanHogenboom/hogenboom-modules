package main.test.modules.realestate;

import main.java.modules.realestate.AddEntryScreen;
import org.junit.Assert;
import org.junit.Test;

public class AddEntryScreenTest {

    @Test
    public void testPostalCodeValidation() {
        AddEntryScreen screen = new AddEntryScreen();
        Assert.assertTrue(screen.isValidPostalCode("1111RD"));
        Assert.assertTrue(screen.isValidPostalCode("2273 RN"));
        Assert.assertFalse(screen.isValidPostalCode("1234"));
        Assert.assertFalse(screen.isValidPostalCode("123423"));
        Assert.assertFalse(screen.isValidPostalCode("1234 23"));
        Assert.assertFalse(screen.isValidPostalCode("1234 T3"));
        Assert.assertFalse(screen.isValidPostalCode("T234 DD"));
        Assert.assertFalse(screen.isValidPostalCode("1234 2T"));
    }
}
