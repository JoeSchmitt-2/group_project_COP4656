package com.example.therecipebook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;


public class CustomAdapter extends BaseAdapter {
    private Context mContext;
    RecipeBookContentProvider.MainDatabaseHelper mainDatabaseHelper;
    SQLiteDatabase db;
    private ArrayList<String> ingredients = new ArrayList<String>();
    public CustomAdapter(Context  context,ArrayList<String> ingredients)
    {
        this.mContext = context;
        this.ingredients = ingredients;
    }
    @Override
    public int getCount() {
        return ingredients.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final    viewHolder holder;
        mainDatabaseHelper = new RecipeBookContentProvider.MainDatabaseHelper(mContext);
        LayoutInflater layoutInflater;
        if (convertView == null) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.activity_grocery_list, null);
            holder = new viewHolder();
            holder.ingredients = (TextView) convertView.findViewById(R.id.ingredientTV);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        holder.ingredients.setText(ingredients.get(position));
        return convertView;
    }
    public class viewHolder {
        TextView ingredients;
    }
}
