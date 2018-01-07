package shm.dim.dailybudget;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import shm.dim.dailybudget.database.DbHelper;

public class CostsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mCostsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costs);

        this.setTitle("Расходы");

        Toolbar toolbar = findViewById(R.id.toolbar_costs);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.costs_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_costs);
        navigationView.setNavigationItemSelectedListener(this);

        mCostsList = findViewById(R.id.costs_list);
    }

    @Override
    protected void onStart() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getCosts());
        mCostsList.setAdapter(adapter);
        super.onStart();
    }


    protected ArrayList<String> getCosts() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ArrayList<String> data = new ArrayList<>();

        Cursor cursor = database.rawQuery("select * from Costs", null);
        if (cursor.moveToFirst()) {
            int indexCost = cursor.getColumnIndex("COST");
            int indexDate = cursor.getColumnIndex("DATE");
            int indexCategory = cursor.getColumnIndex("CATEGORY");
            do {
                data.add(cursor.getString(indexDate) + " | " +
                         cursor.getString(indexCost) + " | " +
                         cursor.getString(indexCategory));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_monthly_report_costs:
                startActivity(new Intent(CostsActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.nav_category_costs:
                startActivity(new Intent(CostsActivity.this, CategoryActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.nav_costs_costs:
                break;
            case R.id.nav_about_costs:
                startActivity(new Intent(CostsActivity.this, AboutAppActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
        DrawerLayout drawer = findViewById(R.id.costs_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.costs_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}