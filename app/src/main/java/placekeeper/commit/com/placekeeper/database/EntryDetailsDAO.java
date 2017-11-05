package placekeeper.commit.com.placekeeper.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import placekeeper.commit.com.placekeeper.dto.EntryData;

/**
 * Created by booob on 05.11.17.
 */

public class EntryDetailsDAO extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PlaceKeeper.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + EntryData.TABLE_NAME + " (" +
                    EntryData._ID + " INTEGER PRIMARY KEY," +
                    EntryData.COLUMN_NAME_NAME + " TEXT," +
                    EntryData.COLUMN_NAME_SURNAME + " TEXT," +
                    EntryData.COLUMN_NAME_LATTITUDE + " TEXT," +
                    EntryData.COLUMN_NAME_LONGTITUDE + " TEXT," +
                    EntryData.COLUMN_NAME_IMAGE + " BLOB)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + EntryData.TABLE_NAME;


    public EntryDetailsDAO(Context context) {
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

    public Long save(EntryData entryData) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(EntryData.COLUMN_NAME_NAME, entryData.getName());
        values.put(EntryData.COLUMN_NAME_SURNAME, entryData.getSurname());
        values.put(EntryData.COLUMN_NAME_LATTITUDE, entryData.getLattitude());
        values.put(EntryData.COLUMN_NAME_LONGTITUDE, entryData.getLongtitude());

        byte[] byteArray = convertImageToByteArray(entryData);
        values.put(EntryData.COLUMN_NAME_IMAGE, byteArray);

        long id = db.insert(EntryData.TABLE_NAME, null, values);

        return id;
    }

    private byte[] convertImageToByteArray(EntryData entryData) {
        Bitmap imageBitmap = entryData.getImageBitmap();
        if (imageBitmap == null) {
            return new byte[0];
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public List<EntryData> findAll() {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                EntryData._ID,
                EntryData.COLUMN_NAME_NAME,
                EntryData.COLUMN_NAME_SURNAME,
        };

        Cursor cursor = db.query(
                EntryData.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<EntryData> items = new ArrayList<>();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(EntryData._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(EntryData.COLUMN_NAME_NAME));
            String surname = cursor.getString(cursor.getColumnIndexOrThrow(EntryData.COLUMN_NAME_SURNAME));

            items.add(new EntryData(itemId, name, surname));
        }
        cursor.close();
        db.close();
        return items;
    }

    public EntryData findById(Long id) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                EntryData._ID,
                EntryData.COLUMN_NAME_NAME,
                EntryData.COLUMN_NAME_SURNAME,
                EntryData.COLUMN_NAME_LATTITUDE,
                EntryData.COLUMN_NAME_LONGTITUDE,
                EntryData.COLUMN_NAME_IMAGE
        };

        String selection = EntryData._ID + " = ?";
        String[] selectionArgs = { id.toString() };

        Cursor cursor = db.query(
                EntryData.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        List<EntryData> items = new ArrayList<>();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(EntryData._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(EntryData.COLUMN_NAME_NAME));
            String surname = cursor.getString(cursor.getColumnIndexOrThrow(EntryData.COLUMN_NAME_SURNAME));
            String lattitude = cursor.getString(cursor.getColumnIndexOrThrow(EntryData.COLUMN_NAME_LATTITUDE));
            String longtittude = cursor.getString(cursor.getColumnIndexOrThrow(EntryData.COLUMN_NAME_LONGTITUDE));
            byte[] imageBlob = cursor.getBlob(cursor.getColumnIndexOrThrow(EntryData.COLUMN_NAME_IMAGE));

            Bitmap image = BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length);
            items.add(new EntryData(itemId, name, surname, lattitude, longtittude, image));
        }
        cursor.close();
        db.close();

        return items.get(0);
    }

    public void delete(Long id) {
        SQLiteDatabase db = getWritableDatabase();

        String selection = EntryData._ID + " = ?";
        String[] selectionArgs = { id.toString() };
        db.delete(EntryData.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

}
