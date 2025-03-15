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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class PremiumTabRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ViewHolder viewHolder;
    View view;
    Context context;
    ProgressBar progressBar;
    static ArrayList<StorageReference> ref_list;
    private int lastPosition=0;


    public PremiumTabRecyclerAdapter(Context context, ArrayList<StorageReference> ref_list, ProgressBar progressBar){
        this.context=context;
        this.ref_list=ref_list;
        this.progressBar=progressBar;
    }

    @Override
    public int getItemViewType(int position) {
        if (ref_list.size()==0){
            return -1;
        }
        else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==-1){
            view=LayoutInflater.from(context).inflate(R.layout.no_data_layout,parent,false);
            NoDataViewHolder viewHolder=new NoDataViewHolder(view);return viewHolder;

        }else {
            view = LayoutInflater.from(context).inflate(R.layout.cardview, parent, false);
            viewHolder=new ViewHolder(view);
            return viewHolder;


        }


    }
    private void setAnimation(View view,int pos){
        if (pos>lastPosition){
            Animation animation= AnimationUtils.loadAnimation(context,R.anim.fab_open);
            AnticipateOvershootInterpolator overshootInterpolator=new AnticipateOvershootInterpolator(2,4);
            animation.setInterpolator(overshootInterpolator);
            view.startAnimation(animation);
            lastPosition=pos;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (ref_list.size()>0) {
            Log.d("PREMIUMTAB RVA$$$$$$$$$","--->"+ref_list.get(position).toString());
            Glide.with(context).using(new FirebaseImageLoader()).load(ref_list.get(position)).placeholder(R.drawable.ic_mipmap_trans_96).animate(new ViewPropertyAnimation.Animator() {
                @Override
                public void animate(View view) {
                    Animation animation=AnimationUtils.loadAnimation(context,R.anim.fab_open);
                    animation.setInterpolator(new AnticipateOvershootInterpolator(4));
                    //animation.setFillBefore(true);
                    view.setAnimation(animation);
                }
            }).centerCrop().into(viewHolder.image);
            viewHolder.setIsRecyclable(false);
            setAnimation(viewHolder.container,position);
        }

    }

    @Override
    public int getItemCount() {
        Log.d("PRemium RVA","Item COunt"+ref_list.size());
        if (ref_list.size()==0&&progressBar.getVisibility()==View.INVISIBLE){
            progressBar.setVisibility(View.VISIBLE);

        }else if (ref_list.size()>0&&(progressBar.getVisibility()==View.VISIBLE||progressBar.getVisibility()==View.INVISIBLE)){
            progressBar.setVisibility(View.GONE);
        }
        return ref_list.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        CardView container;
        ImageView starImageOn;

        public ViewHolder(final View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.card_image);
            container=(CardView)itemView.findViewById(R.id.cardview_container);
            /*starImage = itemView.findViewById(R.id.starImage);
            starImageOn = itemView.findViewById(R.id.starImageOn);
            starImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    starImage.setVisibility(View.GONE);
                    starImageOn.setVisibility(View.VISIBLE);
                    Toast.makeText(itemView.getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                }
            });
            starImageOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    starImage.setVisibility(View.VISIBLE);
                    starImageOn.setVisibility(View.GONE);
                    Toast.makeText(itemView.getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                }
            });*/
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s = ref_list.get(getPosition()).toString().substring(28);
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
                    String a = ref_list.get(getPosition()).toString().substring(28);
                    Intent i = new Intent(itemView.getContext(), FullScreenActivity.class);
                    i.putExtra("IMAGE_NAME", ref);
                    i.putExtra("PLACEHOLDER_IMAGE",a);
                    /*if (MainActivity.image_names.get(getPosition()).contains("cars")) {
                        i.putExtra("CATEGORY_NAME", "cars/");
                    } else if (MainActivity.image_names.get(getPosition()).contains("landscape")) {
                        i.putExtra("CATEGORY_NAME", "landscape/");
                    } else if (MainActivity.image_names.get(getPosition()).contains("abstract")) {
                        i.putExtra("CATEGORY_NAME", "abstract/");
                    } else if (MainActivity.image_names.get(getPosition()).contains("minimal")) {
                        i.putExtra("CATEGORY_NAME", "minimal/");
                    } else if (MainActivity.image_names.get(getPosition()).contains("material")) {
                        i.putExtra("CATEGORY_NAME", "material/");
                    } else if (MainActivity.image_names.get(getPosition()).contains("black")) {
                        i.putExtra("CATEGORY_NAME", "black/");
                    } else if (MainActivity.image_names.get(getPosition()).contains("nature")) {
                        i.putExtra("CATEGORY_NAME", "nature/");
                    } else if (MainActivity.image_names.get(getPosition()).contains("tech")) {
                        i.putExtra("CATEGORY_NAME", "technology/");
                    } else {
                        i.putExtra("CATEGORY_NAME", "images/");
                    }*/
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }

    public static class NoDataViewHolder extends RecyclerView.ViewHolder{

        public NoDataViewHolder(View itemView) {
            super(itemView);
        }
    }
}
