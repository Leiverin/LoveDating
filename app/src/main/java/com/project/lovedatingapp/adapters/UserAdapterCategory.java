package com.project.lovedatingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.project.lovedatingapp.models.User;
import com.project.lovedatingapp.R;
import com.project.lovedatingapp.utils.Common;
import com.project.lovedatingapp.views.ShowDetailUserActivity;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapterCategory extends RecyclerView.Adapter<UserAdapterCategory.UserViewHolder> {
    private Context context;
    private List<User> list;

    public UserAdapterCategory(Context context, List<User> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @NotNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_category, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserAdapterCategory.UserViewHolder holder, int position) {
        User user = list.get(position);
        String upperString = user.getUsername().substring(0, 1).toUpperCase() + user.getUsername().substring(1).toLowerCase();
        holder.txtFullname.setText(upperString);
        if(Common.mListUserCategory != null){
            Glide.with(context).load(Common.mListUserCategory.get(position).getListImage().get(0).getUrl()).into(holder.profilePicc);
        }
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowDetailUserActivity.class);
                intent.putExtra("userId", user.getId());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        else
            return 0;
    }


    public class UserViewHolder extends RecyclerView.ViewHolder {
        private CardView rootLayout;
        private ImageView profilePicc;
        private TextView txtFullname;
        private TextView lastMessage;
        private TextView unseenMassage;


        public UserViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            rootLayout = itemView.findViewById(R.id.rootLayout);
            profilePicc = itemView.findViewById(R.id.profilePicc);
            txtFullname = itemView.findViewById(R.id.txtFullname);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            unseenMassage = itemView.findViewById(R.id.unseenMassage);
        }
    }
}
