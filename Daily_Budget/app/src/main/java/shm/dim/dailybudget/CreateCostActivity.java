package shm.dim.dailybudget;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import shm.dim.dailybudget.database.DbHelper;

public class CreateCostActivity extends AppCompatActivity {

    EditText mCost;
    Spinner mCategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_costs);

        mCost = findViewById(R.id.cost);
        mCategoryList = findViewById(R.id.category_list_spinner);
    }

    @Override
    protected void onStart() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                getCategory());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategoryList.setAdapter(adapter);
        super.onStart();
    }

    protected ArrayList<String> getCategory() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ArrayList<String> data = new ArrayList<>();

        Cursor cursor = database.rawQuery("select NAME from Category", null);
        if (cursor.moveToFirst()) {
            int indexName = cursor.getColumnIndex("NAME");
            do {
                data.add(cursor.getString(indexName));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }


    public void addCost(View view) {
        String cost = mCost.getText().toString();
        String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        String selectedCategory = mCategoryList.getSelectedItem().toString();

        if (!cost.isEmpty()) {
            DbHelper dbHelper = new DbHelper(this);
            SQLiteDatabase database = dbHelper.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put("COST", cost);
            cv.put("DATE", date);
            cv.put("CATEGORY", selectedCategory);

            database.insert("Costs", null, cv);
            super.onBackPressed();
        } else {
            Toast.makeText(CreateCostActivity.this, "Введите название категории", Toast.LENGTH_SHORT).show();
        }
    }
}