package placekeeper.commit.com.placekeeper.components;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import placekeeper.commit.com.placekeeper.CategoriesActivity;
import placekeeper.commit.com.placekeeper.R;
import placekeeper.commit.com.placekeeper.dto.CategoryData;

/**
 * Created by booob on 06.11.17.
 */

public class CategoryArrayAdapter extends ArrayAdapter<CategoryData> {

    List<CategoryData> objects = new ArrayList<>();

    public CategoryArrayAdapter(CategoriesActivity categoriesActivity, int category_row_layout, int entry_row, List<CategoryData> allCategories) {
        super(categoriesActivity, category_row_layout, entry_row, allCategories);

        this.objects = allCategories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = View.inflate(getContext(), R.layout.category_row_layout, null);

        CategoryData currentModel = this.objects.get(position);
        view.setBackgroundColor(currentModel.getColor());

        TextView categoryTextView = view.findViewById(R.id.category_entry_row);
        categoryTextView.setText(currentModel.getName());

        return view;
    }
}
