package com.diplomna.diplomna.application;


import android.app.Application;

import com.diplomna.diplomna.DI.components.ApplicationComponent;
import com.diplomna.diplomna.DI.components.DaggerApplicationComponent;
import com.diplomna.diplomna.DI.modules.ApplicationModule;
import com.diplomna.diplomna.DI.modules.RetrofitModule;

public class DIApplication extends Application {
    private ApplicationComponent applicationComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        this.applicationComponent= DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).retrofitModule(new RetrofitModule()).build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

}
