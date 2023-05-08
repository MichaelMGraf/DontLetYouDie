package de.dontletyoudie.frontendapp.ui.homepage.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import de.dontletyoudie.frontendapp.R;
import de.dontletyoudie.frontendapp.data.GlobalProperties;
import de.dontletyoudie.frontendapp.data.apiCalls.CallerStatics;
import de.dontletyoudie.frontendapp.data.apiCalls.FetchFriendsAPICaller;
import de.dontletyoudie.frontendapp.data.apiCalls.FetchFriendRequestsAPICaller;
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
    private AdapterFriends adapterFriends;
    private AdapterFriendRequests adapterFriendRequests;

    public AdapterFriends getAdapterFriends() {
        return adapterFriends;
    }

    public AdapterFriendRequests getAdapterFriendRequests() {
        return adapterFriendRequests;
    }

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
        adapterFriends = new AdapterFriends(getContext(), friendList.getStringList().stream().map(FriendDto::new).collect(Collectors.toList()), this);
        recyclerView.setAdapter(adapterFriends);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable verticalDivider = ContextCompat.getDrawable(requireContext(), R.drawable.card_divider);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(verticalDivider));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public void fillAdapterFriendRequestsWithList (FriendListDto requestList) {
        RecyclerView recyclerViewRequests = (RecyclerView) getView().findViewById(R.id.rv_friend_listFriendRequests);
        adapterFriendRequests = new AdapterFriendRequests(getContext(), requestList.getStringList().stream().map(FriendDto::new).collect(Collectors.toList()), this, recyclerViewRequests);
        recyclerViewRequests.setAdapter(adapterFriendRequests);
        recyclerViewRequests.setLayoutManager(new LinearLayoutManager(view.getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewRequests.getContext(), DividerItemDecoration.VERTICAL);
        Drawable verticalDivider = ContextCompat.getDrawable(requireContext(), R.drawable.card_divider);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(verticalDivider));
        recyclerViewRequests.addItemDecoration(dividerItemDecoration);
    }

    public void noFriendsYet () {
        List<FriendDto> MOFList = new ArrayList<>();
        FriendDto MOFElement = new FriendDto("Currently no friends");
        MOFList.add(MOFElement);

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.rv_friend_listFriends);
        AdapterFriends adapter = new AdapterFriends(getContext(), MOFList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable verticalDivider = ContextCompat.getDrawable(requireContext(), R.drawable.card_divider);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(verticalDivider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter.setButtonsInvisible();
    }

    public void noFriendRequestsYet () {
        List<FriendDto> MOFRList = new ArrayList<>();
        FriendDto MOFRElement = new FriendDto("Currently no friend requests");
        MOFRList.add(MOFRElement);

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.rv_friend_listFriendRequests);
        AdapterFriendRequests adapter = new AdapterFriendRequests(getContext(), MOFRList, this, recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable verticalDivider = ContextCompat.getDrawable(requireContext(), R.drawable.card_divider);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(verticalDivider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter.setButtonsInvisible();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        fetchFriendsAPICaller.executeGET(CallerStatics.APIURL+"api/relationship/getFriends", GlobalProperties.getInstance().userName);
        fetchFriendRequestsAPICaller.executeGET(CallerStatics.APIURL+"api/relationship/getPendingFriendRequests", GlobalProperties.getInstance().userName);
        super.onViewCreated(view, savedInstanceState);
    }
}