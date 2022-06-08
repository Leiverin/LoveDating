package com.project.lovedatingapp.ui.me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.lovedatingapp.models.Post;
import com.project.lovedatingapp.R;

import java.util.List;


public class PhotoFragmentAdapter extends RecyclerView.Adapter<PhotoFragmentAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Post> mListPost;

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ImageViewHolder(view);
    }

    public PhotoFragmentAdapter(Context mContext, List<Post> mListPost) {
        this.mContext = mContext;
        this.mListPost = mListPost;
    }


    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Glide.with(mContext).load(mListPost.get(position).getImageLink()).into(holder.img);

    }

    @Override
    public int getItemCount() {
        if (mListPost != null) {
            return mListPost.size();
        }
        return 0;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);

        }
    }
}
