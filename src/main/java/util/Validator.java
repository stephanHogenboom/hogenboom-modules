package util;

public class Validator {

    public boolean isNumeric(String s){
        return s != null && s.matches("-?\\d+(\\.\\d+)?");
    }

    public void assertAllNotNull(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                System.out.println(obj);
            }
        }

    }
}
