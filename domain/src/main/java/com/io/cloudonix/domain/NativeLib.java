package com.io.cloudonix.domain;

public class NativeLib {

    // Used to load the 'domain' library on application startup.
    static {
        System.loadLibrary("domain");
    }

    /**
     * A native method that is implemented by the 'domain' native library,
     * which is packaged with this application.
     */
    public native String getIpAddress();
}