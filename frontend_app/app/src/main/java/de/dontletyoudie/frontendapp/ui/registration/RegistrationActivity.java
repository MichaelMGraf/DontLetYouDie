package de.dontletyoudie.frontendapp.ui.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import de.dontletyoudie.frontendapp.R;
import de.dontletyoudie.frontendapp.data.apiCalls.CreateAccountAPICaller;
import de.dontletyoudie.frontendapp.data.apiCalls.CreateAccountFailedException;
import de.dontletyoudie.frontendapp.databinding.ActivityLoginBinding;
import de.dontletyoudie.frontendapp.ui.homepage.MainActivity;

import org.apache.commons.validator.routines.EmailValidator;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

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
                if(!passwordValidates(password1).equals("Password is valid")) {
                    return;
                }

                //check if password is the same
                if(!password1.equals(password2)) {
                    showMessage("password must be the same");
                }

                //TODO delete this line:
                showMessage("hello " + username);


                CreateAccountAPICaller createAccountAPICaller = new CreateAccountAPICaller();
                try {
                    createAccountAPICaller.createAccount(username, email, password1);
                } catch (CreateAccountFailedException e) {
                    showMessage(e.getMessage());
                    //TODO give user input what went wrong
                    e.printStackTrace();
                    return;
                }


                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                //und diesen Intent dann anschließend starten
                startActivity(intent);
            }
        });

        //
        //actionlistener for clicking on the "You already have an account? Log in"-Button
        //this navigates to the previous activity, which will in this case always be the login page
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void showMessage(String message) {
        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    //returns true, if the password matches those conditions:
    // - at least 8 characters long
    // - at least 1 letter
    // - at least 1 digit
    // - at least 1 special symbol
    private String passwordValidates(String password) {

        PasswordValidator validator = new PasswordValidator(
                // length between 8 and 16 characters
                new LengthRule(8, 50),

                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),

                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),

                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1),

                // at least one symbol (special character)
                new CharacterRule(EnglishCharacterData.Special, 1),

                // define some illegal sequences that will fail when >= 5 chars long
                // alphabetical is of the form 'abcde', numerical is '34567', qwery is 'asdfg'
                // the false parameter indicates that wrapped sequences are allowed; e.g. 'xyzabc'
                new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
                new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false),
                new IllegalSequenceRule(EnglishSequenceData.USQwerty, 5, false),

                // no whitespace
                new WhitespaceRule());

        RuleResult result = validator.validate(new PasswordData(new String(password)));
        if (result.isValid()) {
            return "Password is valid";
        } else {
            for (String msg : validator.getMessages(result)) {
                showMessage(msg);
                return "Invalid password";
            }
            return "Invalid password";
        }
    }

}