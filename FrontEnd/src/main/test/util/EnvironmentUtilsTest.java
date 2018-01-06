package main.test.util;



import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static main.java.util.EnvironmentUtils.getEnvOrPropertyOrDefault;

public class EnvironmentUtilsTest {

    @Test
    public void testGetEnvironmentPropertyOrDefault() {
        Properties props = new Properties();
        props.setProperty("BASE_URL", "localhost:8080");
        System.setProperties(props);
        Assert.assertThat(getEnvOrPropertyOrDefault("BASE_URL", "dd"), is("localhost:8080"));
        Assert.assertThat(getEnvOrPropertyOrDefault("BASE_URL", "dd"), not("localhost:8090"));
    }
}
