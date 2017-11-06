package placekeeper.commit.com.placekeeper.dto;

import android.provider.BaseColumns;

/**
 * Created by booob on 05.11.17.
 */

public class CategoryData implements BaseColumns {
    public static final String TABLE_NAME = "category_data";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_COLOR = "color";

    private long id;

    private String name;

    private int color;

    public CategoryData() {
    }

    public CategoryData(long id, String name, int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public CategoryData(String name, int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return name;
    }
}
