package de.dontletyoudie.frontendapp.ui.homepage.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import de.dontletyoudie.frontendapp.R;

public class JudgeFragment extends Fragment {

    public JudgeFragment() {
        // Required empty public constructor
    }

    private Button button_accept;
    private Button button_deny;
    private TextView tv_username;
    private TextView tv_category;
    private ImageView iv_proof_image;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_judge,
                container, false);
        // Inflate the layout for this fragment
        button_accept = view.findViewById(R.id.button_judge_accept);
        button_deny = view.findViewById(R.id.button_judge_deny);
        tv_username = view.findViewById(R.id.tv_judge_proof_src_username);
        tv_category = view.findViewById(R.id.tv_jugde_category);
        iv_proof_image = view.findViewById(R.id.iv_judge_proof_image);

        button_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        button_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
        return view;
    }
}