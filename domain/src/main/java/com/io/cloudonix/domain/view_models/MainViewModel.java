package com.io.cloudonix.domain.view_models;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.io.cloudonix.domain.app.domain.NativeIPAddressBridge;
import com.io.cloudonix.domain.app.network.request.SendAddressRequest;
import com.io.cloudonix.domain.app.network.response.SendAddressResponse;
import com.io.cloudonix.domain.app.repository.AddressRepository;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private final AddressRepository dataRepository;
    CompositeDisposable compositeDisposable;
    private final ObservableField<String> ipAddress = new ObservableField<>("");
    private final ObservableField<Boolean> loadingVisible = new ObservableField<>(false);
    private final MutableLiveData<SendAddressResponse> sendAddressResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public ObservableField<Boolean> getLoadingVisible() {
        return loadingVisible;
    }

    public ObservableField<String> getIpAddress() {
        return ipAddress;
    }

    @Inject
    public MainViewModel(AddressRepository dataRepository) {
        this.dataRepository = dataRepository;
        compositeDisposable = new CompositeDisposable();
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public MutableLiveData<SendAddressResponse> getSendAddressResponseLiveData() {
        return sendAddressResponseLiveData;
    }


    public void getIPCPPSolution() {
        ipAddress.set(new NativeIPAddressBridge().getIpAddress());
        sendRequest();
    }

    private void sendRequest() {

        Observable<Object> serverRequest = Observable.create(emitter -> {
            emitter.onNext(dataRepository.sendAddress(new SendAddressRequest(getIpAddress().get()))
                    .subscribe(this::parseResponse, error -> {
                        loadingVisible.set(false);
                        errorLiveData.setValue(error.getMessage());
                    }));
            emitter.onComplete();
        }).subscribeOn(Schedulers.io());

        Observable<String> showWaitUI = Observable.just("Please wait...")
                .delay(3, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread());

        compositeDisposable.add(Observable.concat(showWaitUI, serverRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            parseResponse(response);
                            System.out.println("Received response: " + response);
                        },
                        throwable -> {
                            System.err.println("Error: " + throwable.getMessage());
                        }
                ));
    }

    private void parseResponse(Object response) {
        if (response instanceof SendAddressResponse) {
            sendAddressResponseLiveData.setValue((SendAddressResponse) response);
            loadingVisible.set(false);
        } else {
            loadingVisible.set(true);
        }
    }


    public void reset() {
        clearCompositeDisposable();
        loadingVisible.set(false);
        getIPCPPSolution();
    }

    private void clearCompositeDisposable() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }
}