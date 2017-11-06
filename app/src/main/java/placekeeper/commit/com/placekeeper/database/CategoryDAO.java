package placekeeper.commit.com.placekeeper.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import placekeeper.commit.com.placekeeper.dto.CategoryData;

/**
 * Created by booob on 05.11.17.
 */

public class CategoryDAO extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "PlaceKeeper.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CategoryData.TABLE_NAME + " (" +
                    CategoryData._ID + " INTEGER PRIMARY KEY," +
                    CategoryData.COLUMN_NAME_NAME + " TEXT," +
                    CategoryData.COLUMN_NAME_COLOR + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CategoryData.TABLE_NAME;


    public CategoryDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public Long save(CategoryData categoryData) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CategoryData.COLUMN_NAME_NAME, categoryData.getName());
        values.put(CategoryData.COLUMN_NAME_COLOR, categoryData.getColor());

        long id = db.insert(CategoryData.TABLE_NAME, null, values);

        return id;
    }

    public List<CategoryData> findAll() {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                CategoryData._ID,
                CategoryData.COLUMN_NAME_NAME,
                CategoryData.COLUMN_NAME_COLOR,
        };

        Cursor cursor = db.query(
                CategoryData.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<CategoryData> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(CategoryData._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(CategoryData.COLUMN_NAME_NAME));
            int color = cursor.getInt(cursor.getColumnIndexOrThrow(CategoryData.COLUMN_NAME_COLOR));

            items.add(new CategoryData(itemId, name, color));
        }
        cursor.close();
        db.close();
        return items;
    }

    public void delete(Long id) {
        SQLiteDatabase db = getWritableDatabase();

        String selection = CategoryData._ID + " = ?";
        String[] selectionArgs = {id.toString()};
        db.delete(CategoryData.TABLE_NAME, selection, selectionArgs);

        db.close();
    }
}
