package a.com.serialport;

import android.serialport.api.fm70.FingerprintResponseFM70;
import android.serialport.api.fm70.FingerprintUtils;
import android.serialport.api.fm70.SerialPortUtil;

/**
 * @author suhu
 * @data 2018/8/3 0003.
 * @description
 */

public class FingerprintFM70Manager {

    /**
     *
     * 初始化指纹数据
     *
     */
    public static void init(){
        SerialPortUtil.getInstance().init();
        openLED();
        SerialPortUtil.getInstance().sendSerialPort(FingerprintUtils.VfyPwd);
        SerialPortUtil.getInstance().sendSerialPort(FingerprintUtils.GetEcho);
    }

    /**
     *
     * 设置监听
     *
     * @param responseFM70
     */
    public static void setListener(FingerprintResponseFM70 responseFM70){
        SerialPortUtil.getInstance().setFingerprintResponse(responseFM70);

    }

    /**
     *
     * 开灯
     *
     */
    public static void openLED(){
        SerialPortUtil.getInstance().sendSerialPort(FingerprintUtils.OpenLED);
    }


    /**
     *
     * 关灯
     *
     */
    public static void closeLED(){
        SerialPortUtil.getInstance().sendSerialPort(FingerprintUtils.CLoseLED);
    }

    /**
     * 验证指纹
     *
     */
    public static void validation(){
        SerialPortUtil.getInstance().sendSerialPort(FingerprintUtils.AutoSearch);
    }

    /**
     *
     * 指纹自动注册
     *
     */
    public static void localRegistered(int pageID){
        SerialPortUtil.getInstance().sendSerialPort(FingerprintUtils.AutoLogin(pageID));
    }


    /**
     *
     * 根据指纹ID读取特征到缓冲区
     *
     * 第一步：readFeatureToBuffer
     * 第二步：readFeatureData
     *
     *
     * @param pageID
     */
    public static void readFeatureToBuffer(int pageID){
        SerialPortUtil.getInstance().sendSerialPort(FingerprintUtils.LoadChar(pageID));
    }


    /**
     *
     * 将缓冲区特征读到程序
     * 不能单独定义，需要在readFeatureToBuffer返回成功才能读取
     *
     */
    public static void readFeatureData(){
        SerialPortUtil.getInstance().sendSerialPort(FingerprintUtils.UpChar);
    }


    /**
     *
     * 下发指纹特征到缓冲区
     * 返回成功，跟随数据包
     *
     *
     */
    public static void registeredData(){
        SerialPortUtil.getInstance().sendSerialPort(FingerprintUtils.DownChar);
    }

    /**
     *
     * 将缓存区数据存放到指定到pageID
     *
     * @param pageID
     */
    public static void StoreChar(int pageID){
        SerialPortUtil.getInstance().sendSerialPort(FingerprintUtils.StoreChar(pageID));
    }




    /**
     *
     * 清空所有数据
     *
     */
    public static void clearAll(){
        SerialPortUtil.getInstance().sendSerialPort(FingerprintUtils.Empty);
    }


    /**
     *
     * 根据pageID删除数据
     *
     * @param pageID
     */
    public static void clearPageID(int pageID){
        SerialPortUtil.getInstance().sendSerialPort(FingerprintUtils.DeleteChar(pageID));
    }





    /**
     *
     * 关闭串口
     *
     */
    public static void closeSerialPort(){
        SerialPortUtil.getInstance().closeSerialPort();
    }


    /**
     *
     * 发送数据到指定的位置
     *
     * @param data
     * @param page
     */
    public static void sendDataToDevices(byte[] data,int page){
        if (data==null&& data.length!=556&& page<0&& page>999) return;
        byte[] featureData = getFeatureData (data,page);
        int l = featureData.length;
        int j = 0;
        while (j<l){
            byte[] bytes;
            if (l-j>16){
                bytes = new byte[16];
            }else {
                bytes = new byte[l-j];
            }
            for (int i = 0; i < bytes.length; i++) {
                if (j<l){
                    bytes[i] = featureData[j];
                    j++;
                }
            }
           sendData(bytes);
        }

    }

    private static byte[] getFeatureData(byte[] data,int page){
        return FingerprintUtils.getFeatureData(data,page);
    }

    /**
     *
     * 外部指纹特征注册到本地指纹库
     * @param data 指纹特征
     *
     */
    private static void sendData(byte [] data){
        SerialPortUtil.getInstance().sendSerialPort(FingerprintUtils.Send_Data(data));

    }

    /**
     * 设置阈值
     *
     * @param level
     */
    public static void setThreshold(int level){
        SerialPortUtil.getInstance().sendSerialPort(FingerprintUtils.GR_threshold(level));
    }

}
