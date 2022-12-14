package de.dontletyoudie.frontendapp.ui.homepage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import de.dontletyoudie.frontendapp.R;
import de.dontletyoudie.frontendapp.data.GloablStuff;
import de.dontletyoudie.frontendapp.data.apiCalls.AddFriendsAPICaller;

public class AddFriendsActivity extends AppCompatActivity {

    private Button btn_search;
    private TextView tf_searchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        btn_search = (Button) findViewById(R.id.btn_addFriends_search);
        tf_searchField = (TextView) findViewById(R.id.tf_addFriends_Name);

        AddFriendsActivity refToThis = this;
        btn_search.setOnClickListener(v -> {
            String relName = tf_searchField.getText().toString();

            AddFriendsAPICaller addFriendsAPICaller = new AddFriendsAPICaller(refToThis);
            addFriendsAPICaller.createRelationship(GloablStuff.username, relName);
        });
    }

    public void showMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

}