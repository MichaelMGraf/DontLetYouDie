package de.dontletyoudie.frontendapp.ui.homepage.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.dontletyoudie.frontendapp.R;
import de.dontletyoudie.frontendapp.data.GlobalProperties;
import de.dontletyoudie.frontendapp.data.apiCalls.CallerStatics;
import de.dontletyoudie.frontendapp.data.apiCalls.DeleteAccountAPICaller;
import de.dontletyoudie.frontendapp.data.apiCalls.core.ActionAfterCall;
import de.dontletyoudie.frontendapp.data.apiCalls.core.TokenEntity;
import de.dontletyoudie.frontendapp.ui.homepage.TakePictureActivity;
import de.dontletyoudie.frontendapp.ui.login.LoginActivity;
import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private Button btn_delete_account;
    private TextView tv_username;
    private Button btn_logout;
    private Button btn_blog;
    private Button btn_about;

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
        btn_logout = (Button) view.findViewById(R.id.btn_settings_logout);
        btn_blog = (Button) view.findViewById(R.id.btn_settings_blog);
        btn_about = (Button) view.findViewById(R.id.btn_settings_about);
        tv_username = view.findViewById(R.id.tv_settings_username);

        tv_username.setText(GlobalProperties.getInstance().userName);

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

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        btn_blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToBlog();
            }
        });

        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAboutPage();
            }
        });

        return view;
    }
    public void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void logout() {
        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TokenEntity.deleteTokens();
        showMessage("good bye, " + GlobalProperties.getInstance().userName);
        startActivity(i);
    }

    public void navigateToAboutPage() {
        String url = "https://github.com/MichaelMGraf/DontLetYouDie";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void navigateToBlog() {
        String url = "https://dontletyoudie.wordpress.com/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}