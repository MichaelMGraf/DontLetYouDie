package de.dontletyoudie.frontendapp.ui.homepage.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.stream.Collectors;

import de.dontletyoudie.frontendapp.R;
import de.dontletyoudie.frontendapp.data.GlobalProperties;
import de.dontletyoudie.frontendapp.data.apiCalls.CallerStatics;
import de.dontletyoudie.frontendapp.data.apiCalls.FetchFriendsAPICaller;
import de.dontletyoudie.frontendapp.data.apiCalls.core.FetchFriendRequestsAPICaller;
import de.dontletyoudie.frontendapp.data.dto.FriendDto;
import de.dontletyoudie.frontendapp.data.dto.FriendListDto;
import de.dontletyoudie.frontendapp.ui.homepage.AdapterFriendRequests;
import de.dontletyoudie.frontendapp.ui.homepage.AdapterFriends;
import de.dontletyoudie.frontendapp.ui.homepage.AddFriendsActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends Fragment {

    private View view;

    FetchFriendsAPICaller fetchFriendsAPICaller = new FetchFriendsAPICaller(this);
    FetchFriendRequestsAPICaller fetchFriendRequestsAPICaller = new FetchFriendRequestsAPICaller(this);



    private Button btn_add_friends;

    public FriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_friends,
                container, false);
        btn_add_friends = (Button) view.findViewById(R.id.btn_home_addFriends);
        btn_add_friends.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity().getApplication(), AddFriendsActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void fillAdapterFriendsWithList (FriendListDto friendList) {
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.rv_friend_listFriends);
        AdapterFriends adapter = new AdapterFriends(getContext(), friendList.getStringList().stream().map(FriendDto::new).collect(Collectors.toList()));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public void fillAdapterFriendRequestsWithList (FriendListDto requestList) {
        RecyclerView recyclerViewRequests = (RecyclerView) getView().findViewById(R.id.rv_friend_listFriendRequests);
        AdapterFriendRequests adapterRequests = new AdapterFriendRequests(getContext(), requestList.getStringList().stream().map(FriendDto::new).collect(Collectors.toList()));
        recyclerViewRequests.setAdapter(adapterRequests);
        recyclerViewRequests.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public void noFriendsYet () {

    }

    public void noFriendRequestsYet () {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        fetchFriendsAPICaller.executeGET(CallerStatics.APIURL+"api/relationship/getFriends", GlobalProperties.getInstance().userName);
        fetchFriendRequestsAPICaller.executeGET(CallerStatics.APIURL+"api/relationship/getPendingFriendRequests", GlobalProperties.getInstance().userName);
        super.onViewCreated(view, savedInstanceState);
    }
}