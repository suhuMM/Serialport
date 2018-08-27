package android.serialport.api.fm70;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 *@author suhu
 *@time 2018/7/6 0006 下午 9:15
 *
*/
public class SerialPortUtil {

    private static final String TAG = "SerialPortUtil";
    private static final int RESPONSE = 0;
    private SerialPort serialPort = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private Thread receiveThread = null;
    private FingerprintResponseFM70 response;

    private boolean flag = false;
    private static SerialPortUtil serialPortUtil = new SerialPortUtil();


    private WeakRefHandler mHandler = new WeakRefHandler();
    private Handler.Callback callback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what){
                case RESPONSE:
                    if (response!=null){
                        response.responseFM70((byte[]) msg.obj);
                    }
                    break;
            }


            return false;
        }
    };

    private SerialPortUtil(){

    }


    public static SerialPortUtil getInstance(){
        return serialPortUtil;
    }

    /**
     * 初始化数据
     */
    public void init(){
        openSerialPort();
        mHandler.setReference(callback);
    }

    /**
     * 打开串口的方法
     */
    private void openSerialPort(){
        Log.i(TAG,"打开串口");
        try {
            serialPort = new SerialPort(new File("/dev/"+ Config.PORT),Config.BAUD_RATE,0);
            //获取打开的串口中的输入输出流，以便于串口数据的收发
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            flag = true;
            receiveSerialPort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *关闭串口的方法
     * 关闭串口中的输入输出流
     * 然后将flag的值设为flag，终止接收数据线程
     */
    public void closeSerialPort(){
        Log.i(TAG,"关闭串口");
        try {
            if(inputStream != null) {
                inputStream.close();
            }
            if(outputStream != null){
                outputStream.close();
            }
            flag = false;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送串口数据的方法
     * @param data 要发送的数据
     */
    public void sendSerialPort(byte[]  data){
        Log.i(TAG,"发送串口数据");
        String str = AlgorithmUtils.byte2hex(data);
        Log.i(TAG,"============输入参数:"+str);
        try {
            if (outputStream==null) return;
            outputStream.write(data);
            outputStream.flush();
            Log.i(TAG,"串口数据发送成功");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG,"串口数据发送异常");
        }
    }

    /**
     * 接收串口数据的方法
     */
    public  void receiveSerialPort(){
        Log.i(TAG,"接收串口数据");
        if(receiveThread != null)
            return;
        /*创建子线程接收串口数据
         */
        receiveThread = new Thread(){
            @Override
            public void run() {
                byte[] readData = new byte[64];
                while (flag) {
                    try {

                        if (inputStream == null) {
                            return;
                        }
                        int size = inputStream.read(readData);
                        Log.i(TAG, "接收到串口数据:数据大小为："+size);
                        if (size>0 && flag) {
                            byte [] bytes = Arrays.copyOf(readData,size);

                            String str = AlgorithmUtils.byte2hex(bytes);
                            Log.i(TAG,"接收收据"+str);

                            Message message = Message.obtain();
                            message.obj = bytes;
                            message.what = RESPONSE;
                            mHandler.sendMessage(message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        //启动接收线程
        receiveThread.start();
    }




    /**
     *
     *
     * 指纹数据接口
     *
     * @param response
     */
    public void setFingerprintResponse(FingerprintResponseFM70 response) {
        this.response = response;
    }























}
