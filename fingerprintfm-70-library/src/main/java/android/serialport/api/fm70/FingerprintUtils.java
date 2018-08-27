package android.serialport.api.fm70;

/**
 * @author suhu
 * @data 2018/7/5 0005.
 * @description 指纹命令工具类
 *
 *
 * 校验和的计算方式 ：
 *  包标识+包长度+包内容所有数据，超过两个字节进行忽略
 *
 */

public class FingerprintUtils {

    /**
     *
     * 最基础的：
     *
     * EF 01 FF FF FF FF
     *
     */
    private static byte[] initial = {(byte)0xEF,(byte)0x01,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};


    /**
     *
     * 验证口令
     * VfyPwd
     *
     * 指令代码：13
     *
     * 命令：
     * EF 01 FF FF FF FF 01 00 07 13 00 00 00 00 00 1B
     *
     * 默认口令是：
     * 00 00 00 00
     *
     * 返回
     * 01 ：接收包有错
     * 13 ：口令不正确
     * 00 ：口令验证成功
     *
     *
     *
     *
     */
    public static byte[] VfyPwd = {(byte)0xEF,(byte)0x01,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0x01,
            (byte)0x00,(byte)0x07,(byte)0x13,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x1B};


    /**
     *
     * 开灯
     * OpenLED
     *
     * 指令代码：50
     *
     *
     * 命令：
     * EF 01 FF FF FF FF 01 00 03 50 00 54
     *
     *
     * 返回
     * 00 ：成功
     * 其他失败
     *
     *
     */

    public static byte[] OpenLED={(byte)0xEF,(byte)0x01,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0x01,
            (byte)0x00,(byte)0x03,(byte)0x50,(byte)0x00,(byte)0x54};


    /**
     *
     * 关灯
     * CloseLED
     *
     * 指令代码：51
     *
     *
     * 命令：
     * EF 01 FF FF FF FF 01 00 03 51 00 55
     *
     *
     * 返回
     * 00 ：成功
     * 其他失败
     *
     *
     */

    public static byte[] CLoseLED={(byte)0xEF,(byte)0x01,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0x01,
            (byte)0x00,(byte)0x03,(byte)0x51,(byte)0x00,(byte)0x55};


    /**
     *
     * 握手
     * GetEcho
     *
     * 指令代码：53
     *
     *
     * 命令：
     * EF 01 FF FF FF FF 01 00 03 53 00 57
     *
     *
     * 返回
     * 55 ：成功
     * 其他失败
     *
     *
     */
    public static byte[] GetEcho={(byte)0xEF,(byte)0x01,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0x01,
            (byte)0x00,(byte)0x03,(byte)0x53,(byte)0x00,(byte)0x57};


    /**
     *
     * 自动登记
     * AutoLogin
     *
     * 指令代码：54
     *
     * 命令：
     * EF 01 FF FF FF FF 01 00 08 54 36 02 （两个字节的储存编号） 01  （两个字节的校验和）
     *
     *
     *
     */
    public static byte[] AutoLogin(int pageID){
        byte[] page = AlgorithmUtils.toBytes(AlgorithmUtils.intToOx(pageID));
        byte[] b1 = {(byte)0x01,(byte)0x00,(byte)0x08,(byte)0x54,(byte)0x36,(byte)0x02};
        byte[] b2 = AlgorithmUtils.mergeByteArray(b1,page);
        //要求校验和所有的数据
        byte[] b3 = AlgorithmUtils.mergeByteArray(b2,new byte[]{(byte)0x01});
        //校验和
        byte[] b4 = AlgorithmUtils.sumCheck(b3,2);
        //要求校验和所有的数据+校验和
        byte[] b5 = AlgorithmUtils.mergeByteArray(b3,b4);
        return AlgorithmUtils.mergeByteArray(initial,b5);
    }


    /**
     *
     * 读取特征到缓存区
     * 将数据库中指定的ID的指纹读入到模板缓冲区CharBuff1
     *
     * 指令代码：07
     *
     * 命令：
     * EF 01 FF FF FF FF 01 00 06 07 01 （pagerID）（sum）
     *
     *
     */

    public static byte[] LoadChar(int pageID){
        byte [] base = {(byte)0x01, (byte)0x00,(byte) 0x06,(byte) 0x07,(byte)0x01};
        return AlgorithmUtils.mergeByteArray(initial,AlgorithmUtils.conversion(base,pageID));
    }

    /**
     *
     * 读取指纹特征数据
     * 将特征缓存区的特征文件上传给调用者
     *
     * 指令代码：08
     *
     * 命令：
     * EF 01 FF FF FF FF 01 00 04 08 01 00 0E
     *
     * 返回成功才开始发送数据
     * 一个请求两个应答
     *
     *
     */

    public static byte[] UpChar = {(byte)0xEF,(byte)0x01,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0x01,
            (byte)0x00,(byte) 0x04,(byte) 0x08,(byte) 0x01,(byte)0x00,(byte)0x0E};


    /**
     *
     * 下发指纹特征到缓冲区
     * 调用者将指纹特征录入到指纹硬件的特征缓存区
     *
     * 指令代码：09
     *
     * 命令：
     * EF 01 FF FF FF FF 01 00 04 09 01 00 0F
     * EF 01 FF FF FF FF 01 00 04 09 02 00 10
     *
     * 应答成功才可以接收数据
     *
     *
     */
    public static byte[] DownChar = {(byte)0xEF,(byte)0x01,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0x01,
            (byte)0x00,(byte) 0x04,(byte) 0x09,(byte)0x01 ,(byte)0x00,(byte)0x0F};


    /**
     *
     * 储存模板
     *
     * 将CharBuffer1与CharBuffer2中的模板存放到pageID号flash数据库位置
     *
     * 指令代码：06
     *
     * 命令：
     * EF 01 FF FF FF FF 01 00 06 06 01 （pageID）（sum）
     *
     *
     *
     */
    public static byte[] StoreChar(int pageID){
        byte[] base ={(byte)0x01, (byte)0x00,(byte) 0x06,(byte) 0x06,(byte) 0x01};
        return AlgorithmUtils.mergeByteArray(initial,AlgorithmUtils.conversion(base,pageID));
    }


    /**
     * 清空flash 数据库中所有指纹模板
     *
     * 指令代码：0d
     *
     * 命令：
     *  EF 01 FF FF FF FF 01 00 03 0d 00 11
     *
     */
    public static byte Empty[] = {(byte)0xEF,(byte)0x01,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0x01,
            (byte)0x00,(byte) 0x03,(byte) 0x0d,(byte)0x00,(byte)0x11};


    /**
     *
     * 清空指定ID指纹数据
     *
     * 指令代码：0c
     *
     * 命令：
     *
     * EF 01 FF FF FF FF 01 00 07 0c （指纹页面）（删除个数）（校验和）
     *
     *
     */
    public static byte[] DeleteChar(int pageID){
        byte[] base = {(byte)0x01,(byte) 0x00,(byte) 0x07,(byte)0x0c};
        byte[] number = {(byte)0x00,(byte)0x01};
        byte[] page = AlgorithmUtils.toBytes(AlgorithmUtils.intToOx(pageID));
        byte[] b1 = AlgorithmUtils.mergeByteArray(base,page);
        byte[] b2 = AlgorithmUtils.mergeByteArray(b1,number);
        byte[] b3 = AlgorithmUtils.sumCheck(b2,2);
        byte[] b4 = AlgorithmUtils.mergeByteArray(b2,b3);
        return AlgorithmUtils.mergeByteArray(initial,b4);
    }


    /**
     *
     * 自动搜索
     * 指令代码：55
     *
     * 指令：
     *
     * EF 01 FF FF FF FF 01 00 08 55 36 00 00 03 E8 01 7F
     *
     * 搜索个数1000个
     *
     */
    public static byte[] AutoSearch = {(byte)0xEF,(byte)0x01,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0x01,
            (byte)0x00,(byte)0x08,(byte)0x55,(byte)0x36,(byte)0x00,(byte)0x00,(byte)0x03,(byte)0xE8,(byte)0x01,(byte)0x7F
    };







    /**
     *
     * 储存模板
     *
     * 将CharBuffer1与CharBuffer2中的模板存放到pageID号flash数据库位置
     *
     * 指令代码：06
     *
     * 命令：
     * EF 01 FF FF FF FF 01 00 06 06 01 （pageID）（sum）
     *
     *
     *
     */
    public static byte[] GR_StoreChar(int pageID){
        byte[] base ={(byte)0x01, (byte)0x00,(byte) 0x06,(byte) 0x06,(byte) 0x01};
        return AlgorithmUtils.mergeByteArray(initial,AlgorithmUtils.conversion(base,pageID));
    }

    public static byte[] getFeatureData(byte[] data,int page){
        if (data==null) return null;
        byte[] b1 = GR_StoreChar(page);
        return AlgorithmUtils.mergeByteArray(data,b1);
    }


    /**
     *
     * 从数据库直接获取返回数据
     * 数据包：
     * 将指纹特征发给指纹模块
     *
     * 包标识：02
     *
     * 命令：
     * EF 01 FF FF FF FF 02 （包长）（数据）（校验和）
     *
     *
     */
    public static byte[] Send_Data(byte [] data){
        return data;
    }



    /**
     *
     * 设置验证级别
     * 1-5
     *
     * 指令代码：0e
     *
     * 命令：
     * EF 01 FF FF FF FF 01 00 05 0e 05 （内容） （校验和）
     *
     * 1=<level<=5
     *
     */

    public static byte[] GR_threshold (int level){
        byte [] base = {(byte)0x01, (byte)0x00,(byte) 0x05,(byte) 0x0e,(byte)0x05};
        return AlgorithmUtils.mergeByteArray(initial,AlgorithmUtils.conversion1(base,level));
    }





}
