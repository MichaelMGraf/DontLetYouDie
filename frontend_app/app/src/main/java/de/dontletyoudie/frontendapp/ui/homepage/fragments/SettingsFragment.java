package de.dontletyoudie.frontendapp.ui.homepage.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.dontletyoudie.frontendapp.R;
import de.dontletyoudie.frontendapp.data.GlobalProperties;
import de.dontletyoudie.frontendapp.data.apiCalls.CallerStatics;
import de.dontletyoudie.frontendapp.data.apiCalls.DeleteAccountAPICaller;
import de.dontletyoudie.frontendapp.data.apiCalls.core.ActionAfterCall;
import de.dontletyoudie.frontendapp.ui.homepage.AddFriendsActivity;
import de.dontletyoudie.frontendapp.ui.login.LoginActivity;
import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private Button btn_delete_account;
    private DeleteAccountAPICaller deleteAccountAPICaller = new DeleteAccountAPICaller(this);
    private Context context;

    public SettingsFragment() {
    }


    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
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
        context = getContext();
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_settings,
                container, false);
        btn_delete_account = (Button) view.findViewById(R.id.btn_settings_deleteAccount);
        btn_delete_account.setOnClickListener(view1 -> {
            new AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to delete your account?")
                    .setNegativeButton("Cancel", ((dialogInterface, i) -> {}))
                    .setPositiveButton("Yes", (dialogInterface1, i1) -> {
                        Map<Integer, ActionAfterCall> actionAfterCall = new HashMap<>();
                        actionAfterCall.put(HttpsURLConnection.HTTP_NO_CONTENT, new ActionAfterCall() {
                            @Override
                            public void onSuccessfulCall(String responseBody, Headers headers, Context appContext) {
                                showMessage("Successfully deleted Account");
                                Intent intent = new Intent(getActivity().getApplication(), LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                        deleteAccountAPICaller.executeDELETE(CallerStatics.APIURL+"api/account/delete",
                                GlobalProperties.getInstance().userName, actionAfterCall);
                            }).show();
        });

        return view;
    }
    public void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
}