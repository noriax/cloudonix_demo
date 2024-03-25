package com.io.cloudonix.domain.app.network.response;

import com.squareup.moshi.Json;

public class SendAddressResponse {

    @Json(name = "nat")
    Boolean nat;

    public Boolean getNat() {
        return nat;
    }

    @Override
    public String toString() {
        return "SendAddressResponse{" +
                "nat=" + nat +
                '}';
    }
}
