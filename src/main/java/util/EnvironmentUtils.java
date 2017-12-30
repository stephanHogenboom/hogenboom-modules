package util;

public class EnvironmentUtils {

    public static String getEnvOrPropertyOrDefault(String name, String def) {
        String variable = System.getenv(name);
        if (variable != null) {
            return variable;
        } else {
            return System.getProperty(name, def);
        }
    }
}
