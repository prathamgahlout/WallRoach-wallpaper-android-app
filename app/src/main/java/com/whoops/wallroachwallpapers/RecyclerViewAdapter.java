package com.whoops.wallroachwallpapers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ViewHolder viewHolder;
    View view;
     Context context;
    //StorageReference reference;
    ProgressBar progressBar;
    ArrayList<StorageReference> pathrefs;
    //int i=0;
    SwipeRefreshLayout swiper_refresh;

    private final int VIEW_TYPE_ITEM=0;
    private final int VIEW_TYPE_LOADING=1;
    private int lastPosition=0;
    private boolean isloading=true;
    private int visibleThreshold=9;
    private int lastVisibleItem,totalItemCount;


    public RecyclerViewAdapter(RecyclerView recyclerView,Context context, StorageReference reference,ProgressBar progressBar,ArrayList<StorageReference> pathrefs) {
        this.context = context;
        //this.reference= reference;
        //this.image_names=image_names;
        this.progressBar=progressBar;

        this.pathrefs=pathrefs;
        Log.d(MainActivity.TAG,"recyclervieew constructor");
        final GridLayoutManager layoutManager=(GridLayoutManager)recyclerView.getLayoutManager();
        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount=layoutManager.getItemCount();
                lastVisibleItem=layoutManager.findLastVisibleItemPosition();
                if (!isloading&&totalItemCount<=(lastVisibleItem+visibleThreshold)){
                    if (onLoadMoreListener!=null){
                        onLoadMoreListener.onLoadMore();
                    }
                    isloading=true;
                }
            }
        });*/




    }

    private void setAnimation(View view,int pos){
        if (pos>lastPosition){
            Animation animation= AnimationUtils.loadAnimation(context, R.anim.fab_open);
            animation.setInterpolator(new AnticipateOvershootInterpolator(2));
            view.startAnimation(animation);
            lastPosition=pos;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            view = LayoutInflater.from(context).inflate(R.layout.cardview, parent, false);

            viewHolder = new ViewHolder(view);
            viewHolder.setIsRecyclable(false);
            Log.d(MainActivity.TAG, "recyclervieew oncreateviewholder" + viewType);

        return viewHolder;



    }



    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        Glide.with(context).using(new FirebaseImageLoader()).load(pathrefs.get(position)).placeholder(R.drawable.ic_mipmap_trans_96).animate(new ViewPropertyAnimation.Animator() {
            @Override
            public void animate(View view) {
                Animation animation=AnimationUtils.loadAnimation(context,R.anim.fab_open);
                animation.setInterpolator(new AnticipateOvershootInterpolator(4));
                //animation.setFillBefore(true);
                view.setAnimation(animation);
            }
        }).centerCrop().into(viewHolder.imageView);
        viewHolder.setIsRecyclable(false);
        setAnimation(viewHolder.container,position);

        Log.d(MainActivity.TAG, "recyclervieew onBind" + pathrefs.get(position));


    }

    public void setLoaded(){
        isloading=false;
    }

    @Override
    public int getItemCount() {

        Log.d(MainActivity.TAG,"recyclervieew itemCount"+pathrefs.size());
        if (pathrefs.size()==0&&progressBar.getVisibility()==View.INVISIBLE){
            progressBar.setVisibility(View.VISIBLE);

        }else if (pathrefs.size()>0&&(progressBar.getVisibility()==View.VISIBLE||progressBar.getVisibility()==View.INVISIBLE)){
            progressBar.setVisibility(View.GONE);
        }
        if (pathrefs.size()>0&&isloading){
            Collections.shuffle(MainActivity.pathrefsthumb);
            setLoaded();
        }
        return pathrefs.size();
    }






    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        CardView container;
        ImageView starImageOn;
        boolean dataSet=false;

        public ViewHolder(final View itemView) {
            super(itemView);
            //starImage=itemView.findViewById(R.id.starImage);
            //starImageOn=itemView.findViewById(R.id.starImageOn);
            imageView=(ImageView)itemView.findViewById(R.id.card_image);
            container=(CardView)itemView.findViewById(R.id.cardview_container);
            /*final SharedPreferences pref=itemView.getContext().getSharedPreferences("fav_pref",Context.MODE_PRIVATE);
            if (pref.getString(MainActivity.pathrefsthumb.get(getPosition()).toString(),null)!=null){
                starImage.setVisibility(View.GONE);
                starImageOn.setVisibility(View.VISIBLE);
            }*/
            //itemView.setOnClickListener(this);


            /*starImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    starImage.setVisibility(View.GONE);
                    starImageOn.setVisibility(View.VISIBLE);
                   /* SharedPreferences.Editor editor=pref.edit();
                    String s=MainActivity.pathrefsthumb.get(getPosition()).toString().substring(28);
                    String z=s.substring(0,s.length()-10);
                    String ref=z+".jpg";
                    editor.putString(MainActivity.pathrefsthumb.get(getPosition()).toString(),ref);
                    editor.commit();
                    Toast.makeText(itemView.getContext(),"Added to favorites",Toast.LENGTH_SHORT).show();
                }
            });
            starImageOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences pref=itemView.getContext().getSharedPreferences("fav_pref",Context.MODE_PRIVATE);
                    //SharedPreferences.Editor editor=pref.edit();
                    /*editor.remove(MainActivity.pathrefsthumb.get(getPosition()).toString());
                    editor.commit();
                    starImage.setVisibility(View.VISIBLE);
                    starImageOn.setVisibility(View.GONE);
                    Toast.makeText(itemView.getContext(),"Removed from favorites",Toast.LENGTH_SHORT).show();
                }
            });*/
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s=MainActivity.pathrefsthumb.get(getPosition()).toString().substring(28);
                    String ref="";
                    if (s.contains("jpg")){
                        String z=s.substring(0,s.length()-10);
                        ref=z+".jpg";
                    }else if (s.contains("jpeg")){
                        String z=s.substring(0,s.length()-11);
                        ref=z+".jpeg";
                    }else if (s.contains("png")){
                        String z=s.substring(0,s.length()-10);
                        ref=z+".png";
                    }
                    String a=MainActivity.pathrefsthumb.get(getPosition()).toString().substring(28);
                    /*String rev="";
                    for (int i=s.length();i>=0;i--){
                        rev
                    }*/
                    Intent i= new Intent(itemView.getContext(),FullScreenActivity.class);
                    i.putExtra("IMAGE_NAME",ref);
                    i.putExtra("PLACEHOLDER_IMAGE",a);
                    i.putExtra("POSITION",getPosition());
                    if (MainActivity.image_names.get(getPosition()).contains("cars")){
                        i.putExtra("CATEGORY_NAME","cars/");
                    }else if (MainActivity.image_names.get(getPosition()).contains("landscape")){
                        i.putExtra("CATEGORY_NAME","landscape/");
                    }else if (MainActivity.image_names.get(getPosition()).contains("abstract")){
                        i.putExtra("CATEGORY_NAME","abstract/");
                    }else if (MainActivity.image_names.get(getPosition()).contains("minimal")){
                        i.putExtra("CATEGORY_NAME","minimal/");
                    }else if (MainActivity.image_names.get(getPosition()).contains("material")){
                        i.putExtra("CATEGORY_NAME","material/");
                    }else if (MainActivity.image_names.get(getPosition()).contains("black")){
                        i.putExtra("CATEGORY_NAME","black/");
                    }else if (MainActivity.image_names.get(getPosition()).contains("nature")){
                        i.putExtra("CATEGORY_NAME","nature/");
                    }else if (MainActivity.image_names.get(getPosition()).contains("tech")){
                        i.putExtra("CATEGORY_NAME","technology/");
                    }else {
                        i.putExtra("CATEGORY_NAME","images/");
                    }


                    itemView.getContext().startActivity(i);
                }
            });
        }


        public void getviewHodlder() {

        }
    }

    public static class WallViewHolder extends RecyclerView.ViewHolder{

        public ProgressBar progressBar;

        public WallViewHolder(View itemView) {
            super(itemView);

        }
    }
}


