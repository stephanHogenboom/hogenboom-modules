package util;

public class Validator {

    public boolean isNumeric(String s){
        if (s == null) {
            return false;
        }
        else return s.matches("-?\\d+(\\.\\d+)?");
    }

    public void assertAllNotNull(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                throw new IllegalStateException("Members of object cannot be null!");
            }
        }

    }
}
