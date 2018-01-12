package shm.dim.dailybudget;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import shm.dim.dailybudget.database.DbHelper;

public class CreateCategoryActivity extends AppCompatActivity {

    EditText mCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_category);

        mCategoryName = findViewById(R.id.category_name);
    }

    public void addCategory(View view) {
        String categoryName = mCategoryName.getText().toString();
        if (!categoryName.isEmpty()) {
            DbHelper dbHelper = new DbHelper(this);
            SQLiteDatabase database = dbHelper.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put("NAME", categoryName);
            database.insert("Category", null, cv);
            super.onBackPressed();
        } else {
            Toast.makeText(CreateCategoryActivity.this, "Введите название категории", Toast.LENGTH_SHORT).show();
        }
    }
}