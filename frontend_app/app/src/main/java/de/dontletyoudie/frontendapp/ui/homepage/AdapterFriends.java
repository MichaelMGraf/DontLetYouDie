package de.dontletyoudie.frontendapp.ui.homepage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.dontletyoudie.frontendapp.R;
import de.dontletyoudie.frontendapp.data.dto.FriendDto;
import de.dontletyoudie.frontendapp.ui.homepage.fragments.FriendsFragment;

public class AdapterFriends extends RecyclerView.Adapter<AdapterFriends.ViewHolder> {
    Context context;
    public List<FriendDto> friendList;

    private final FriendsFragment friendsFragment;

    public AdapterFriends(Context context, List<FriendDto> friendList, FriendsFragment friendsFragment) {
        this.context = context;
        this.friendList = friendList;
        this.friendsFragment = friendsFragment;
    }

    public FriendsFragment getFriendsFragment() {
        return friendsFragment;
    }

    public void addFriend(String friendName) {
        FriendDto newFriend = new FriendDto(friendName);
        friendList.add(newFriend);
        notifyItemInserted(friendList.size());
    }

    public void deleteFriend(int friendPosition) {
        friendList.remove(friendPosition);
        if (friendList.isEmpty()) {
            friendsFragment.noFriendsYet();
        }
    }

    @NonNull
    @Override
    public AdapterFriends.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFriends.ViewHolder holder, int position) {
        holder.tvName.setText(friendList.get(position).getName());
        System.out.println(position);
        System.out.println(friendList.toString());
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById((R.id.tv_friends_nameFriend));
        }
    }
}
