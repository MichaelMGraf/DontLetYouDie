package de.dontletyoudie.frontendapp.ui.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import de.dontletyoudie.frontendapp.R;

public class RegistrationActivity extends AppCompatActivity {

    Button logInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        logInButton = (Button) findViewById(R.id.btn_login_register);
    }
}