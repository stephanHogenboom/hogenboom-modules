package main.java.util;

public class Validator {

    public boolean isNumeric(String s){
        return s != null && s.matches("-?\\d+(\\.\\d+)?");
    }

    public void assertNotNull(Object obj, String name) {
        if (obj ==null) {
            throw new IllegalStateException(name + " is not allowed to be empty!");
        }
    }

    public boolean isNumeric(String... strings){
        for (String s : strings) {
            if (!isNumeric(s)) {
                System.out.printf("%s is not numeric %s", s, "\n");
                return false;
            }
        }
        return true;
    }
}
