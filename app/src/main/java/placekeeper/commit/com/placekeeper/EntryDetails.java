package placekeeper.commit.com.placekeeper;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import enums.Category;
import enums.Extra;
import placekeeper.commit.com.placekeeper.database.EntryDetailsDAO;
import placekeeper.commit.com.placekeeper.dto.EntryData;

public class EntryDetails extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;

    private EntryData entryData;

    private ImageView preview;
    private EditText nameEditText;
    private EditText surnameEditText;
    private FloatingActionButton takePhotoBtn;
    private FloatingActionButton saveBtn;
    private FloatingActionButton provideBtn;
    private FloatingActionButton deleteBtn;
    private Button localizationBtn;

    private EntryDetailsDAO entryDetailsDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_details);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        entryDetailsDAO = new EntryDetailsDAO(getApplicationContext());

        loadViewFields();

        initModel();

        setHeader();

        setBtnListeners();
    }

    private void loadViewFields() {
        preview = findViewById(R.id.preview);
        nameEditText = (EditText) findViewById(R.id.name);
        surnameEditText = (EditText) findViewById(R.id.surname);
        takePhotoBtn = (FloatingActionButton) findViewById(R.id.takePhoto);
        saveBtn = (FloatingActionButton) findViewById(R.id.saveBtn);
        provideBtn = (FloatingActionButton) findViewById(R.id.provide);
        takePhotoBtn = (FloatingActionButton) findViewById(R.id.takePhoto);
        localizationBtn = (Button) findViewById(R.id.localization);
        deleteBtn = findViewById(R.id.delete);
    }

    private void initModel() {
        Bundle extras = getIntent().getExtras();
        Long modelId = (Long) extras.get(Extra.MODE_DATA.toString());

        if (modelId == null) {
            this.entryData = new EntryData();
        } else {
            EntryData model = findModelInDB(modelId);
            this.entryData = model;
            fillFormWith(model);

            switchElementsVisibility();
        }
    }

    private void switchElementsVisibility() {
        takePhotoBtn.setVisibility(View.GONE);
        saveBtn.setVisibility(View.GONE);
        localizationBtn.setVisibility(View.GONE);
        provideBtn.setVisibility(View.VISIBLE);
        deleteBtn.setVisibility(View.VISIBLE);

    }

    private void fillFormWith(EntryData model) {
        preview.setImageBitmap(model.getImageBitmap());
        nameEditText.setText(model.getName());
        surnameEditText.setText(model.getSurname());
    }

    private EntryData findModelInDB(Long modelId) {
        return entryDetailsDAO.findById(modelId);
    }

    private void setBtnListeners() {
        final FloatingActionButton tickBtn = (FloatingActionButton) findViewById(R.id.checkLocation);
        final ProgressBar spinner = (ProgressBar) findViewById(R.id.spinner);

        localizationBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                tickBtn.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    public void run() {
                        if (ActivityCompat.checkSelfPermission(EntryDetails.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(EntryDetails.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }

                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                                EntryDetails.this);
                        Toast.makeText(EntryDetails.this, "Pobrano lokalizację", Toast.LENGTH_SHORT).show();
                        spinner.setVisibility(View.GONE);

                        tickBtn.setVisibility(View.VISIBLE);
                    }
                }, 1500);
            }
        });

        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        provideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entryData.setName(nameEditText.getText().toString());
                entryData.setSurname(surnameEditText.getText().toString());

                entryDetailsDAO.save(entryData);

                Toast.makeText(EntryDetails.this, "Zapisano dane", Toast.LENGTH_SHORT).show();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(EntryDetails.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Usuwanie")
                        .setMessage("Czy na pewno usunąć element?")
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                entryDetailsDAO.delete(entryData.getId());
                                Toast.makeText(EntryDetails.this, "Usunięto dane", Toast.LENGTH_SHORT).show();
                                EntryDetails.this.finish();
                            }

                        })
                        .setNegativeButton("Nie", null)
                        .show();
            }
        });
    }

    private void navigate() {
        if (entryData.getLattitude() != null && entryData.getLongtitude() != null) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + entryData.getLattitude() + "," + entryData.getLongtitude());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } else
            Toast.makeText(getBaseContext(), "Informacja o lokalizacja jest nie dostepna", Toast.LENGTH_SHORT).show();
    }

    private void setHeader() {
        Bundle extras = getIntent().getExtras();
        Category category = (Category) extras.get(Extra.CATEGORY_NAME.toString());

        if (category != null) {
            setTitle(category.toString());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        entryData.setLattitude(String.valueOf(location.getLatitude()));
        entryData.setLongtitude(String.valueOf(location.getLongitude()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int i, Bundle bundle) {
        // do nothing
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Włączono GPS " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Wyłączono GPS " + provider, Toast.LENGTH_SHORT).show();
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            preview.setVisibility(View.VISIBLE);
            preview.setImageBitmap(photo);

            entryData.setImageBitmap(photo);
        }
    }
}
