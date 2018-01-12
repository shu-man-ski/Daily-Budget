package shm.dim.dailybudget;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import shm.dim.dailybudget.adapter.CategoryDataAdapter;
import shm.dim.dailybudget.database.DbHelper;
import shm.dim.dailybudget.dialog_fragment.CreateCategoryDialog;
import shm.dim.dailybudget.model.Category;

public class CategoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mCategoryList;
    private CategoryDataAdapter mCategoryDataAdapter;
    protected List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        this.setTitle("Категории");

        Toolbar toolbar = findViewById(R.id.toolbar_category);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.category_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_category);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCategory();
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = CategoryDataAdapter.getPosition();
        Category category = categoryList.get(position);
        showDialogMessage("Удалить категорию " +
                category.getName() + "?", category);
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onStart() {
        mCategoryList = findViewById(R.id.category_list);
        categoryList = getCategory();
        mCategoryDataAdapter = new CategoryDataAdapter(this, categoryList);
        mCategoryList.setAdapter(mCategoryDataAdapter);
        super.onStart();
    }


    private void showDialogMessage(String msg, final Category category) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCategory(category);
                        onStart();
                    }
                }).create().show();
    }

    protected void deleteCategory(Category category) {
        DbHelper dbHelper = new DbHelper(CategoryActivity.this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete("Category", "NAME = '" + category.getName() + "'", null);
    }

    protected ArrayList<Category> getCategory() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ArrayList<Category> data = new ArrayList<>();

        Cursor cursor = database.rawQuery("select NAME from Category", null);
        if (cursor.moveToFirst()) {
            int indexName = cursor.getColumnIndex("NAME");
            do {
                data.add(new Category(cursor.getString(indexName)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_monthly_report_category:
                startActivity(new Intent(CategoryActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.nav_category_category:
                break;
            case R.id.nav_costs_category:
                startActivity(new Intent(CategoryActivity.this, CostsActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.nav_about_category:
                startActivity(new Intent(CategoryActivity.this, AboutAppActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
        DrawerLayout drawer = findViewById(R.id.category_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createCategory() {
        CreateCategoryDialog dialog = new CreateCategoryDialog();
        dialog.show(getSupportFragmentManager(), null);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.category_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}