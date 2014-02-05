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
        config.AUTHENTICATION_TYPE= BaasBox.Config.AuthType.SESSION_TOKEN;
        config.API_DOMAIN = "192.168.56.1"; // the host address
        //config.APP_CODE = "123PinBox456"; // your appcode
        //config.HTTP_PORT = 80; // your port

        box = BaasBox.initDefault(this,config);


    }

}
