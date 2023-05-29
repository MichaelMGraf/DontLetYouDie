package de.dontletyoudie.frontendapp.ui.homepage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.dontletyoudie.frontendapp.R;
import de.dontletyoudie.frontendapp.data.GloablStuff;
import de.dontletyoudie.frontendapp.data.GlobalProperties;
import de.dontletyoudie.frontendapp.data.apiCalls.AcceptFriendsAPICaller;
import de.dontletyoudie.frontendapp.data.apiCalls.AddFriendsAPICaller;
import de.dontletyoudie.frontendapp.data.apiCalls.CallerStatics;
import de.dontletyoudie.frontendapp.data.apiCalls.DenyFriendsAPICaller;
import de.dontletyoudie.frontendapp.data.apiCalls.core.ActionAfterCall;
import de.dontletyoudie.frontendapp.data.dto.FriendDto;
import de.dontletyoudie.frontendapp.ui.homepage.fragments.FriendsFragment;
import okhttp3.Headers;

public class AdapterFriendRequests extends RecyclerView.Adapter<AdapterFriendRequests.ViewHolder> {
    Context context;
    private final List<FriendDto> requestList;

    private final FriendsFragment friendsFragment;

    private final RecyclerView recyclerView;
    private boolean buttonsInvisible = false;

    public AdapterFriendRequests(Context context, List<FriendDto> requestList, FriendsFragment friendsFragment, RecyclerView recyclerView) {
        this.context = context;
        this.requestList = requestList;
        this.friendsFragment = friendsFragment;
        this.recyclerView = recyclerView;
    }

    public FriendsFragment getFriendsFragment() {
        return friendsFragment;
    }

    public Context getContext() {
        return context;
    }

    public void addFriend(String friendName) {
        FriendDto newFriend = new FriendDto(friendName);
        requestList.add(newFriend);
    }

    public void deleteFriend(int friendPosition) {
        requestList.remove(friendPosition);
        if (requestList.isEmpty()) {
            friendsFragment.noFriendRequestsYet();
        }
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
        String relName = holder.tvName.getText().toString();

        holder.buttonDeny.setOnClickListener(v -> {
            Map<Integer, ActionAfterCall> actionAfterDenyCall = new HashMap<>();
            actionAfterDenyCall.put(HttpsURLConnection.HTTP_NO_CONTENT, new ActionAfterCall() {
                @Override
                public void onSuccessfulCall(String responseBody, Headers headers, Context appContext) {
                    int removedPosition = holder.getBindingAdapterPosition();
                    AdapterFriendRequests.this.showMessage("Successfully denied Friend");
                    AdapterFriendRequests.this.deleteFriend(removedPosition);
                    notifyItemRemoved(removedPosition);
                }
            });
            DenyFriendsAPICaller denyFriendsAPICaller = new DenyFriendsAPICaller(this);
            denyFriendsAPICaller.executeDELETE(CallerStatics.APIURL+"api/relationship/delete", GlobalProperties.getInstance().userName, relName, actionAfterDenyCall);
        });
        holder.buttonAccept.setOnClickListener(v -> {
            Map<Integer, ActionAfterCall> actionAfterAcceptCall = new HashMap<>();
            actionAfterAcceptCall.put(HttpsURLConnection.HTTP_NO_CONTENT, new ActionAfterCall() {
                @Override
                public void onSuccessfulCall(String responseBody, Headers headers, Context appContext) {
                    int removedPosition = holder.getBindingAdapterPosition();
                    AdapterFriendRequests.this.showMessage("Successfully accepted Friend");
                    AdapterFriendRequests.this.deleteFriend(removedPosition);
                    notifyItemRemoved(removedPosition);
                    AdapterFriendRequests.this.getFriendsFragment().getAdapterFriends().addFriend(relName);
                }
            });
            AcceptFriendsAPICaller acceptFriendsAPICaller = new AcceptFriendsAPICaller(this);
            acceptFriendsAPICaller.executePUT(CallerStatics.APIURL+"api/relationship/accept", GlobalProperties.getInstance().userName, relName, actionAfterAcceptCall);
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        Button buttonDeny;
        Button buttonAccept;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById((R.id.tv_friends_nameFriendRequest));
            buttonDeny = itemView.findViewById((R.id.deny_button));
            buttonAccept = itemView.findViewById((R.id.accept_button));
            if (buttonsInvisible) {
                buttonDeny.setVisibility(View.INVISIBLE);
                buttonAccept.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void setButtonsInvisible () {
        buttonsInvisible = true;
    }
}
