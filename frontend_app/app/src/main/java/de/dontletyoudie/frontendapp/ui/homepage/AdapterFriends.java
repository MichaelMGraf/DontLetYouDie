package de.dontletyoudie.frontendapp.ui.homepage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.dontletyoudie.frontendapp.R;
import de.dontletyoudie.frontendapp.data.dto.FriendDto;

public class AdapterFriends extends RecyclerView.Adapter<AdapterFriends.ViewHolder> {

    Context context;
    public FriendDto[] friendList;

    public AdapterFriends(Context context, FriendDto[] friendList) {
        this.context = context;
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public AdapterFriends.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFriends.ViewHolder holder, int position) {
        holder.tvName.setText(friendList[position].getName());
        System.out.println(position);
        System.out.println(friendList.toString());
    }

    @Override
    public int getItemCount() {
        return friendList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById((R.id.tv_friends_nameFriend));
        }
    }
}
