package de.dontletyoudie.backend.security.filter;

public class PathFilterResult {

    private final boolean instantGrant;
    private final boolean accessDenied;
    private final String message;


    public static PathFilterResult getInstantGrant() {
        return new PathFilterResult(true, false, "");
    }

    public static PathFilterResult getAccessDenied(String message) {
        return new PathFilterResult(false, true, message);
    }

    public static PathFilterResult getNotDenied() {
        return new PathFilterResult(false, false, "");
    }

    private PathFilterResult(boolean instantGrant, boolean accessDenied, String message) {
        this.instantGrant = instantGrant;
        this.accessDenied = accessDenied;
        this.message = message;
    }

    public boolean isAccessDenied() {
        return accessDenied;
    }

    public String getMessage() {
        return message;
    }

    public boolean isInstantGrant() {
        return instantGrant;
    }
}
