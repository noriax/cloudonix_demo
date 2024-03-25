package com.io.cloudonix.domain.app.network.request;

import com.squareup.moshi.Json;

public class SendAddressRequest {
    @Json(name = "address")
    String address = "";

    public SendAddressRequest(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
