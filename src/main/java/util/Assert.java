package util;

public class Assert {
    public static void notNull(Object o) {
        if (o == null) try {
            throw new IllegalAccessException("Reference must not be null!");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
