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

public class AdapterFriendRequests extends RecyclerView.Adapter<AdapterFriendRequests.ViewHolder> {
    Context context;
    public List<FriendDto> requestList;

    public AdapterFriendRequests(Context context, List<FriendDto> requestList) {
        this.context = context;
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public AdapterFriendRequests.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_friend_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFriendRequests.ViewHolder holder, int position) {
        holder.tvName.setText(requestList.get(position).getName());
        System.out.println(position);
        System.out.println(requestList.toString());
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById((R.id.tv_friends_nameFriendRequest));
        }
    }
}
