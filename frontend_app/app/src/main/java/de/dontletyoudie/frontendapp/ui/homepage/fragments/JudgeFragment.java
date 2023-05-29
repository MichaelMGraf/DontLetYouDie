package de.dontletyoudie.frontendapp.ui.homepage.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.dontletyoudie.frontendapp.R;
import de.dontletyoudie.frontendapp.data.GlobalProperties;
import de.dontletyoudie.frontendapp.data.apiCalls.JudgeProofAPICaller;
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
    private JudgeProofAPICaller judgeProofAPICaller = new JudgeProofAPICaller(this);

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
        button_accept.setVisibility(View.INVISIBLE);
        button_deny.setVisibility(View.INVISIBLE);
        button_accept.setEnabled(false);
        button_deny.setEnabled(false);

        getProofAPICaller.executeGET(CallerStatics.APIURL+"api/proof/getPending", GlobalProperties.getInstance().userName);

        button_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judgeProofAPICaller.executePOST(
                        CallerStatics.APIURL+"api/judgement/add",
                        GlobalProperties.getInstance().userName,
                        proof.getProofId(),
                        true);
            }
        });

        button_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judgeProofAPICaller.executePOST(
                        CallerStatics.APIURL+"api/judgement/add",
                        GlobalProperties.getInstance().userName,
                        proof.getProofId(),
                        false);
                        }
        });
        return view;
    }

    public void displayProof (ProofGetDto proof) {
        button_accept.setVisibility(View.VISIBLE);
        button_deny.setVisibility(View.VISIBLE);
        button_accept.setEnabled(true);
        button_deny.setEnabled(true);
        tv_username.setText(proof.getUsername());
        tv_category.setText(proof.getCategory());
        Bitmap bitmap = BitmapFactory.decodeByteArray(proof.getImage(), 0, proof.getImage().length);
        iv_proof_image.setImageBitmap(bitmap);
        this.proof = proof;
    }

    public void noProofFound () {
        tv_username.setText("No proofs yet");
        tv_category.setText(" ");
        iv_proof_image.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.symbol_photo));
        button_deny.setEnabled(false);
        button_accept.setEnabled(false);
        button_deny.setVisibility(View.INVISIBLE);
        button_accept.setVisibility(View.INVISIBLE);
    }

    public void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    public GetProofAPICaller getGetProofAPICaller() {
        return getProofAPICaller;
    }
}