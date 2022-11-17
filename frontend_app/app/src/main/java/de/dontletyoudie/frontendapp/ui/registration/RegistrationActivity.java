package de.dontletyoudie.frontendapp.ui.registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

                //check username
                //TODO check if username is available
                if(username.equals("")) {
                    showMessage("username is not allowed to be blank!");
                    return;
                }

                //check email
                String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
                    // got this regex-String from https://emailregex.com/
                if(email.equals("")) {
                    showMessage("email ist not allowed to be blank!");
                    return;
                }else if(!email.matches(emailRegex)) {
                    showMessage("invalid email!");
                    return;
                }

                //TODO check password

                //TODO check if both passwords are the same

                //TODO check the form
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

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}