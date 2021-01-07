package ru.nsk.nsu.sosed.representation.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sosed.R;

import java.util.List;

import ru.nsk.nsu.sosed.data.profile.ProfileEntity;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<ProfileEntity> mUsers;

    public UserAdapter(Context mContext, List<ProfileEntity> mUsers){
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProfileEntity user = mUsers.get(position);
        holder.username.setText(user.getName());

        holder.profile_image.setImageResource(R.drawable.ic_baseline_account_box_24);


       /* if (imageUrl.equals("default")){
            holder.profile_image.setImageResource(R.drawable.ic_baseline_account_box_24);
        }else{
            //download_image();
            StorageReference profileRef  = storageReference.child("images/profiles/" + user.getImageUrl());
            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(mContext).load(imageUrl).into(holder.profile_image);
                }
            });
        }*/
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_image;

        public ViewHolder(View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);

        }
    }

}
