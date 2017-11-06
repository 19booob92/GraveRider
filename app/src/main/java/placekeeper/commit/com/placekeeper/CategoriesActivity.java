package placekeeper.commit.com.placekeeper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TableRow;

import java.util.List;

import enums.Category;
import enums.Extra;
import placekeeper.commit.com.placekeeper.components.CategoryArrayAdapter;
import placekeeper.commit.com.placekeeper.database.CategoryDAO;
import placekeeper.commit.com.placekeeper.dto.CategoryData;
import placekeeper.commit.com.placekeeper.dto.EntryData;
import placekeeper.commit.com.placekeeper.dto.NewCategory;

public class CategoriesActivity extends ListActivity {

    private ArrayAdapter<CategoryData> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_place);

        fillList();

        addBtnListeners();
    }

    private void fillList() {
        CategoryDAO categoryDAO = new CategoryDAO(getApplicationContext());

        try {
            List<CategoryData> allCategories = categoryDAO.findAll();

            adapter = new CategoryArrayAdapter(this, R.layout.category_row_layout, R.id.category_entry_row, allCategories);
            setListAdapter(adapter);

            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            registerForContextMenu(getListView());
        } catch (Exception ex) {
            Log.e(ex.getMessage(), ex.getLocalizedMessage());
        }
    }


    private void addBtnListeners() {
        FloatingActionButton addItemBtn = (FloatingActionButton) findViewById(R.id.newCategory);
        addItemBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent newCategory = new Intent(CategoriesActivity.this, NewCategory.class);
                startActivity(newCategory);

//                Intent entryList = new Intent(CategoriesActivity.this, EntryListActivity.class);
//                entryList.putExtra(Extra.CATEGORY_NAME.toString(), Category.GRAVES);
//                startActivity(entryList);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        fillList();
    }

    @Override
    protected void onListItemClick(android.widget.ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        CategoryData selectedItem = (CategoryData) getListView().getItemAtPosition(position);

        Intent entryList = new Intent(CategoriesActivity.this, EntryListActivity.class);
        entryList.putExtra(Extra.CATEGORY_NAME.toString(), selectedItem.getName());
        startActivity(entryList);
    }
}
