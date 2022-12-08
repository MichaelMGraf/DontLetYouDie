package de.dontletyoudie.frontendapp.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResultActivity {
    @Nullable
    private LoggedInUserViewActivity success;
    @Nullable
    private Integer error;

    LoginResultActivity(@Nullable Integer error) {
        this.error = error;
    }

    LoginResultActivity(@Nullable LoggedInUserViewActivity success) {
        this.success = success;
    }

    @Nullable
    LoggedInUserViewActivity getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}