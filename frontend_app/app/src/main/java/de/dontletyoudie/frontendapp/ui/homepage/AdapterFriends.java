package de.dontletyoudie.frontendapp.ui.homepage;

import android.app.AlertDialog;
import android.content.Context;
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
import de.dontletyoudie.frontendapp.data.GlobalProperties;
import de.dontletyoudie.frontendapp.data.apiCalls.CallerStatics;
import de.dontletyoudie.frontendapp.data.apiCalls.DeleteFriendsAPICaller;
import de.dontletyoudie.frontendapp.data.apiCalls.core.ActionAfterCall;
import de.dontletyoudie.frontendapp.data.dto.FriendDto;
import de.dontletyoudie.frontendapp.ui.homepage.fragments.FriendsFragment;
import okhttp3.Headers;

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

        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to remove " + holder.tvName.getText() + " from your friend list?")
                    .setNegativeButton("Cancel", ((dialogInterface, i) -> {}))
                    .setPositiveButton("Yes", (dialogInterface1, i1) -> {
                        Map<Integer, ActionAfterCall> actionAfterCall = new HashMap<>();
                        actionAfterCall.put(HttpsURLConnection.HTTP_NO_CONTENT, new ActionAfterCall() {
                            @Override
                            public void onSuccessfulCall(String responseBody, Headers headers, Context appContext) {
                                int removedPosition = holder.getBindingAdapterPosition();
                                showMessage("Successfully deleted Friend");
                                deleteFriend(removedPosition);
                                notifyItemRemoved(removedPosition);
                            }
                        });

                        DeleteFriendsAPICaller deleteFriendsAPICaller = new DeleteFriendsAPICaller(this);
                        deleteFriendsAPICaller.executeDELETE(CallerStatics.APIURL+"api/relationship/delete",
                                GlobalProperties.getInstance().userName, holder.tvName.getText().toString(),
                                actionAfterCall);
                    })
                    .show();

        });
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        Button deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById((R.id.tv_friends_nameFriend));
            deleteButton = itemView.findViewById(R.id.button_friends_deleteFriend);
        }
    }

    public void showMessage(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
