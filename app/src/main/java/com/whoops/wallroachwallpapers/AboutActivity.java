package com.whoops.wallroachwallpapers;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;


import java.util.List;


public class AboutActivity extends AppCompatActivity {


    private FloatingActionButton shareappontwitter,shareonfb,gpluscommunity,wallroachblog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        shareappontwitter=(FloatingActionButton)findViewById(R.id.shareappontwitter);
        shareonfb=(FloatingActionButton)findViewById(R.id.shareonfb);
        gpluscommunity=(FloatingActionButton)findViewById(R.id.gpluscommunity);
        wallroachblog=(FloatingActionButton)findViewById(R.id.wallroachblog);

        CardView cardView=(CardView)findViewById(R.id.rate_card);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri =Uri.parse("market://details?id="+getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW,uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_NEW_DOCUMENT|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                }catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://play.google.com/store/apps/details?id="+getPackageName())));
                }
            }
        });

        shareappontwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent =new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,"Hi! Check out this beautiful Android App WallRoach 4K HD Wallpapers.  #wallroach , #wallroachHDwallpapers , Get the WallRoach App from Google Play https://play.google.com/store/apps/details?id=com.whoops.wallroachwallpapers");
                PackageManager packageManager=getPackageManager();
                List<ResolveInfo> resolvedInfoList=packageManager.queryIntentActivities(shareIntent,PackageManager.MATCH_DEFAULT_ONLY);

                boolean resolved=false;
                for(ResolveInfo resolveInfo: resolvedInfoList){
                    if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")){
                        shareIntent.setClassName(resolveInfo.activityInfo.packageName,resolveInfo.activityInfo.name);
                        resolved=true;
                        break;
                    }
                }
                if(resolved){
                    startActivity(shareIntent);
                }else {
                    Toast.makeText(getApplicationContext(),"Twitter App is not installed on the device",Toast.LENGTH_LONG).show();
                }
            }
        });

        shareonfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent =new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,"Hi! Check out this beautiful Android App WallRoach 4K HD Wallpapers.  #wallroach , #wallroachHDwallpapers , Get the WallRoach App from Google Play https://play.google.com/store/apps/details?id=com.whoops.wallroachwallpapers");
                PackageManager packageManager=getPackageManager();
                List<ResolveInfo> resolvedInfoList=packageManager.queryIntentActivities(shareIntent,PackageManager.MATCH_DEFAULT_ONLY);

                boolean resolved=false;
                for(ResolveInfo resolveInfo: resolvedInfoList){
                    if (resolveInfo.activityInfo.packageName.startsWith("com.facebook")){
                        shareIntent.setClassName(resolveInfo.activityInfo.packageName,resolveInfo.activityInfo.name);
                        resolved=true;
                        break;
                    }
                }
                if(resolved){
                    startActivity(shareIntent);
                }else {
                    Toast.makeText(getApplicationContext(),"Facebook App is not installed on the device",Toast.LENGTH_LONG).show();
                }
            }

        });

        gpluscommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="https://plus.google.com/communities/115402358216379571443";
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        wallroachblog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="https://wallroach.blogspot.com";
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });






    }


}
