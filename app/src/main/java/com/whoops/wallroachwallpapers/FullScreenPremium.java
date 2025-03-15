package com.whoops.wallroachwallpapers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class FullScreenPremium extends AppCompatActivity {

    private ImageView fullimage;
    private FloatingActionButton fab_setas,fab_home,fab_lock,fab_download;
    private boolean image_loaded=false;
    private TextView setAshome,setAslock,download;
    private ProgressBar progressBar;
    private boolean isFabOpen=true;
    private Animation fab_open,fab_close,rotate_forward,rotate_back;
    private volatile boolean isRewarded=false;
    private InterstitialAd interstitialAd;
    private StorageReference ref;
    private AdView adView;


    public boolean getConnectivityStatus(){
        ConnectivityManager cm=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNet=cm.getActiveNetworkInfo();
        return activeNet!=null&&activeNet.isConnectedOrConnecting();
    }

    private void showInterstitial() {
        if (interstitialAd != null) {
            interstitialAd.show(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getConnectivityStatus()) {
            /*rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
            rewardedVideoAd.setRewardedVideoAdListener(this);
            rewardedVideoAd.loadAd("ca-app-pub-6967138802491752/4217947508", new AdRequest.Builder().addTestDevice("DB5AAE762EBC25AAF350EFB16E0B804C").build());*/

            /*interstitialAd.setAdUnitId("ca-app-pub-6967138802491752/9590416466");*/
            interstitialAd = new InterstitialAd() {
                @Nullable
                @Override
                public FullScreenContentCallback getFullScreenContentCallback() {
                    return null;
                }

                @Nullable
                @Override
                public OnPaidEventListener getOnPaidEventListener() {
                    return null;
                }

                @NonNull
                @Override
                public ResponseInfo getResponseInfo() {
                    return null;
                }

                @NonNull
                @Override
                public String getAdUnitId() {
                    return "ca-app-pub-6967138802491752/5517921429";
                }

                @Override
                public void setFullScreenContentCallback(@Nullable FullScreenContentCallback fullScreenContentCallback) {

                }

                @Override
                public void setImmersiveMode(boolean b) {

                }

                @Override
                public void setOnPaidEventListener(@Nullable OnPaidEventListener onPaidEventListener) {

                }

                @Override
                public void show(@NonNull Activity activity) {

                }
            };



            setContentView(R.layout.activity_full_screen);
            getSupportActionBar().hide();

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            adView = (AdView) findViewById(R.id.main_banner);

           /* AdRequest adRequest = new AdRequest.Builder().addTestDevice("DB5AAE762EBC25AAF350EFB16E0B804C").build();
            adView.loadAd(adRequest);*/

            fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);
            fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
            rotate_back = AnimationUtils.loadAnimation(this, R.anim.rotate_back);
            rotate_forward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
            fab_open.setInterpolator(new AccelerateDecelerateInterpolator());
            fab_close.setInterpolator(new AccelerateDecelerateInterpolator());

            fullimage = (ImageView) findViewById(R.id.fullscreenimage);
            setAshome= (TextView) findViewById(R.id.setAsHome);
            setAslock= (TextView) findViewById(R.id.setAslock);
            download= (TextView) findViewById(R.id.download);
            fab_setas = (FloatingActionButton) findViewById(R.id.fab_setas);
            fab_home = (FloatingActionButton) findViewById(R.id.fab_home);
            fab_download=(FloatingActionButton)findViewById(R.id.fab_down);
            fab_lock = (FloatingActionButton) findViewById(R.id.fab_lock);
            progressBar = (ProgressBar) findViewById(R.id.progressbar);


            Intent intent = getIntent();
            String s = intent.getExtras().getString("IMAGE_NAME");
            String a = intent.getExtras().getString("PLACEHOLDER_IMAGE");
             ref = FirebaseStorage.getInstance().getReferenceFromUrl("gs://walle-f113c.appspot.com").child(a);

            String s1 = intent.getExtras().getString("CATEGORY_NAME");
            final ImageView placeholder = (ImageView) findViewById(R.id.placeholder);
            Glide.with(this).using(new FirebaseImageLoader()).load(ref).centerCrop().into(placeholder);
            //String s2=intent.getExtras().getString("FROM");
            //int i=intent.getExtras().getInt("POS");
            ref = FirebaseStorage.getInstance().getReferenceFromUrl("gs://walle-f113c.appspot.com").child(s);
            Log.d(MainActivity.TAG, "ref=" + s);


            Glide.with(this).using(new FirebaseImageLoader()).load(ref).listener(new RequestListener<StorageReference, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    placeholder.setVisibility(View.GONE);

                    progressBar.setVisibility(View.GONE);
                    fullimage.setVisibility(View.VISIBLE);
                    fab_home.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                             Toast.makeText(getApplicationContext(),"Please wait...",Toast.LENGTH_SHORT).show();
                             setHomeScreenWallpaper();
                             showInterstitial();
                        }
                    });
                    fab_lock.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getApplicationContext(),"Please wait...",Toast.LENGTH_SHORT).show();
                             setLockScreenWallpaper();
                            showInterstitial();
                        }
                    });
                    fab_download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            downloadImage();
                            showInterstitial();
                        }
                    });
                    download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            downloadImage();
                            showInterstitial();
                        }
                    });
                    setAshome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getApplicationContext(),"Please wait...",Toast.LENGTH_SHORT).show();
                            setHomeScreenWallpaper();
                            showInterstitial();
                        }
                    });
                    setAslock.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getApplicationContext(),"Please wait...",Toast.LENGTH_SHORT).show();
                            setLockScreenWallpaper();
                            showInterstitial();
                        }
                    });


                    return false;
                }
            }).centerCrop().into(fullimage);


            fab_setas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    animateFab();
                }
            });

        }else {
            setContentView(R.layout.not_connected_layout);
        }


    }

    public void downloadImage(){

        if(ref!=null){
            final File file;
            final AlertDialog builder;

            builder = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AppTheme_NoActionBar_Fullscreen)).create();
            LayoutInflater inflater=getLayoutInflater();
            View downloadalertlayout=inflater.inflate(R.layout.download_progress_dialog,null);
            builder.setView(downloadalertlayout);
            builder.setCancelable(false);
            builder.show();
            final ProgressBar progressBar= (ProgressBar) downloadalertlayout.findViewById(R.id.downloadprogress);
            try{
                file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"/WallRoach/");
                if (!file.exists())file.mkdirs();
                File imagefile=new File(file.getAbsolutePath(),ref.getName());
                ref.getFile(imagefile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        try {
                            builder.dismiss();
                            Toast.makeText(getApplicationContext(),"Successfully downloaded Wallpaper in"+file.getCanonicalPath(),Toast.LENGTH_LONG).show();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        builder.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressBar.setProgress((int)progress);
                    }
                });


            }catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void setHomeScreenWallpaper(){
        fullimage.buildDrawingCache();
        Bitmap bitmap=fullimage.getDrawingCache();

        try {
            WallpaperManager wm=WallpaperManager.getInstance(FullScreenPremium.this);

            wm.setBitmap(bitmap);

            Toast.makeText(FullScreenPremium.this,"Your Wallpaper has been set!",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setLockScreenWallpaper(){
        fullimage.buildDrawingCache();
        Bitmap bitmap=fullimage.getDrawingCache();

        try {
            WallpaperManager wm=WallpaperManager.getInstance(FullScreenPremium.this);
            if (Build.VERSION.SDK_INT>=24) {
                wm.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                Toast.makeText(FullScreenPremium.this, "Your Wallpaper has been set!", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this,"Sorry! Setting LockScreen Wallpaper is only allowed on devices that run on NOUGAT or above. Check Google docs for more info.",Toast.LENGTH_LONG).show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void animateFab(){
        if (isFabOpen){
            fab_setas.startAnimation(rotate_back);

            fab_home.startAnimation(fab_close);
            fab_lock.startAnimation(fab_close);
            fab_download.startAnimation(fab_close);
            setAshome.startAnimation(fab_close);
            setAslock.startAnimation(fab_close);
            download.startAnimation(fab_close);
            fab_download.setClickable(false);
            fab_lock.setClickable(false);
            fab_home.setClickable(false);
            isFabOpen=false;

        }else {
            fab_setas.startAnimation(rotate_forward);
            fab_home.setVisibility(View.VISIBLE);
            fab_lock.setVisibility(View.VISIBLE);
            fab_download.setVisibility(View.VISIBLE);
            fab_lock.startAnimation(fab_open);
            fab_home.startAnimation(fab_open);
            fab_download.startAnimation(fab_open);
            setAshome.startAnimation(fab_open);
            setAslock.startAnimation(fab_open);
            download.startAnimation(fab_open);
            fab_download.setClickable(true);
            fab_lock.setClickable(true);
            fab_home.setClickable(true);
            isFabOpen=true;
        }
    }

    /*@Override
    public void onRewardedVideoAdLoaded() {
            rewardedVideoAd.show();
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
            isRewarded=true;
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }*/
}
