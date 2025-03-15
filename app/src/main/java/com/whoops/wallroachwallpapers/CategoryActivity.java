package com.whoops.wallroachwallpapers;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;


import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CategoryRecyclerAdapter recyclerAdapter;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private GridLayoutManager gridLayoutManager;
    private SwipeRefreshLayout swiper;
    private ArrayList<StorageReference> references;

    private AdView adView;

    public boolean getConnectivityStatus(){
        ConnectivityManager cm=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNet=cm.getActiveNetworkInfo();
        return activeNet!=null&&activeNet.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        String s=intent.getExtras().getString("CATEGORY_NAME");
        switch (s) {
            case "space":
                references = MainActivity.space_refs_thumb;
                break;
            case "nature":
                references=MainActivity.nature_refs_thumb;
                break;
            case "abstract":
                references=MainActivity.abstract_refs_thumb;
                break;
            case "landscape":
                references=MainActivity.landscape_refs_thumb;
                break;
            case "cars":
                references=MainActivity.car_refs_thumb;
                break;
            case "minimal":
                references=MainActivity.minimal_refs_thumb;
                break;
            case "material":
                references=MainActivity.material_refs_thumb;
                break;
            case "black":
                references=MainActivity.black_refs_thumb;
                break;
            case "misc":
                references=MainActivity.funny_refs_thumb;
                break;
            case "animals":
                references=MainActivity.animal_refs_thumb;
                break;
            case "amoled":
                references=MainActivity.amoled_refs_thumb;
                break;
            case "motivational":
                references=MainActivity.motivational_refs_thumb;
                break;
            case "superhero":
                references=MainActivity.superhero_refs_thumb;
                break;
            case "city":
                references=MainActivity.city_refs_thumb;
                break;
        }

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(s.toUpperCase());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setSubtitle("All the HD Wallpapers for "+s.toUpperCase());

        if (getConnectivityStatus()) {

            setContentView(R.layout.activity_category);
            swiper = (SwipeRefreshLayout) findViewById(R.id.category_swiper);
            recyclerView= (RecyclerView)findViewById(R.id.category_recycler);
            progressBar= (ProgressBar) findViewById(R.id.category_progressBar);
            gridLayoutManager=new GridLayoutManager(this,2, LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerAdapter=new CategoryRecyclerAdapter(this,references,progressBar);
            recyclerView.setAdapter(recyclerAdapter);

            adView=(AdView)findViewById(R.id.ad_category_activity);
            adView.loadAd(new AdRequest.Builder().build());
            Log.d("AD-LOADzED","AD has bEEn LoAdzEd");






            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (recyclerAdapter!=null) {

                        //Snackbar.make(new View(getApplicationContext()),"You have the latest Wallpapers!",Snackbar.LENGTH_LONG);

                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swiper.setRefreshing(false);
                        }
                    },2000);


                }
            });

        }else{
            setContentView(R.layout.not_connected_layout);
        }





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return true;
    }
}
