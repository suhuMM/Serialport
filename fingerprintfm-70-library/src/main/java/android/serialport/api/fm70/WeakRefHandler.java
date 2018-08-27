package android.serialport.api.fm70;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * @author suhu
 * @data 2018/5/9 0009.
 * @description WeakRefHandler
 */

public class WeakRefHandler extends Handler{
    private WeakReference<Callback> reference;

    public WeakRefHandler(){
    }

    public WeakRefHandler(Callback callback){
        reference = new WeakReference<>(callback);
    }

    /**
     * 子线程调用传入Looper
     * @param callback
     * @param looper
     */
    public WeakRefHandler(Callback callback, Looper looper){
        super(looper);
        reference = new WeakReference<>(callback);
    }

    public void setReference(Callback callback) {
        reference = new WeakReference<>(callback);
    }

    @Override
    public void handleMessage(Message msg) {
        if (reference!=null&&reference.get()!=null){
            Callback callback =reference.get();
            callback.handleMessage(msg);
        }
    }
}
