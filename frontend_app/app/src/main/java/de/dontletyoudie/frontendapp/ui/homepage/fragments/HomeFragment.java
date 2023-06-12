package de.dontletyoudie.frontendapp.ui.homepage.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Map;
import java.util.Objects;

import de.dontletyoudie.frontendapp.R;
import de.dontletyoudie.frontendapp.data.GlobalProperties;
import de.dontletyoudie.frontendapp.data.apiCalls.CallerStatics;
import de.dontletyoudie.frontendapp.data.apiCalls.FetchStatsAPICaller;
import de.dontletyoudie.frontendapp.ui.homepage.AdapterStats;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment{

    private Button btn_take_photo;
    private TextView statTitle;
    private ProgressBar progressBar;
    private ImageView miniMeImage;

    private AdapterStats adapterStats;
    private RecyclerView recyclerViewHome;

    private View view;

    FetchStatsAPICaller fetchStatsAPICaller = new FetchStatsAPICaller(this);

    public HomeFragment() {}

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        View view = inflater.inflate(R.layout.fragment_home,
                container, false);
        miniMeImage = view.findViewById(R.id.img_home_miniMe);
        return view;
    }

    public void fillAdapterStatsWithData(Map<String, Integer> stats) {
        recyclerViewHome = (RecyclerView) getView().findViewById(R.id.rv_home_stats);
        adapterStats = new AdapterStats(getContext(), this, stats);
        recyclerViewHome.setAdapter(adapterStats);
        recyclerViewHome.setLayoutManager(new LinearLayoutManager(view.getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewHome.getContext(), DividerItemDecoration.VERTICAL);
        Drawable verticalDivider = ContextCompat.getDrawable(requireContext(), R.drawable.card_divider);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(verticalDivider));
        recyclerViewHome.addItemDecoration(dividerItemDecoration);
    }

    public void changeMiniMeVersion(Map<String, Integer> stats) {
        stats.put("fitness", 23);
        //Log.d("hellothere", "fitness: " + stats.get("fitness").toString());
        if(stats.get("fitness") < 10) { //if fitness is below 10
            miniMeImage.setImageResource(R.drawable.minime_fat);
        } else if(stats.get("fitness") < 20) { //if fitness is between 10 and 20
            miniMeImage.setImageResource(R.drawable.minime_skinny);
        } else { //if fitness is over 20
            miniMeImage.setImageResource(R.drawable.minime_trained);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        //executing API call
        fetchStatsAPICaller.executeGet(CallerStatics.APIURL + "api/stats/getStats", GlobalProperties.getInstance().userName);
        super.onViewCreated(view, savedInstanceState);
    }
}