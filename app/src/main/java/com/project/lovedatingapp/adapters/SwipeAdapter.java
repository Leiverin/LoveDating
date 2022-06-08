package com.project.lovedatingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.project.lovedatingapp.R;
import com.project.lovedatingapp.interfaces.IOnClickCard;
import com.project.lovedatingapp.models.User;

import java.util.List;

public class SwipeAdapter extends BaseAdapter {
    private Context mContext;
    private List<User> mListUser;
    private IOnClickCard onClickCard;

    public SwipeAdapter(Context mContext, List<User> mListUser, IOnClickCard onClickCard) {
        this.mContext = mContext;
        this.mListUser = mListUser;
        this.onClickCard = onClickCard;
    }

    @Override
    public int getCount() {
        if (mListUser != null) {
            return mListUser.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder{
        public ShapeableImageView imgCard;
        public ImageButton imbDelete;
        public ImageButton imbMessage;
        public ImageButton imbLike;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
            initView(view, holder);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        User user = mListUser.get(i);
//        Glide.with(mContext).load(user.getImages().get(0)).into(holder.imgCard);
        holder.imbLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCard.onClickBtnLike(user);
            }
        });
        holder.imbDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCard.onClickBtnDelete(user);
            }
        });
        holder.imbMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCard.onClickBtnMessage(user);
            }
        });
        return view;
    }

    private void initView(View view, ViewHolder holder) {
        holder.imgCard = (ShapeableImageView) view.findViewById(R.id.img_card);
        holder.imbDelete = (ImageButton) view.findViewById(R.id.imb_delete);
        holder.imbMessage = (ImageButton) view.findViewById(R.id.imb_message);
        holder.imbLike = (ImageButton) view.findViewById(R.id.imb_like);

    }
}
