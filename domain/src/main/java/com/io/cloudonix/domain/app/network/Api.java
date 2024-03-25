package com.io.cloudonix.domain.app.network;

import com.io.cloudonix.domain.app.network.request.SendAddressRequest;
import com.io.cloudonix.domain.app.network.response.SendAddressResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {
    @POST("/")
    Observable<SendAddressResponse> sendAddress(@Body SendAddressRequest sendAddressRequest);
}
