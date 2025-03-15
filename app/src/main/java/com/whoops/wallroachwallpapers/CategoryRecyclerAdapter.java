package com.whoops.wallroachwallpapers;


import android.content.Context;
import android.content.Intent;
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

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    ViewHolder viewHolder;
    View view;
    Context context;
    ProgressBar progressBar;
    public static ArrayList<StorageReference> ref_list;
    private int lastPosition=0;

    public CategoryRecyclerAdapter(Context context,ArrayList<StorageReference> reference,ProgressBar progressBar){
        this.context=context;
        ref_list=new ArrayList<>();
        this.progressBar=progressBar;
        ref_list=reference;
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

            view = LayoutInflater.from(context).inflate(R.layout.category_activity_card_view, parent, false);
            viewHolder = new ViewHolder(view);
            return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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

    @Override
    public int getItemCount() {
        if (ref_list!=null&&progressBar!=null) {
            if (ref_list.size() == 0 && progressBar.getVisibility() == View.INVISIBLE) {
                progressBar.setVisibility(View.VISIBLE);

            } else if (ref_list.size() > 0 && (progressBar.getVisibility() == View.VISIBLE || progressBar.getVisibility() == View.INVISIBLE)) {
                progressBar.setVisibility(View.GONE);
            }
            return ref_list.size();
        }else{
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        CardView container;
        ImageView starImageOn;

        public ViewHolder(final View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.category_activity_card_image);
            container=(CardView)itemView.findViewById(R.id.category_activity_container);
           /* starImage=itemView.findViewById(R.id.starImage);

            starImageOn=itemView.findViewById(R.id.starImageOn);
            starImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    starImage.setVisibility(View.GONE);
                    starImageOn.setVisibility(View.VISIBLE);
                    Toast.makeText(itemView.getContext(),"Added to favorites",Toast.LENGTH_SHORT).show();
                }
            });
            starImageOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    starImage.setVisibility(View.VISIBLE);
                    starImageOn.setVisibility(View.GONE);
                    Toast.makeText(itemView.getContext(),"Removed from favorites",Toast.LENGTH_SHORT).show();
                }
            });*/
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i= new Intent(itemView.getContext(),FullScreenActivity.class);
                    String s=ref_list.get(getPosition()).toString().substring(28);
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
                    String a=ref_list.get(getPosition()).toString().substring(28);
                    i.putExtra("IMAGE_NAME",ref);
                    i.putExtra("PLACEHOLDER_IMAGE",a);

                    /*if (MainActivity.image_names.get(getPosition()).contains("cars")){
                        i.putExtra("CATEGORY_NAME","cars/");
                        buildRef(i,MainActivity.car_refs_thumb);
                    }else if (MainActivity.image_names.get(getPosition()).contains("landscape")){
                        i.putExtra("CATEGORY_NAME","landscape/");
                        buildRef(i,MainActivity.landscape_refs_thumb);
                    }else if (MainActivity.image_names.get(getPosition()).contains("abstract")){
                        i.putExtra("CATEGORY_NAME","abstract/");
                        buildRef(i,MainActivity.abstract_refs_thumb);
                    }else if (MainActivity.image_names.get(getPosition()).contains("minimal")){
                        i.putExtra("CATEGORY_NAME","minimal/");
                        buildRef(i,MainActivity.minimal_refs_thumb);
                    }else if (MainActivity.image_names.get(getPosition()).contains("material")){
                        i.putExtra("CATEGORY_NAME","material/");
                        buildRef(i,MainActivity.material_refs_thumb);
                    }else if (MainActivity.image_names.get(getPosition()).contains("black")){
                        i.putExtra("CATEGORY_NAME","black/");
                        buildRef(i,MainActivity.black_refs_thumb);
                    }else if (MainActivity.image_names.get(getPosition()).contains("nature")){
                        i.putExtra("CATEGORY_NAME","nature/");
                        buildRef(i,MainActivity.nature_refs_thumb);
                    }else if (MainActivity.image_names.get(getPosition()).contains("tech")){
                        i.putExtra("CATEGORY_NAME","technology/");
                        buildRef(i,MainActivity.technology_refs_thumb);
                    }else if (MainActivity.image_names.get(getPosition()).contains("animal")){
                        buildRef(i,MainActivity.animal_refs_thumb);
                    }else if (MainActivity.image_names.get(getPosition()).contains("amoled")){
                        buildRef(i,MainActivity.amoled_refs_thumb);
                    }else {
                        i.putExtra("CATEGORY_NAME","images/");
                    }*/
                    itemView.getContext().startActivity(i);
                }
            });

        }
        private void buildRef(Intent i,ArrayList<StorageReference> reference){

        }
    }
}
