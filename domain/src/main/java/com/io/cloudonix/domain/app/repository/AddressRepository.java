package com.io.cloudonix.domain.app.repository;

import com.io.cloudonix.domain.app.network.Api;
import com.io.cloudonix.domain.app.network.request.SendAddressRequest;
import com.io.cloudonix.domain.app.network.response.SendAddressResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddressRepository {

    private final Api api;

    @Inject
    public AddressRepository(Api api) {
        this.api = api;
    }

    public Observable<SendAddressResponse> sendAddress(SendAddressRequest sendAddressRequest) {
        return api.sendAddress(sendAddressRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
