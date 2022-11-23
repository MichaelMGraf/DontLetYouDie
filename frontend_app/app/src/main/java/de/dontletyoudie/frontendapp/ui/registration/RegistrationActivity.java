package de.dontletyoudie.frontendapp.ui.registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import de.dontletyoudie.frontendapp.R;
import de.dontletyoudie.frontendapp.databinding.ActivityLoginBinding;
import de.dontletyoudie.frontendapp.ui.login.LoginViewModel;
import de.dontletyoudie.frontendapp.ui.login.LoginViewModelFactory;
import org.apache.commons.validator.routines.EmailValidator;

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
                EmailValidator validator = EmailValidator.getInstance();
                 if (email.equals("")){
                    showMessage("your email ist not allowed to be blank!");
                    return;
                } else if (!validator.isValid(email)) {
                    showMessage("your email is invalid");
                    return;
                }

                //check password
                if(!passwordValidates(password1)) {
                    return;
                }

                //check if password is the same
                if(!password1.equals(password2)) {
                    showMessage("password must be the same");
                }


                //TODO now log in with this credentials
                //delete this line:
                showMessage("all format requirements are met :)");
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

    //returns true, if the password matches those conditions:
    // - at least 8 characters long
    // - at least 1 letter
    // - at least 1 digit
    // - at least 1 special symbol
    private boolean passwordValidates(String password) {
        int countDigits = 0;
        int countLetters = 0;
        int countSymbols = 0;
        String pass;

        for(int c = 0; c < password.length(); c++) {
            //we do this for each character
            pass = Character.toString(password.charAt(c));

            if( pass.matches(".*[0-9].*") ) {
                countDigits++;
            }
            if( pass.matches(".*[a-z].*") ){
                countLetters++;
            }
            if( pass.matches(".*[A-Z].*") ) {
                countLetters++;
            }
            // using \\ and \so the brackets dont get interpreted as character classes
            // though, there isnt a backslash included yet, let's hope no one notices...
            if(pass.matches(".*[-*.!@#$%^&(){}\\[\\]:;'<>,.\"?/~`_+=|].*")) {
                countSymbols++;
            }
        }

        if (countLetters == 0) {
            showMessage("password must contain at least 1 letter");
            return false;
        }
        if (countDigits == 0) {
            showMessage("password must contain at least 1 digit");
            return false;
        }
        if (countSymbols == 0) {
            showMessage("password must contain at least 1 symbol");
            return false;
        }

        if(countLetters + countDigits + countSymbols < 8){
            showMessage("password must be at least 8 characters");
            return false;
        } else {
            return true;
        }
    }
}