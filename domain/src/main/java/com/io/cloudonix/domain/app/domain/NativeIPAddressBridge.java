package com.io.cloudonix.domain.app.domain;

public class NativeIPAddressBridge {

    // Used to load the 'data' library on application startup.
    static {
        System.loadLibrary("domain");
    }

    /**
     * A native method that is implemented by the 'data' native library,
     * which is packaged with this application.
     */
    public native String getIpAddress();

}