package de.dontletyoudie.frontendapp.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserViewActivity {
    private String displayName;
    //... other data fields that may be accessible to the UI

    LoggedInUserViewActivity(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}