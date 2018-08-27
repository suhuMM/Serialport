package a.com.serialport;

import android.app.Application;

/**
 * @author: tiger
 * @email: suhu0824@gmail.com
 * @data: 2018/8/27 18:38
 * @description:
 */

public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化串口
        FingerprintFM70Manager.init();
    }
}
