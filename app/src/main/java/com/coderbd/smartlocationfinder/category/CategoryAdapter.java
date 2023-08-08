package com.coderbd.smartlocationfinder.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coderbd.smartlocationfinder.R;

import java.util.List;


    public class CategoryAdapter extends BaseAdapter {
        private final Context context;
        private List<Category> categoryList;
        LayoutInflater inflater;
        public CategoryAdapter(Context context, List<Category> categories) {
            this.context = context;
            this.categoryList = categories;

        }

        @Override
        public int getCount() {
            return categoryList.size();
        }

        @Override
        public Object getItem(int i) {
            return categoryList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v=View.inflate(context, R.layout.category_list, null);
            // TextView ids=(TextView)v.findViewById(R.id.view_id);
            TextView name=(TextView)v.findViewById(R.id.view_name);

            // set Text
            // ids.setText(contactList.get(i).getID());
            name.setText(categoryList.get(i).getName());

            return v;
        }
    }
