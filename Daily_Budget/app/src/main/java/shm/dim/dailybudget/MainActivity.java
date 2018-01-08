package shm.dim.dailybudget;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import shm.dim.dailybudget.PieChart.PieChartView;
import shm.dim.dailybudget.database.DbHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private PieChartView mPieChartView;
    private RecyclerView mMainList;
    private CostsDataAdapter mCostsDataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("Главная | Отчет за месяц");

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_main);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateCostActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        //dbHelper.onUpgrade(database, 1, 1);
    }

    @Override
    protected void onStart() {
        mPieChartView = findViewById(R.id.pie_chart);
        float[] datapoints = getSumCosts();
        drawRoundView(datapoints, 80);

        mMainList = findViewById(R.id.main_list);
        List<Costs> costsList = getCategoryAndSumCosts();
        mCostsDataAdapter = new CostsDataAdapter(this, costsList);
        mMainList.setAdapter(mCostsDataAdapter);

        super.onStart();
    }


    protected float[] getSumCosts() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        List<Float> listResult = new ArrayList<>();

        String query = "select CATEGORY, sum(COST) sum from Costs group by CATEGORY order by CATEGORY asc;";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int sumIndex = cursor.getColumnIndex("sum");
            do {
                listResult.add(Float.valueOf(cursor.getString(sumIndex)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        float[] result = new float[listResult.size()];
        for(int i = 0; i < listResult.size(); i++)
            result[i] = listResult.get(i);

        return result;
    }

    protected ArrayList<Costs> getCategoryAndSumCosts() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ArrayList<Costs> data = new ArrayList<>();

        String query = "select CATEGORY, sum(co.COST) sum from Costs co, Category ca "
                + "where co.CATEGORY = ca.NAME group by CATEGORY order by sum desc;";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int categoryIndex = cursor.getColumnIndex("CATEGORY");
            int sumIndex = cursor.getColumnIndex("sum");
            do {
                data.add(new Costs(data.size(), cursor.getString(categoryIndex), cursor.getString(sumIndex)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_monthly_report_main:
                onBackPressed();
                break;
            case R.id.nav_category_main:
                startActivity(new Intent(MainActivity.this, CategoryActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.nav_costs_main:
                startActivity(new Intent(MainActivity.this, CostsActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.nav_about_main:
                startActivity(new Intent(MainActivity.this, AboutAppActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void drawRoundView(float[] datapoints, int wight) {
        mPieChartView.setWidth(wight);
        mPieChartView.setDataPoints(datapoints);
        mPieChartView.invalidate();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}