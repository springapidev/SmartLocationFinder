package com.coderbd.smartlocationfinder;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.coderbd.smartlocationfinder.category.Category;
import com.coderbd.smartlocationfinder.category.CategoryDatabaseHandler;
import com.coderbd.smartlocationfinder.location.LocationData;
import com.coderbd.smartlocationfinder.location.LocationDataAdapter;
import com.coderbd.smartlocationfinder.location.LocationDataDatabaseHandler;

import java.util.List;

public class LocationActivity extends AppCompatActivity {
    ListView listviews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        listviews=findViewById(R.id.location_listview);
        loadData();
        listviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryDatabaseHandler db = new CategoryDatabaseHandler(getApplicationContext());
                db.deleteData(new Category(position + 1));
                Toast.makeText(LocationActivity.this,
                        "Data " + position + " deleted",
                        Toast.LENGTH_SHORT).show();
                loadData();
            }
        });
    }
    private void loadData() {
        LocationDataDatabaseHandler db = new LocationDataDatabaseHandler(getApplicationContext());
        List<LocationData> dataList = db.getAllLocations();

        Toast.makeText(this, "List size: " + dataList.size(),
                Toast.LENGTH_LONG).show();

        LocationDataAdapter adapter = new LocationDataAdapter(this, dataList);
        listviews.setAdapter(adapter);
    }
}