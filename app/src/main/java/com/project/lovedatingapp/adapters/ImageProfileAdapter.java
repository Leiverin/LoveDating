package com.project.lovedatingapp.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.imageview.ShapeableImageView;
import com.project.lovedatingapp.R;
import com.project.lovedatingapp.interfaces.IOnClickImageProfile;
import com.project.lovedatingapp.models.Image;

import java.util.List;

public class ImageProfileAdapter extends RecyclerView.Adapter<ImageProfileAdapter.ImageProfileViewHolder> {
    private Context mContext;
    private List<Image> mListImage;
    private IOnClickImageProfile onClickImageProfile;
    private boolean isLoaded = false;

    public ImageProfileAdapter(Context mContext, List<Image> mListImage, IOnClickImageProfile onClickImageProfile) {
        this.mContext = mContext;
        this.mListImage = mListImage;
        this.onClickImageProfile = onClickImageProfile;
        notifyDataSetChanged();
    }

    @Override
    public ImageProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_profile, parent, false);
        return new ImageProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageProfileViewHolder holder, int position) {
        Image image = mListImage.get(position);
        if(image == null){
            return;
        }else{
            holder.prgLoadImg.setVisibility(View.VISIBLE);
            isLoaded = false;
            if(image.getUri() != null){
                Glide.with(mContext)
                        .load(image.getUri())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Toast.makeText(mContext, "Failed to load image", Toast.LENGTH_SHORT).show();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.prgLoadImg.setVisibility(View.GONE);
                                isLoaded = true;
                                return false;
                            }
                        }).into(holder.imgProfile);
            }else{
                Glide.with(mContext)
                     .load(image.getUrl())
                     .listener(new RequestListener<Drawable>() {
                         @Override
                         public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                             Toast.makeText(mContext, "Failed to load image", Toast.LENGTH_SHORT).show();
                             return false;
                         }

                         @Override
                         public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                             holder.prgLoadImg.setVisibility(View.GONE);
                             isLoaded = true;
                             return false;
                         }
                     }).into(holder.imgProfile);
            }



            holder.imgProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isLoaded == true){
                        onClickImageProfile.OnClickImage(position, image);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int limit = 6;
        if(mListImage != null && mListImage.size() > 6){
            return limit;
        }else if(mListImage != null){
            return mListImage.size();
        }
        return limit;
    }

    public class ImageProfileViewHolder extends RecyclerView.ViewHolder {

        private ShapeableImageView imgProfile;
        private SpinKitView prgLoadImg;

        public ImageProfileViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View view) {
            imgProfile = (ShapeableImageView) view.findViewById(R.id.img_profile);
            prgLoadImg = (SpinKitView) view.findViewById(R.id.prg_load_img);
        }
    }
}
