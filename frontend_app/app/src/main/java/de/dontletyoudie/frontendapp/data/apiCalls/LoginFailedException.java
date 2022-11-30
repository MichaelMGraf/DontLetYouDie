package de.dontletyoudie.frontendapp.data.apiCalls;

public class LoginFailedException extends RuntimeException{
    public LoginFailedException(String msg) {
        super(msg);
    }
}