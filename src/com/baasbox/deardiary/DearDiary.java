package com.baasbox.deardiary;

import android.app.Application;
import android.util.Log;
import com.baasbox.android.*;

import java.util.List;

/**
 * Created by Andrea Tortorella on 24/01/14.
 */
public class DearDiary extends Application {

    //todo 1.1
    private BaasBox box;

    @Override
    public void onCreate() {
        super.onCreate();
        //todo 1.2
        BaasBox.Config config = new BaasBox.Config();
        config.authenticationType= BaasBox.Config.AuthType.SESSION_TOKEN;
        config.apiDomain = "192.168.56.1"; // the host address
        config.httpPort=9000;
        box = BaasBox.initDefault(this,config);
    }

}
