package com.whoops.wallroachwallpapers;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.*;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SectionsPageAdapter mSectionsPageAdapter;

    private static RecyclerView recyclerView;


    private volatile boolean isConnected;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };



    private static GridLayoutManager gridLayoutManager;
    private static  RecyclerViewAdapter recyclerViewAdapter;
    public static RecyclerView recyclerView_premium;
    public static PremiumTabRecyclerAdapter premiumTabRecyclerAdapter;
    public static CategoryTabRecyclerAdapter categoryTabRecyclerAdapter;
    public static RecyclerView category;

    private static StorageReference reference;

    public static final String TAG=MainActivity.class.getSimpleName();

    private DatabaseReference databaseReference;

    public static ArrayList<String> image_names;
    public static ArrayList<StorageReference> pathrefs;
    public static ArrayList<StorageReference> space_refs_thumb,nature_refs_thumb,car_refs_thumb,technology_refs_thumb,landscape_refs_thumb,abstract_refs_thumb,material_refs_thumb,minimal_refs_thumb;
    public static ArrayList<StorageReference> black_refs,black_refs_thumb,funny_refs,funny_refs_thumb,animal_refs,amoled_refs;
    public static ArrayList<StorageReference> animal_refs_thumb,amoled_refs_thumb,motivational_refs,motivational_refs_thumb,superhero_refs,superhero_refs_thumb;
    public static ArrayList<StorageReference> pathrefsthumb,city_refs_thumb;
    public static ArrayList<StorageReference> premium_wall_refs;

    //Ads
    //private AdView adView;
    private AlertDialog.Builder exitDialog;





    public boolean getConnectivityStatus(){
        ConnectivityManager cm=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNet=cm.getActiveNetworkInfo();
        return activeNet!=null&&activeNet.isConnectedOrConnecting();
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Explore");

        //the tab setting up section...
        tabLayout=(TabLayout)findViewById(R.id.tabs);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        mSectionsPageAdapter=new SectionsPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mSectionsPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1,true);

        View view=findViewById(R.id.GO_PRO_FOOTER);
        if (view!=null)view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri =Uri.parse("market://details?id=com.whoops.bestgame2018.gameoftheyear.ketchup.ohmyballs");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW,uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_NEW_DOCUMENT|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                }catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://play.google.com/store/apps/details?id=com.whoops.bestgame2018.gameoftheyear.ketchup.ohmyballs")));
                }
            }
        });

        verifyStoragePermissions(this);


        //nav list view

        isConnected=getConnectivityStatus();
            //inialize mobileads


            //MobileAds.initialize(this, "ca-app-pub-6967138802491752~1261033133");


            //databaseReference= FirebaseDatabase.getInstance().getReference();
            image_names = new ArrayList<>();



            car_refs_thumb = new ArrayList<>();
            pathrefsthumb = new ArrayList<>();
            black_refs_thumb = new ArrayList<>();
            technology_refs_thumb = new ArrayList<>();
            minimal_refs_thumb = new ArrayList<>();
            material_refs_thumb = new ArrayList<>();
            space_refs_thumb = new ArrayList<>();
            nature_refs_thumb = new ArrayList<>();
            landscape_refs_thumb = new ArrayList<>();
            funny_refs_thumb = new ArrayList<>();
            abstract_refs_thumb = new ArrayList<>();
            premium_wall_refs=new ArrayList<>();
            animal_refs_thumb=new ArrayList<>();
            amoled_refs_thumb=new ArrayList<>();
            motivational_refs_thumb=new ArrayList<>();
            superhero_refs_thumb=new ArrayList<>();
            city_refs_thumb=new ArrayList<>();

        if (isConnected) {


            //recyclerViewAdapter=new RecyclerViewAdapter(RecentlyAdded.getviewContext(),MainActivity.reference,MainActivity.image_names,MainActivity.pathrefs);
            databaseReference = FirebaseDatabase.getInstance().getReference();
            reference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://walle-f113c.appspot.com");



       /*for(int a=39;a<54;a++){

            /*if(a==44||a==45){
                databaseReference.push().child("imagename").setValue("abstract_premium_" + a + "_thumb.jpg");
            } else{
                databaseReference.push().child("imagename").setValue("funny_" + a + "_thumb.jpg");
            //}

        }*/





            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    image_names.add((String) dataSnapshot.child("imagename").getValue());
                    if (image_names.get(image_names.size() - 1).contains("space")) {
                        if (image_names.get(image_names.size() - 1).contains("thumb")) {
                            space_refs_thumb.add(reference.child("space/" + image_names.get(image_names.size() - 1)));
                            pathrefsthumb.add(reference.child("space/" + image_names.get(image_names.size() - 1)));
                        }
                        if (image_names.get(image_names.size() - 1).contains("premium")){
                            premium_wall_refs.add(reference.child("space/" + image_names.get(image_names.size() - 1)));
                        }

                    } else if (image_names.get(image_names.size() - 1).contains("nature")) {
                        if (image_names.get(image_names.size() - 1).contains("thumb")) {
                            nature_refs_thumb.add(reference.child("nature/" + image_names.get(image_names.size() - 1)));
                            pathrefsthumb.add(reference.child("nature/" + image_names.get(image_names.size() - 1)));
                        }
                        if (image_names.get(image_names.size() - 1).contains("premium")){
                            premium_wall_refs.add(reference.child("nature/" + image_names.get(image_names.size() - 1)));
                        }
                    } else if (image_names.get(image_names.size() - 1).contains("cars")) {
                        if (image_names.get(image_names.size() - 1).contains("thumb")) {
                            car_refs_thumb.add(reference.child("cars/" + image_names.get(image_names.size() - 1)));
                            pathrefsthumb.add(reference.child("cars/" + image_names.get(image_names.size() - 1)));
                        }
                        if (image_names.get(image_names.size() - 1).contains("premium")){
                            premium_wall_refs.add(reference.child("cars/" + image_names.get(image_names.size() - 1)));
                        }
                    }else if (image_names.get(image_names.size() - 1).contains("city")) {
                        if (image_names.get(image_names.size() - 1).contains("thumb")) {
                            city_refs_thumb.add(reference.child("city/" + image_names.get(image_names.size() - 1)));
                            pathrefsthumb.add(reference.child("city/" + image_names.get(image_names.size() - 1)));
                        }
                        if (image_names.get(image_names.size() - 1).contains("premium")){
                            premium_wall_refs.add(reference.child("city/" + image_names.get(image_names.size() - 1)));
                        }
                    } else if (image_names.get(image_names.size() - 1).contains("landscape")) {
                        if (image_names.get(image_names.size() - 1).contains("thumb")) {
                            landscape_refs_thumb.add(reference.child("landscape/" + image_names.get(image_names.size() - 1)));
                            pathrefsthumb.add(reference.child("landscape/" + image_names.get(image_names.size() - 1)));
                        }
                        if (image_names.get(image_names.size() - 1).contains("premium")){
                            premium_wall_refs.add(reference.child("landscape/" + image_names.get(image_names.size() - 1)));
                        }
                    } else if (image_names.get(image_names.size() - 1).contains("abstract")) {
                        if (image_names.get(image_names.size() - 1).contains("thumb")) {
                            abstract_refs_thumb.add(reference.child("abstract/" + image_names.get(image_names.size() - 1)));
                            pathrefsthumb.add(reference.child("abstract/" + image_names.get(image_names.size() - 1)));
                        }
                        if (image_names.get(image_names.size() - 1).contains("premium")){
                            premium_wall_refs.add(reference.child("abstract/" + image_names.get(image_names.size() - 1)));
                        }
                    } else if (image_names.get(image_names.size() - 1).contains("minimal")) {
                        if (image_names.get(image_names.size() - 1).contains("thumb")) {
                            minimal_refs_thumb.add(reference.child("minimal/" + image_names.get(image_names.size() - 1)));
                            pathrefsthumb.add(reference.child("minimal/" + image_names.get(image_names.size() - 1)));
                        }
                        if (image_names.get(image_names.size() - 1).contains("premium")){
                            premium_wall_refs.add(reference.child("minimal/" + image_names.get(image_names.size() - 1)));
                        }
                    } else if (image_names.get(image_names.size() - 1).contains("material")) {
                        if (image_names.get(image_names.size() - 1).contains("thumb")) {
                            material_refs_thumb.add(reference.child("material/" + image_names.get(image_names.size() - 1)));
                            pathrefsthumb.add(reference.child("material/" + image_names.get(image_names.size() - 1)));
                        }
                        if (image_names.get(image_names.size() - 1).contains("premium")){
                            premium_wall_refs.add(reference.child("material/" + image_names.get(image_names.size() - 1)));
                        }
                    } else if (image_names.get(image_names.size() - 1).contains("black")) {

                        if (image_names.get(image_names.size() - 1).contains("thumb")) {
                            black_refs_thumb.add(reference.child("black/" + image_names.get(image_names.size() - 1)));
                            pathrefsthumb.add(reference.child("black/" + image_names.get(image_names.size() - 1)));
                        }
                        if (image_names.get(image_names.size() - 1).contains("premium")){
                            premium_wall_refs.add(reference.child("black/" + image_names.get(image_names.size() - 1)));
                        }
                    }else if (image_names.get(image_names.size() - 1).contains("funny")) {
                        if (image_names.get(image_names.size() - 1).contains("thumb")) {
                            funny_refs_thumb.add(reference.child("funny/" + image_names.get(image_names.size() - 1)));
                            pathrefsthumb.add(reference.child("funny/" + image_names.get(image_names.size() - 1)));
                        }
                        if (image_names.get(image_names.size() - 1).contains("premium")){
                            premium_wall_refs.add(reference.child("funny/" + image_names.get(image_names.size() - 1)));
                        }
                    }else if (image_names.get(image_names.size()-1).contains("animal")){
                        if (image_names.get(image_names.size()-1).contains("thumb")){
                            animal_refs_thumb.add(reference.child("animal/"+image_names.get(image_names.size()-1)));
                            pathrefsthumb.add(reference.child("animal/"+image_names.get(image_names.size()-1)));
                        }
                        if (image_names.get(image_names.size() - 1).contains("premium")){
                            premium_wall_refs.add(reference.child("animal/" + image_names.get(image_names.size() - 1)));
                        }
                    }else if (image_names.get(image_names.size()-1).contains("amoled")){
                        if (image_names.get(image_names.size()-1).contains("thumb")){
                            amoled_refs_thumb.add(reference.child("amoled/"+image_names.get(image_names.size()-1)));
                            pathrefsthumb.add(reference.child("amoled/"+image_names.get(image_names.size()-1)));
                        }
                        if (image_names.get(image_names.size() - 1).contains("premium")){
                            premium_wall_refs.add(reference.child("amoled/" + image_names.get(image_names.size() - 1)));
                        }
                    }else if (image_names.get(image_names.size()-1).contains("motivational")){
                        if (image_names.get(image_names.size()-1).contains("thumb")) {
                            motivational_refs_thumb.add(reference.child("motivational/" + image_names.get(image_names.size() - 1)));
                            pathrefsthumb.add(reference.child("motivational/" + image_names.get(image_names.size() - 1)));
                        }
                        if (image_names.get(image_names.size() - 1).contains("premium")){
                            premium_wall_refs.add(reference.child("motivational/" + image_names.get(image_names.size() - 1)));
                        }
                    }else if (image_names.get(image_names.size()-1).contains("superhero")){
                        if (image_names.get(image_names.size()-1).contains("thumb")) {
                            superhero_refs_thumb.add(reference.child("superhero/" + image_names.get(image_names.size() - 1)));
                            pathrefsthumb.add(reference.child("superhero/" + image_names.get(image_names.size() - 1)));
                        }
                        if (image_names.get(image_names.size() - 1).contains("premium")){
                            premium_wall_refs.add(reference.child("superhero/" + image_names.get(image_names.size() - 1)));
                        }
                    }
                    if (recyclerViewAdapter != null && recyclerView != null) {
                        recyclerViewAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(recyclerViewAdapter);
                    }
                    if (premiumTabRecyclerAdapter!=null&&recyclerView_premium!=null){
                        premiumTabRecyclerAdapter.notifyDataSetChanged();
                        recyclerView_premium.setAdapter(premiumTabRecyclerAdapter);
                    }
                    if (categoryTabRecyclerAdapter!=null&&category!=null){
                        categoryTabRecyclerAdapter.notifyDataSetChanged();
                        category.setAdapter(categoryTabRecyclerAdapter);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });






            /*adView=(AdView) findViewById(R.id.navigation_ad);
            AdRequest adRequest=new AdRequest.Builder().build();
            adView.loadAd(adRequest);*/
        }





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);

        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        }else if (viewPager.getCurrentItem()!=1){
            viewPager.setCurrentItem(1);
        }else  {

                exitDialog=new AlertDialog.Builder(this);
                exitDialog.setTitle("Are you sure?")
                        .setMessage(getResources().getString(R.string.app_rate_persuade))
                        .setPositiveButton("QUIT", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.super.onBackPressed();
                            }
                        })
                        .setNegativeButton("RATE NOW", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Uri uri =Uri.parse("market://details?id="+getPackageName());
                                Intent goToMarket = new Intent(Intent.ACTION_VIEW,uri);
                                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_NEW_DOCUMENT|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                try {
                                    startActivity(goToMarket);
                                }catch (ActivityNotFoundException e){
                                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://play.google.com/store/apps/details?id="+getPackageName())));
                                }
                            }
                        }).show();



        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }else if (id==R.id.action_share){
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT,"Check out this usefull Wallpapers App with Ultra HD quality walls, updated everyday with around 10000+ wallpapers on a single-click. IN ONLY 3 MB APP SIZE "+" http://play.google.com/store/apps/details?id="+getPackageName());
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent,"SHARE"));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i=new Intent(this, CategoryActivity.class);

        if (id == R.id.settings) {
            Intent intent=new Intent(this,SettingsActivity.class);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(intent);

        }else if (id==R.id.SPACE){
            i.putExtra("CATEGORY_NAME","space");

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(i);



        }else if (id==R.id.MATERIAL){
            i.putExtra("CATEGORY_NAME","material");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(i);

        }else if (id==R.id.MINIMAL){
            i.putExtra("CATEGORY_NAME","minimal");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(i);

        }else if (id==R.id.CARS){
            i.putExtra("CATEGORY_NAME","cars");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(i);

        }else if (id==R.id.CITY){
            i.putExtra("CATEGORY_NAME","city");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(i);

        }else if (id==R.id.LANDSCAPES){
            i.putExtra("CATEGORY_NAME","landscape");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(i);

        }else if (id==R.id.ABSTRACT){
            i.putExtra("CATEGORY_NAME","abstract");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(i);

        }else if (id==R.id.BLACK){
            i.putExtra("CATEGORY_NAME","black");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(i);

        }else if (id==R.id.MISC){

            i.putExtra("CATEGORY_NAME","misc");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(i);

        }else if (id==R.id.ANIMALS){
            i.putExtra("CATEGORY_NAME","animals");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(i);
        }else if (id==R.id.AMOLED){
            i.putExtra("CATEGORY_NAME","amoled");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(i);
        }else if (id==R.id.MOTIVATIONAL){
            i.putExtra("CATEGORY_NAME","motivational");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(i);
        }else if (id==R.id.SUPERHERO){
            i.putExtra("CATEGORY_NAME","superhero");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(i);

        }else if (id==R.id.rate){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            Uri uri =Uri.parse("market://details?id="+getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW,uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_NEW_DOCUMENT|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            }catch (ActivityNotFoundException e){
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://play.google.com/store/apps/details?id="+getPackageName())));
            }

        }else if (id==R.id.feedback){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            SettingsActivity.sendFeedback(this);
        }else if (id==R.id.about){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            Intent intent=new Intent(this,AboutActivity.class);
            startActivity(intent);
        }
        return true;

    }




    public static class PlaceHolderFragment extends Fragment{

        private static final String ARG_SECTION_NUMBER="section_number";
        private static Context context;

        public static PlaceHolderFragment newInstance(int sectionNumber) {
            PlaceHolderFragment fragment = new PlaceHolderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.not_connected_layout, container, false);
             context=rootView.getContext();
            return  rootView;
        }
    }

    public class SectionsPageAdapter extends FragmentPagerAdapter{


        public SectionsPageAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return PremiumTab.newInstance(position+1);
                case 1:
                    return RecentlyAdded.newInstance(position+1);
                case 2:
                    return CategoryTab.newInstance(position+1);


            }
            return PlaceHolderFragment.newInstance(position+1);
        }


        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "EDITOR'S CHOICE";
                case 1:
                    return "ALL";
                case 2:
                    return "CATEGORIES";
                //case 3:
                    //return "FAVORITE";
            }

            return null;
        }
    }

    public static class RecentlyAdded extends Fragment{

        private static final String ARG_SECTION_NUMBER="section_number";
        private  Context context;
        private  SwipeRefreshLayout swiper;
        private ProgressBar progressBar;
        private Handler handler;
        private Runnable r;


        public static RecentlyAdded newInstance(int sectionNumber) {
            RecentlyAdded fragment = new RecentlyAdded();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        public boolean getConnectivityStatus(Context context){
            ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNet=cm.getActiveNetworkInfo();
            return activeNet!=null&&activeNet.isConnectedOrConnecting();
        }



        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
           if (getConnectivityStatus(getContext())) {
               final View rootView = inflater.inflate(R.layout.recently_added_tab, container, false);
               swiper = rootView.findViewById(R.id.swipe_refresh);
               progressBar = rootView.findViewById(R.id.rated_tab_progressBar);
               context = rootView.getContext();
               recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
               gridLayoutManager = new GridLayoutManager(rootView.getContext(), 3, LinearLayoutManager.VERTICAL, false);
               recyclerView.setLayoutManager(gridLayoutManager);

               recyclerViewAdapter = new RecyclerViewAdapter(recyclerView, rootView.getContext(), MainActivity.reference, progressBar, MainActivity.pathrefsthumb);
               recyclerView.setAdapter(recyclerViewAdapter);
               r=new Runnable() {
                   @Override
                   public void run() {
                       if (recyclerViewAdapter!=null) {

                           //recyclerViewAdapter.notifyItemRangeChanged(0,pathrefs.size()-1);
                           Collections.shuffle(MainActivity.pathrefsthumb);
                           recyclerViewAdapter.notifyDataSetChanged();
                           if(MainActivity.pathrefsthumb.size()!=0)Snackbar.make(rootView,"Shuffled & Updated latest wallpapers!",Snackbar.LENGTH_SHORT).setActionTextColor(Color.RED).show();

                       }
                       swiper.setRefreshing(false);
                   }
               };

               swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                   @Override
                   public void onRefresh() {
                       if (getConnectivityStatus(getContext())){
                         handler=  new Handler();
                                 handler.postDelayed(r,2000);
                       }else{
                           Snackbar.make(rootView,"No Network Connection!",Snackbar.LENGTH_SHORT).show();
                           swiper.setRefreshing(false);
                       }
                   }
               });
               return  rootView;
           }else {
               final View rootView=inflater.inflate(R.layout.not_connected_layout,container,false);
               Button button=rootView.findViewById(R.id.retry_button);
              button.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      if (getConnectivityStatus(getContext())){

                      }
                  }
              });
               return  rootView;

           }





        }

        @Override
        public void onDestroy() {
            if (handler!=null&&r!=null)handler.removeCallbacks(r);
            super.onDestroy();
        }

    }



    //premium tab
    public static class PremiumTab extends Fragment{

        private static final String ARG_SECTION_NUMBER="section_number";
        private  Context context;

        private  SwipeRefreshLayout swiper_premium;
        private ProgressBar progressBar;
        private Handler handler;
        private Runnable r;


        public static PremiumTab newInstance(int sectionNumber) {
            PremiumTab fragment = new PremiumTab();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        public boolean getConnectivityStatus(Context context){
            ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNet=cm.getActiveNetworkInfo();
            return activeNet!=null&&activeNet.isConnectedOrConnecting();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            if (getConnectivityStatus(getContext())) {
                final View rootView = inflater.inflate(R.layout.premium_tab_layout, container, false);
                swiper_premium = rootView.findViewById(R.id.swipe_refresh_premium);
                progressBar = rootView.findViewById(R.id.premium_tab_progressBar);
                context = rootView.getContext();
                recyclerView_premium = (RecyclerView) rootView.findViewById(R.id.recyclerview_premium);
                gridLayoutManager = new GridLayoutManager(rootView.getContext(), 3, LinearLayoutManager.VERTICAL, false);
                recyclerView_premium.setLayoutManager(gridLayoutManager);


                premiumTabRecyclerAdapter = new PremiumTabRecyclerAdapter(rootView.getContext(), MainActivity.premium_wall_refs, progressBar);
                recyclerView_premium.setAdapter(premiumTabRecyclerAdapter);

                r=new Runnable() {
                    @Override
                    public void run() {
                        if (premiumTabRecyclerAdapter != null) {

                            //recyclerViewAdapter.notifyItemRangeChanged(0,pathrefs.size()-1);
                            if (premium_wall_refs.size()!=0)Snackbar.make(rootView, "You have the latest Wallpapers", Snackbar.LENGTH_SHORT).show();

                        }

                        swiper_premium.setRefreshing(false);
                    }
                };

                if (getConnectivityStatus(rootView.getContext())) {
                    swiper_premium.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                           handler= new Handler();
                                    handler.postDelayed(r, 2000);
                        }
                    });
                } else {
                    Snackbar.make(rootView, "No Network Connection!", Snackbar.LENGTH_SHORT).show();
                    swiper_premium.setRefreshing(false);
                }
                return rootView;
            }else {
                final View rootView=inflater.inflate(R.layout.not_connected_layout,container,false);
                Button button=rootView.findViewById(R.id.retry_button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getConnectivityStatus(getContext())){

                        }
                    }
                });
                return  rootView;

            }
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }
    }

    public static class CategoryTab extends Fragment{
        private static final String ARG_SECTION_NUMBER="section_number";
        private  Context context;
        private  SwipeRefreshLayout swiper;
        private ProgressBar progressBar;

        private ArrayList<String> categories;


        public static CategoryTab newInstance(int sectionNumber) {
            CategoryTab fragment = new CategoryTab();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.rated_tab, container, false);
            swiper=rootView.findViewById(R.id.swipe_refresh_premium);
            context=rootView.getContext();
            categories=new ArrayList<>();
            categories.add("ABSTRACT");
            categories.add("ANIMALS");
            categories.add("AMOLED");
            categories.add("BLACK");
            categories.add("CARS");
            categories.add("CITY");
            categories.add("MISC");
            categories.add("LANDSCAPE");
            categories.add("MATERIAL");
            categories.add("MINIMAL");
            categories.add("MOTIVATIONAL");
            categories.add("SPACE");
            categories.add("SUPERHERO");
            category= (RecyclerView)rootView.findViewById(R.id.category_tab_recycler);
            categoryTabRecyclerAdapter=new CategoryTabRecyclerAdapter(rootView.getContext(),categories);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(rootView.getContext(),LinearLayoutManager.VERTICAL,false);
            category.setLayoutManager(linearLayoutManager);
            category.setAdapter(categoryTabRecyclerAdapter);


            /*swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (categoryTabRecyclerAdapter!=null) {

                                //recyclerViewAdapter.notifyItemRangeChanged(0,pathrefs.size()-1);
                                Snackbar.make(rootView,"You have the latest Wallpapers",Snackbar.LENGTH_SHORT).show();

                            }

                            swiper.setRefreshing(false);
                        }
                    },3000);
                }
            });*/

            return  rootView;
        }

    }

}
