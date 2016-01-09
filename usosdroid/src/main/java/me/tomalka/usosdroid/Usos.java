package me.tomalka.usosdroid;

import android.app.Activity;

import retrofit2.Retrofit;
import retrofit2.GsonConverterFactory;
import retrofit2.RxJavaCallAdapterFactory;

public class Usos {
    private String provider;
    private Retrofit retrofit;
    private UsosService service;

    public Usos(String provider) {
        this.provider = provider;
        retrofit = new Retrofit.Builder()
                .baseUrl(provider)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        service = retrofit.create(UsosService.class);
    }

    public String getProvider() {
        return provider;
    }

    public UsosService getService() {
        return service;
    }
}
