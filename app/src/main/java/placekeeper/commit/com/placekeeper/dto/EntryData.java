package placekeeper.commit.com.placekeeper.dto;

import android.graphics.Bitmap;
import android.provider.BaseColumns;

/**
 * Created by booob on 05.11.17.
 */

public class EntryData implements BaseColumns {

    public static final String TABLE_NAME = "entry_data";
    public static final String COLUMN_NAME_LATTITUDE = "lattitude";
    public static final String COLUMN_NAME_LONGTITUDE = "longtitude";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_SURNAME = "surname";
    public static final String COLUMN_NAME_IMAGE = "image";
    public static final String COLUMN_NAME_CATEGORY = "category";

    private long id;

    private String lattitude;

    private String longtitude;

    private String name;

    private String surname;

    private Bitmap imageBitmap;

    private String category;

    public EntryData() {
    }

    public EntryData(long id, String name, String surname, String lattitude, String longtittude, Bitmap image, String category) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.lattitude = lattitude;
        this.longtitude = longtittude;
        this.imageBitmap = image;
        this.category = category;
    }

    public EntryData(long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public EntryData(long id, String name, String surname, String lattitude, String longtittude, Bitmap image) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.lattitude = lattitude;
        this.longtitude = longtittude;
        this.imageBitmap = image;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return id + " : " + name + " " + surname;
    }
}
