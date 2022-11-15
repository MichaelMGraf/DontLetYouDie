package de.dontletyoudie.frontendapp.ui.registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.dontletyoudie.frontendapp.R;
import de.dontletyoudie.frontendapp.databinding.ActivityLoginBinding;
import de.dontletyoudie.frontendapp.ui.login.LoginViewModel;
import de.dontletyoudie.frontendapp.ui.login.LoginViewModelFactory;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);


        final Button logInButton = (Button) findViewById(R.id.btn_register_login);
        final TextView usernameEditText = findViewById(R.id.tf_registration_username);
        final TextView emailEditText = findViewById(R.id.tf_registration_email);
        final TextView passwordEditText = findViewById(R.id.tf_registration_password);
        final TextView password2EditText = findViewById(R.id.tf_registration_password2);
        final Button registerButton = findViewById(R.id.btn_register_register);

        //to be deleted:
        registerButton.setEnabled(true);


        //actionlistener for clicking on the "register"-button
        //this button should only be clickable if the textFields matches the requirements,
        //but this is realized somewhere else
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password1 = passwordEditText.getText().toString();
                String password2 = password2EditText.getText().toString();
                //TODO now log in with this credentials
            }
        });



        //actionlistener for clicking on the "You already have an account? Log in"-Button
        //this navigates to the previous activity, which will in this case always be the login page
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}