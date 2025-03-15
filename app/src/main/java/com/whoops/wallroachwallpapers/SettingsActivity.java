package com.whoops.wallroachwallpapers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;


import androidx.appcompat.app.AlertDialog;

import java.io.File;

public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(android.R.id.content,new MainPreferenceFragment()).commit();


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();

        }
        return true;
    }


    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);
            Preference contact =findPreference(getString(R.string.contact_developer));
            Preference policy=findPreference("policy");
            if (contact!=null)contact.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    sendFeedback(getActivity());
                    return true;
                }
            });
            if (policy!=null)policy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    openPolicy(getActivity());
                    return true;
                }
            });

            Preference clear_cache=findPreference(getString(R.string.clear_cache));
           // bindPrefSummarytoValue(clear_cache);
            if (clear_cache!=null)clear_cache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("Delete Cache")
                            .setMessage("Are you sure you want to delete the image cache?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteCache(getActivity());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                    return true;
                }
            });

            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
            CheckBoxPreference key_notification= (CheckBoxPreference) findPreference(getString(R.string.key_notification));
            boolean checkbox_pref=sharedPreferences.getBoolean("key_notification",true);
            key_notification.setChecked(checkbox_pref);
        }
       /* private static void bindPrefSummarytoValue(Preference preference){
            preference.setOnPreferenceChangeListener(sbindPrefSummarytoValueListener);
            sbindPrefSummarytoValueListener.onPreferenceChange(preference,"Clear locally cached images. Size: "+getCacheSize(preference.getContext())+" KB");
        }

        private static Preference.OnPreferenceChangeListener sbindPrefSummarytoValueListener=new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String stringValue=o.toString();
                preference.setSummary(stringValue);
                return true;
            }
        };*/




    }

    public static void openPolicy(Context context){
        String policy_url="https://whoopswallroach.wordpress.com/privacy-policy/";
        Intent i=new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(policy_url));
        context.startActivity(i);
    }

    public static void sendFeedback(Context context){
        String body=null;
        try{
            body=context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
            body="\n\n------------------------------------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE+"\n App Version: "+body+"\n Device Brand: "+Build.BRAND+"\n Device Model: "+Build.MODEL+"\n Device Manufacturer: "+Build.MANUFACTURER;
        }catch (PackageManager.NameNotFoundException e){}
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"pgahlaut888@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT,"Query from android app");
        intent.putExtra(Intent.EXTRA_TEXT,body);
        context.startActivity(Intent.createChooser(intent,context.getString(R.string.choose_email_client)));
    }
    public static void deleteCache(Context context){
        try {
            File dir=context.getCacheDir();
            deleteDir(dir);
        }catch (Exception e){}
    }
    public static boolean deleteDir(File dir){
        if (dir!=null&&dir.isDirectory()){
            String[] children=dir.list();
            for (int i=0;i<children.length;i++){
                boolean success=deleteDir(new File(dir,children[i]));
                if (!success){
                    return false;
                }
            }
            return dir.delete();
        }else if (dir!=null&&dir.isFile()){
            return dir.delete();
        }else {
            return false;
        }
    }

    public static float getCacheSize(Context context){
        File dir=context.getCacheDir();
        File extdir=context.getExternalCacheDir();

        long size=0;
        File[] files=dir.listFiles();
        for (File f:files){
            size=size+f.length();
        }
        File[] ext=extdir.listFiles();
        for (File f:ext){
            size=size+f.length();
        }
        return size/1024;
    }

}
