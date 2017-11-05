package enums;

/**
 * Created by booob on 05.11.17.
 */

public enum Category {
    GRAVES("Groby"),
    FRIENDS("ZNajomi"),
    PARKINGS("Parkingi");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
