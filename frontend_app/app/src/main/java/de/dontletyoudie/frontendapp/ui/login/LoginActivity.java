package de.dontletyoudie.frontendapp.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import de.dontletyoudie.frontendapp.R;
import de.dontletyoudie.frontendapp.data.apiCalls.LoginAPICaller;
import de.dontletyoudie.frontendapp.databinding.ActivityLoginBinding;
import de.dontletyoudie.frontendapp.ui.homepage.MainActivity;
import de.dontletyoudie.frontendapp.ui.registration.RegistrationActivity;


public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private LoginActivity refToThis = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);


        //Deklarieren der Elemente
        final EditText emailEditText = binding.tfLoginEmail;
        final EditText passwordEditText = binding.tfLoginPassword;
        final Button loginButton = binding.btnLoginSignin;
        final ProgressBar loadingProgressBar = binding.loading;
        final Button registerButton = binding.btnLoginRegister;

        //diese Methode überwacht die Eingabe und enabled den "sign in" button, wenn alle
        //Eingaben vom Format her Akzeptiert sind
        //Außerdem macht es die Meldungen mit "passwort zu kurz" hin falls nötig
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    emailEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(emailEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                LoginAPICaller loginAPICaller = new LoginAPICaller(refToThis);
                loginAPICaller.logIn(emailEditText.getText().toString(), passwordEditText.getText().toString());
                showMessage("welcome " + emailEditText.getText().toString());

            }

        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Request.Builder request = new Request.Builder()
                        .url(CallerStatics.APIURL + "api/account/get");


                HashMap<Integer, CallSuccessfulHandler> handler = new HashMap<>();
                handler.put(200, new CallSuccessfulHandler() {
                    @Override
                    public void onSuccessfulCall(Response response) {
                        try {
                            Log.d(TAG, "result: " + response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                CallerStatics callerStatics = new CallerStatics(request, handler);
                callerStatics.executeCall();*/
                //openRegistrationActivity();
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    // navigiert zu der Registartion Activity
    private void openRegistrationActivity() {
        //dafür müssen wir einen Intent erstellen mit Sourceactivity(?) und ZielActivity
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        //und diesen Intent dann anschließend starten
        startActivity(intent);
    }

    public void navigateToMainActivity() {

        //TODO lösche Activity Verlauf (back button nicht mehr auf Anmelde-Fenster)
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}