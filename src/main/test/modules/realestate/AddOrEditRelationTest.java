package modules.realestate;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AddOrEditRelationTest {

    @Test
    public void testValidEmail() {
        // testing pattern https://stackoverflow.com/questions/8204680/java-regex-email
        AddOrEditRelation screen = new AddOrEditRelation();
        assertTrue(screen.isValidEmail("fagb@jfj.com"));
        assertTrue(screen.isValidEmail("CoolStoryBrah@MomKnowsBest.com"));
        assertFalse(screen.isValidEmail("21946239tg"));
        assertFalse(screen.isValidEmail("IlovePancakes@yahoo.123"));
        assertFalse(screen.isValidEmail("IlovePancakes@yahoo,com"));
        assertFalse(screen.isValidEmail("IlovePancakes@yahoo,COM"));
        assertTrue(screen.isValidEmail("luke@ImYourFather.SW"));
        assertTrue(screen.isValidEmail("luk3@1w4SUrF4th3r.SW"));
    }



}
