package com.whoops.wallroachwallpapers;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import java.util.ArrayList;

public class CategoryTabRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    View view;
    ViewHolder viewHolder;
    ArrayList<String> category_list;
    Context context;
    private int lastPosition=0;


    public CategoryTabRecyclerAdapter(Context context, ArrayList<String> list){
        this.context=context;
        category_list=list;


    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==-1){
            view=LayoutInflater.from(context).inflate(R.layout.blank_margin,parent,false);
            BlankSpaceViewHolder blankSpaceViewHolder=new BlankSpaceViewHolder(view);
            return blankSpaceViewHolder;
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.category_card, parent, false);
            viewHolder = new ViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        viewHolder.categ_card_text.setText(category_list.get(position));
        viewHolder.setIsRecyclable(false);
        if (MainActivity.motivational_refs_thumb.size()>0) {
            if (category_list.get(position).equalsIgnoreCase("abstract")) {
                if (MainActivity.abstract_refs_thumb.size()>0)
                Glide.with(context).using(new FirebaseImageLoader()).load(MainActivity.abstract_refs_thumb.get(0)).centerCrop().into(viewHolder.categ_card_image);
            }else if (category_list.get(position).equalsIgnoreCase("amoled")) {
                if (MainActivity.amoled_refs_thumb.size()>0)
                Glide.with(context).using(new FirebaseImageLoader()).load(MainActivity.amoled_refs_thumb.get(1)).centerCrop().into(viewHolder.categ_card_image);
            }else if (category_list.get(position).equalsIgnoreCase("black")) {
                if (MainActivity.black_refs_thumb.size()>0)
                Glide.with(context).using(new FirebaseImageLoader()).load(MainActivity.black_refs_thumb.get(0)).centerCrop().into(viewHolder.categ_card_image);
            }else if (category_list.get(position).equalsIgnoreCase("cars")) {
                if (MainActivity.car_refs_thumb.size()>0)
                Glide.with(context).using(new FirebaseImageLoader()).load(MainActivity.car_refs_thumb.get(0)).centerCrop().into(viewHolder.categ_card_image);
            }else if (category_list.get(position).equalsIgnoreCase("landscape")) {
                if (MainActivity.landscape_refs_thumb.size()>0)
                Glide.with(context).using(new FirebaseImageLoader()).load(MainActivity.landscape_refs_thumb.get(31)).centerCrop().into(viewHolder.categ_card_image);
            }else if (category_list.get(position).equalsIgnoreCase("motivational")) {
                if (MainActivity.motivational_refs_thumb.size()>0)
                Glide.with(context).using(new FirebaseImageLoader()).load(MainActivity.motivational_refs_thumb.get(3)).centerCrop().into(viewHolder.categ_card_image);
            }else if (category_list.get(position).equalsIgnoreCase("misc")) {
                if (MainActivity.funny_refs_thumb.size()>0)
                Glide.with(context).using(new FirebaseImageLoader()).load(MainActivity.funny_refs_thumb.get(0)).centerCrop().into(viewHolder.categ_card_image);
            }else if (category_list.get(position).equalsIgnoreCase("animals")) {
                if (MainActivity.animal_refs_thumb.size()>0)
                Glide.with(context).using(new FirebaseImageLoader()).load(MainActivity.animal_refs_thumb.get(0)).centerCrop().into(viewHolder.categ_card_image);
            }else if (category_list.get(position).equalsIgnoreCase("material")) {
                if (MainActivity.material_refs_thumb.size()>0)
                Glide.with(context).using(new FirebaseImageLoader()).load(MainActivity.material_refs_thumb.get(0)).centerCrop().into(viewHolder.categ_card_image);
            }else if (category_list.get(position).equalsIgnoreCase("minimal")) {
                if (MainActivity.minimal_refs_thumb.size()>0)
                Glide.with(context).using(new FirebaseImageLoader()).load(MainActivity.minimal_refs_thumb.get(15)).centerCrop().into(viewHolder.categ_card_image);
            }else if (category_list.get(position).equalsIgnoreCase("superhero")) {
                if (MainActivity.superhero_refs_thumb.size()>0)
                Glide.with(context).using(new FirebaseImageLoader()).load(MainActivity.superhero_refs_thumb.get(26)).centerCrop().into(viewHolder.categ_card_image);
            }else if (category_list.get(position).equalsIgnoreCase("space")) {
                if (MainActivity.space_refs_thumb.size()>0)
                Glide.with(context).using(new FirebaseImageLoader()).load(MainActivity.space_refs_thumb.get(8)).centerCrop().into(viewHolder.categ_card_image);
            }else if (category_list.get(position).equalsIgnoreCase("city")) {
                if (MainActivity.space_refs_thumb.size()>0)
                    Glide.with(context).using(new FirebaseImageLoader()).load(MainActivity.city_refs_thumb.get(13)).centerCrop().into(viewHolder.categ_card_image);
            }
        }

    }

    @Override
    public int getItemCount() {
        return category_list.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView categ_card_image;
        TextView categ_card_text;

        public ViewHolder(final View itemView) {
            super(itemView);
            categ_card_text=itemView.findViewById(R.id.category_card_text);
            categ_card_image=itemView.findViewById(R.id.category_card_image);
            categ_card_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(itemView.getContext(),CategoryActivity.class);
                    intent.putExtra("CATEGORY_NAME",categ_card_text.getText().toString().toLowerCase());
                       /* if (categ_card_text.getText().toString().equalsIgnoreCase("abstract")){
                            intent.putExtra("CATEGORY_NAME","abstract");
                        }else if (categ_card_text.getText().toString().equalsIgnoreCase("amoled")){
                            intent.putExtra("CATEGORY_NAME","amoled");
                        }else if (categ_card_text.getText().toString().equalsIgnoreCase("landscape")){
                            intent.putExtra("CATEGORY_NAME","landscape");
                        }else if (categ_card_text.getText().toString().equalsIgnoreCase("animals")){
                            intent.putExtra("CATEGORY_NAME","animal");
                        }else if (categ_card_text.getText().toString().equalsIgnoreCase("black")){
                            intent.putExtra("CATEGORY_NAME","black");
                        }else if (categ_card_text.getText().toString().equalsIgnoreCase("cars")){
                            intent.putExtra("CATEGORY_NAME","cars");
                        }else if (categ_card_text.getText().toString().equalsIgnoreCase("material")){
                            intent.putExtra("CATEGORY_NAME","material");
                        }else if (categ_card_text.getText().toString().equalsIgnoreCase("minimal")){
                            intent.putExtra("CATEGORY_NAME","minimal");
                        }else if (categ_card_text.getText().toString().equalsIgnoreCase("space")){
                            intent.putExtra("CATEGORY_NAME","space");
                        }else if (categ_card_text.getText().toString().equalsIgnoreCase("superhero")){
                            intent.putExtra("CATEGORY_NAME","superhero");
                        }else if (categ_card_text.getText().toString().equalsIgnoreCase("technology")){
                            intent.putExtra("CATEGORY_NAME","technology");
                        }else if (categ_card_text.getText().toString().equalsIgnoreCase("funny")){
                            intent.putExtra("CATEGORY_NAME","funny");
                        }else if (categ_card_text.getText().toString().equalsIgnoreCase("motivational")){
                            intent.putExtra("CATEGORY_NAME","motivational");
                        }else if (categ_card_text.getText().toString().equalsIgnoreCase("city")){
                            intent.putExtra("CATEGORY_NAME","city");
                        }*/
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    public static class BlankSpaceViewHolder extends RecyclerView.ViewHolder{
        public BlankSpaceViewHolder(View itemView) {
            super(itemView);
        }
    }
}
