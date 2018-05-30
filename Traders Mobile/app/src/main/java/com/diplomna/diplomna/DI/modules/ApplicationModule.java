package com.diplomna.diplomna.DI.modules;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {
        RetrofitModule.class
})
public class ApplicationModule {

    public ApplicationModule(Application app) {
        this.app = app;
    }

    private Application app;
    private String jwtTokenPrefName = "token";
    private String preferenceAppName="Traders";

    @Singleton
    @Provides
    public SharedPreferences getAppPreferences() {
        return app.getSharedPreferences(preferenceAppName, Context.MODE_PRIVATE);
    }

    @Singleton
    @Provides
    public Context contextInject(){
       return app;
    }
}
