package com.project.lovedatingapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.lovedatingapp.R;

import java.io.IOException;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private Context mContext;
    private List<Uri> mListPhoto;

    public PhotoAdapter(Context mContext, List<Uri> mListPhoto) {
        this.mContext = mContext;
        this.mListPhoto = mListPhoto;
    }

    public PhotoAdapter(Context mContext) {
        this.mContext = mContext;
    }



    public void setData(List<Uri> list) {
        this.mListPhoto = list;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_post, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Uri uri = mListPhoto.get(position);
        if (uri == null) {
            return;
        }
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
//            Log.d("aaa", String.valueOf(bitmap));
            if (bitmap != null) {
                holder.img.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            Toast.makeText(mContext, "Please select again image", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (mListPhoto != null) {
            return mListPhoto.size();
        }
        return 0;
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }
}
