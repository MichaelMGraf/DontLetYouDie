package de.dontletyoudie.frontendapp.data;

public class GlobalProperties {
    private static GlobalProperties mInstance= null;

    public String userName;

    protected GlobalProperties(){}

    public static synchronized GlobalProperties getInstance() {
        if(null == mInstance){
            mInstance = new GlobalProperties();
        }
        return mInstance;
    }
}
