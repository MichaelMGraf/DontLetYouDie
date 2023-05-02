package de.dontletyoudie.frontendapp.ui.homepage.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import de.dontletyoudie.frontendapp.R;
import de.dontletyoudie.frontendapp.data.GlobalProperties;
import de.dontletyoudie.frontendapp.data.apiCalls.CallerStatics;
import de.dontletyoudie.frontendapp.data.apiCalls.GetProofAPICaller;
import de.dontletyoudie.frontendapp.data.dto.ProofGetDto;

public class JudgeFragment extends Fragment {

    public JudgeFragment() {
        // Required empty public constructor
    }

    private Button button_accept;
    private Button button_deny;
    private TextView tv_username;
    private TextView tv_category;
    private ImageView iv_proof_image;

    private ProofGetDto proof;

    private GetProofAPICaller getProofAPICaller = new GetProofAPICaller(this);

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

        getProofAPICaller.executeGET(CallerStatics.APIURL+"api/proof/getPending", GlobalProperties.getInstance().userName);
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

    public void displayProof (ProofGetDto proof) {
        tv_username.setText(proof.getUsername());
        tv_category.setText(proof.getCategory());
        Bitmap bitmap = BitmapFactory.decodeByteArray(proof.getImage(), 0, proof.getImage().length);
        iv_proof_image.setImageBitmap(bitmap);
    }

    public void noProofFound () {

    }
}