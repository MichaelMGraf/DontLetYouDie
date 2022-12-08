package de.dontletyoudie.frontendapp.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import de.dontletyoudie.frontendapp.data.LoginRepository;
import de.dontletyoudie.frontendapp.data.Result;
import de.dontletyoudie.frontendapp.data.model.LoggedInUser;
import de.dontletyoudie.frontendapp.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormStateActivity> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResultActivity> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormStateActivity> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResultActivity> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResultActivity(new LoggedInUserViewActivity(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResultActivity(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormStateActivity(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormStateActivity(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormStateActivity(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}