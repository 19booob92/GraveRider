package placekeeper.commit.com.placekeeper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow.LayoutParams;

import java.util.List;

import enums.Category;
import enums.Extra;
import placekeeper.commit.com.placekeeper.database.EntryDetailsDAO;
import placekeeper.commit.com.placekeeper.dto.EntryData;

public class EntryListActivity extends ListActivity {

    private ArrayAdapter<EntryData> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_list);

        fillList();

        setHeader();

        addBtnListeneres();
    }

    private void fillList() {
        EntryDetailsDAO entryDetailsDAO = new EntryDetailsDAO(getApplicationContext());
        List<EntryData> allEntryData = entryDetailsDAO.findAll();

        adapter = new ArrayAdapter<EntryData>(this, R.layout.data_row_layout, R.id.entry_row, allEntryData);
        setListAdapter(adapter);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        registerForContextMenu(getListView());
    }

    private void setHeader() {
        Bundle extras = getIntent().getExtras();
        Category category = (Category) extras.get(Extra.CATEGORY_NAME.toString());

        if (category != null) {
            setTitle(category.toString());
        }
    }

    private void addBtnListeneres() {
        FloatingActionButton addItemBtn = (FloatingActionButton) findViewById(R.id.addItem);
        addItemBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent entryDetails = new Intent(EntryListActivity.this, EntryDetails.class);
                entryDetails.putExtra(Extra.CATEGORY_NAME.toString(), Category.GRAVES);
                startActivity(entryDetails);
            }
        });
    }

    @Override
    protected void onListItemClick(android.widget.ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        EntryData selectedItem = (EntryData) getListView().getItemAtPosition(position);

        Intent entryDetails = new Intent(EntryListActivity.this, EntryDetails.class);
        entryDetails.putExtra(Extra.CATEGORY_NAME.toString(), Category.GRAVES);
        entryDetails.putExtra(Extra.MODE_DATA.toString(), selectedItem.getId());
        startActivity(entryDetails);
    }

    @Override
    protected void onResume() {
        super.onResume();

        fillList();
    }
}
