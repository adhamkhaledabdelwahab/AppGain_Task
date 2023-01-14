package kh.ad.appgaintask.view_model;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashSet;
import java.util.Set;

public class NetworkViewModel extends ViewModel {
    private final MutableLiveData<Boolean> mConnected = new MutableLiveData<>();

    public void registerNetworkStateObserver(Application app) {
        ConnectivityManager manager = (ConnectivityManager)app.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            mConnected.setValue(true);
            return;
        }

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build();

        manager.registerNetworkCallback(networkRequest, new ConnectivityManager.NetworkCallback() {
            final Set<Network> availableNetworks = new HashSet<>();

            public void onAvailable(@NonNull Network network) {
                availableNetworks.add(network);
                mConnected.postValue(true);
            }

            public void onLost(@NonNull Network network) {
                availableNetworks.remove(network);
                mConnected.postValue(!availableNetworks.isEmpty());
            }

            public void onUnavailable() {
                availableNetworks.clear();
                mConnected.postValue(false);
            }
        });
    }

    @NonNull
    public MutableLiveData<Boolean> getConnected() {
        return mConnected;
    }
}
