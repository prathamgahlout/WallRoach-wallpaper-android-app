package com.whoops.wallroachwallpapers;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import es.dmoral.toasty.Toasty;

public class FullScreenActivity extends Activity {

    private ImageView fullimage;
    private FloatingActionButton fab_setas,fab_home,fab_lock,fab_download,fab_share_wall,fab_tweet,fab_insta;
    private boolean image_loaded=false;
    private TextView setAshome,setAslock,download,share_this_wall;
    private ProgressBar progressBar;
    private AdView adView;
    private boolean isFabOpen=false;
    private boolean isShareFabOpen=false;
    private Animation fab_open,fab_close,rotate_forward,rotate_back,share_fab_rotation,share_fab_rotation_for;
    private StorageReference ref;
    private InterstitialAd interstitialAd;

    private final int SHARE_ON_FB=0;
    private final int SHARE_ON_INSTA=1;
    private final int SHARE_ON_TWITTER=3;




    public boolean getConnectivityStatus(){
        ConnectivityManager cm=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNet=cm.getActiveNetworkInfo();
        return activeNet!=null&&activeNet.isConnectedOrConnecting();
    }
    private void showInterstitial() {
            //interstitialAd.show();

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getConnectivityStatus()) {
            setContentView(R.layout.activity_full_screen);

            fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);
            fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
            rotate_back = AnimationUtils.loadAnimation(this, R.anim.rotate_back);
            rotate_forward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
            fab_open.setInterpolator(new AnticipateOvershootInterpolator(4));
            fab_close.setInterpolator(new AnticipateOvershootInterpolator(4));
            share_fab_rotation=AnimationUtils.loadAnimation(this,R.anim.share_fab_rotation);
            share_fab_rotation.setInterpolator(new AnticipateOvershootInterpolator(4));
            share_fab_rotation_for=AnimationUtils.loadAnimation(this,R.anim.share_fab_rotation_for);
            share_fab_rotation_for.setInterpolator(new AnticipateOvershootInterpolator(4));

            /*interstitialAd=new InterstitialAd(this);
            interstitialAd.setAdUnitId("ca-app-pub-6967138802491752/5517921429");*/


            fullimage = (ImageView) findViewById(R.id.fullscreenimage);
            setAshome= (TextView) findViewById(R.id.setAsHome);
            setAslock= (TextView) findViewById(R.id.setAslock);
            download= (TextView) findViewById(R.id.download);
            fab_setas = (FloatingActionButton) findViewById(R.id.fab_setas);
            fab_home = (FloatingActionButton) findViewById(R.id.fab_home);
            fab_lock = (FloatingActionButton) findViewById(R.id.fab_lock);
            fab_download= (FloatingActionButton) findViewById(R.id.fab_down);
            fab_share_wall=(FloatingActionButton) findViewById(R.id.fab_share_wall);
            share_this_wall=(TextView)findViewById(R.id.share_this_wall);
            /*fab_tweet=(FloatingActionButton)findViewById(R.id.fab_tweet);
            fab_insta=(FloatingActionButton)findViewById(R.id.fab_insta);*/
            progressBar = (ProgressBar) findViewById(R.id.progressbar);
            /*adView = (AdView) findViewById(R.id.main_banner);

            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            interstitialAd.loadAd(new AdRequest.Builder().build());*/


            final Intent intent = getIntent();
            final String s = intent.getExtras().getString("IMAGE_NAME");
            String a = intent.getExtras().getString("PLACEHOLDER_IMAGE");
            if (a!=null)ref = FirebaseStorage.getInstance().getReferenceFromUrl("gs://walle-f113c.appspot.com").child(a);

            final ImageView placeholder = (ImageView) findViewById(R.id.placeholder);
            if (ref!=null)Glide.with(this).using(new FirebaseImageLoader()).load(ref).centerCrop().into(placeholder);

            if (s!=null)ref = FirebaseStorage.getInstance().getReferenceFromUrl("gs://walle-f113c.appspot.com").child(s);
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
                            //Toast.makeText(getApplicationContext(),"Please wait...",Toast.LENGTH_SHORT).show();
                            Toasty.info(getBaseContext(),"Please wait...",Toast.LENGTH_SHORT,true).show();
                            setHomeScreenWallpaper();

                            if (interstitialAd.isLoaded()){
                                showInterstitial();
                            }

                        }
                    });
                    setAshome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Toast.makeText(getApplicationContext(),"Please wait...",Toast.LENGTH_SHORT).show();
                            Toasty.info(getApplicationContext(),"Please wait...",Toast.LENGTH_SHORT,true).show();
                            setHomeScreenWallpaper();
                            if (interstitialAd.isLoaded()){
                                showInterstitial();
                            }

                        }
                    });
                    setAslock.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Toast.makeText(getApplicationContext(),"Please wait...",Toast.LENGTH_SHORT).show();
                            Toasty.info(getApplicationContext(),"Please wait...",Toast.LENGTH_SHORT,true).show();
                            setLockScreenWallpaper();
                            if (interstitialAd.isLoaded()){
                                showInterstitial();
                            }

                        }
                    });
                    fab_lock.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Toast.makeText(getApplicationContext(),"Please wait...",Toast.LENGTH_SHORT).show();
                            Toasty.info(getApplicationContext(),"Please wait...",Toast.LENGTH_SHORT,true).show();
                            setLockScreenWallpaper();
                            if (interstitialAd.isLoaded()){
                                showInterstitial();
                            }

                        }
                    });
                    fab_download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            downloadImage(false);
                            if (interstitialAd.isLoaded()){
                                showInterstitial();
                            }


                        }
                    });
                    download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            downloadImage(false);
                            if (interstitialAd.isLoaded()){
                                showInterstitial();
                            }


                        }
                    });
                    share_this_wall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            downloadImage(true);

                        }
                    });
                    fab_share_wall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            downloadImage(true);

                        }
                    });
                    /*fab_tweet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            downloadImage(true,SHARE_ON_TWITTER);
                        }
                    });
                    fab_insta.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            downloadImage(true,SHARE_ON_INSTA);
                        }
                    });*/


                    return false;
                }
            }).animate(new ViewPropertyAnimation.Animator() {
                @Override
                public void animate(View view) {
                    Animation animation=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
                    animation.setInterpolator(new AnticipateOvershootInterpolator(4));
                    //animation.setFillBefore(true);
                    view.setAnimation(animation);
                }
            }).centerCrop().into(fullimage);
        /*switch (s2){
            case "MAIN_RECYCLER":


        }*/
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

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public Uri downloadImage(boolean share){

        if(ref!=null){
            final File file;
            final AlertDialog builder;
            File imagefile;


            builder = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AppTheme_NoActionBar_Fullscreen)).create();
            LayoutInflater inflater=getLayoutInflater();
            View downloadalertlayout=inflater.inflate(R.layout.download_progress_dialog,null);
            builder.setView(downloadalertlayout);
            builder.setCancelable(false);
            builder.show();
            final ProgressBar progressBar= (ProgressBar) downloadalertlayout.findViewById(R.id.downloadprogress);
            progressBar.setBackground(getResources().getDrawable(R.drawable.progress_dialog_background));
            try{
                file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"/WallRoach/");
                if (!file.exists())file.mkdirs();
                 imagefile=new File(file.getAbsolutePath(),ref.getName());
                ref.getFile(imagefile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        try {
                            builder.dismiss();
                            Toasty.success(FullScreenActivity.this,"Successfully downloaded Wallpaper in "+file.getCanonicalPath(),Toast.LENGTH_LONG,true).show();
                            //Toast.makeText(getApplicationContext(),"Successfully downloaded Wallpaper in"+file.getCanonicalPath(),Toast.LENGTH_LONG).show();
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

                if (share){
                    Intent intent=new Intent(Intent.ACTION_SEND);
                    Uri uri=Uri.parse(imagefile.getAbsolutePath());
                    intent.setType("image/jpeg");
                    intent.putExtra(Intent.EXTRA_STREAM,uri);
                    intent.putExtra(Intent.EXTRA_TEXT,"Download the WallRoach App from Google Play- https://play.google.com/store/apps/details?id=com.whoops.wallroachwallpapers");
                    startActivity(Intent.createChooser(intent,"Share via"));
                }

                return Uri.parse(imagefile.getCanonicalPath());


            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    private void setHomeScreenWallpaperViaIntent(){
        Uri uri=downloadImage(false);
        if (uri!=null) {
            Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("mimeType", "image/*");
            startActivity(intent);
        }else {
            Log.d("URI WAS NULLLL!","URI WAS NULLLL");
        }
    }

    public void setHomeScreenWallpaper(){
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.sad_smiley);
            fullimage.buildDrawingCache();
        if (fullimage.getDrawingCache()!=null)
             bitmap= fullimage.getDrawingCache();


        try {
            WallpaperManager wm=WallpaperManager.getInstance(FullScreenActivity.this);

                wm.setBitmap(bitmap);

            Toasty.success(getApplicationContext(),"Your Wallpaper has been set!",Toast.LENGTH_LONG,true).show();
            //Toast.makeText(FullScreenActivity.this,"Your Wallpaper has been set!",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setLockScreenWallpaper(){
        fullimage.buildDrawingCache();
        Bitmap bitmap=fullimage.getDrawingCache();

        try {
            WallpaperManager wm=WallpaperManager.getInstance(FullScreenActivity.this);
            if (Build.VERSION.SDK_INT>=24) {
                wm.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                Toasty.success(getApplicationContext(),"Your Wallpaper has been set!",Toast.LENGTH_LONG,true).show();
                //Toast.makeText(FullScreenActivity.this, "Your Wallpaper has been successfully applied!", Toast.LENGTH_SHORT).show();
            }
            else
                Toasty.error(getApplicationContext(),"Sorry! Setting LockScreen Wallpaper is only allowed on devices that run on NOUGAT or above. Check Google docs for more info!",Toast.LENGTH_LONG,true).show();
            //Toast.makeText(this,"Sorry! Setting LockScreen Wallpaper is only allowed on devices that run on NOUGAT or above. Check Google docs for more info.",Toast.LENGTH_LONG).show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void animateFab(){
        if (isFabOpen){
            fab_setas.startAnimation(rotate_back);
            fab_share_wall.startAnimation(fab_close);
            fab_home.startAnimation(fab_close);
            fab_lock.startAnimation(fab_close);
            fab_download.startAnimation(fab_close);
            setAshome.startAnimation(fab_close);
            setAslock.startAnimation(fab_close);
            download.startAnimation(fab_close);
            share_this_wall.startAnimation(fab_close);
            fab_download.setClickable(false);
            fab_lock.setClickable(false);
            fab_home.setClickable(false);
            fab_share_wall.setClickable(false);
            share_this_wall.setClickable(false);
            isFabOpen=false;

        }else {
            fab_setas.startAnimation(rotate_forward);
            fab_home.setVisibility(View.VISIBLE);
            fab_lock.setVisibility(View.VISIBLE);
            fab_download.setVisibility(View.VISIBLE);
            fab_share_wall.setVisibility(View.VISIBLE);
            fab_share_wall.startAnimation(fab_open);
            fab_lock.startAnimation(fab_open);
            fab_home.startAnimation(fab_open);
            fab_download.startAnimation(fab_open);
            setAshome.startAnimation(fab_open);
            setAslock.startAnimation(fab_open);
            download.startAnimation(fab_open);
            share_this_wall.startAnimation(fab_open);
            fab_download.setClickable(true);
            share_this_wall.setClickable(true);
            fab_share_wall.setClickable(true);
            fab_lock.setClickable(true);
            fab_home.setClickable(true);
            isFabOpen=true;
        }
    }

    public void animateShareFab(){
        if (isShareFabOpen){
            fab_share_wall.startAnimation(share_fab_rotation_for);
            fab_insta.startAnimation(fab_close);
            fab_tweet.startAnimation(fab_close);
            fab_insta.setClickable(false);
            fab_tweet.setClickable(false);
            share_this_wall.startAnimation(fab_open);
            isShareFabOpen=false;
        }else{
            fab_share_wall.startAnimation(share_fab_rotation);
            fab_insta.setVisibility(View.VISIBLE);
            fab_tweet.setVisibility(View.VISIBLE);
            share_this_wall.startAnimation(fab_close);
            fab_insta.startAnimation(fab_open);
            fab_tweet.startAnimation(fab_open);
            fab_insta.setClickable(true);
            fab_tweet.setClickable(true);
            isShareFabOpen=true;
        }
    }
}
