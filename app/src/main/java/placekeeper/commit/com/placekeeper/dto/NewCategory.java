package placekeeper.commit.com.placekeeper.dto;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import placekeeper.commit.com.placekeeper.EntryDetails;
import placekeeper.commit.com.placekeeper.R;
import placekeeper.commit.com.placekeeper.database.CategoryDAO;

public class NewCategory extends AppCompatActivity {

    private int randomColor = 456798;
    private Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setBtnListeners();
    }

    private void setBtnListeners() {
        FloatingActionButton saveCategoryBtn = (FloatingActionButton) findViewById(R.id.saveCategory);
        saveCategoryBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CategoryDAO categoryDAO = new CategoryDAO(getApplicationContext());

                TextView nameCategoryNameTextView = (TextView) findViewById(R.id.categoryName);
                categoryDAO.save(new CategoryData(nameCategoryNameTextView.getText().toString(), randomColor));

                Toast.makeText(NewCategory.this, "Zapisano kategorię", Toast.LENGTH_SHORT).show();
            }
        });

        final Button randomBtn = findViewById(R.id.randomColor);
        randomBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ImageView colorTester = findViewById(R.id.colorCheck);
                int color = rand.nextInt();
                colorTester.setBackgroundColor(color);

                randomColor = color;
            }
        });
    }
}
