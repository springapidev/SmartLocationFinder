package com.coderbd.smartlocationfinder;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.coderbd.smartlocationfinder.category.Category;
import com.coderbd.smartlocationfinder.category.CategoryAdapter;
import com.coderbd.smartlocationfinder.category.CategoryDatabaseHandler;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    ListView listviews;
    Button btnAdd;
    EditText cName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        listviews=findViewById(R.id.categorylistview);
        loadData();
        cName = findViewById(R.id.cname);
        btnAdd = findViewById(R.id.btnAddCategory);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = cName.getText().toString();
                if(categoryName != null && categoryName.length() < 3){
                    Toast.makeText(CategoryActivity.this, "Enter Category name", Toast.LENGTH_SHORT).show();
                }else {
                    writeNewUser(categoryName);
                    Toast.makeText(CategoryActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    loadData();
                }
            }
        });
        listviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryDatabaseHandler db = new CategoryDatabaseHandler(getApplicationContext());
                db.deleteData(new Category((int)id ));
                Toast.makeText(CategoryActivity.this,
                        "Data " + id + " deleted",
                        Toast.LENGTH_SHORT).show();
                loadData();
            }
        });
    }

    public void writeNewUser(String name) {
        CategoryDatabaseHandler db = new CategoryDatabaseHandler(getApplicationContext());
        Category category = new Category(name);
        db.addCategory(category);
        System.out.printf("Category Saved Successfully!");
    }
    private void loadData() {
        CategoryDatabaseHandler db = new CategoryDatabaseHandler(getApplicationContext());
        List<Category> dataList = db.getAllCategories();

        Toast.makeText(this, "List size: " + dataList.size(),
                Toast.LENGTH_LONG).show();

        CategoryAdapter adapter = new CategoryAdapter(this, dataList);
        listviews.setAdapter(adapter);
    }
}