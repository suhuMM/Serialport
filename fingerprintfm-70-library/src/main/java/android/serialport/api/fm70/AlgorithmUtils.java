package android.serialport.api.fm70;

/**
 * @author suhu
 * @data 2018/7/7 0007.
 * @description 转换算法
 */

public class AlgorithmUtils {

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str
     * @return
     */
    public static byte[] toBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }




    /**
     * 校验和
     *
     * @param msg 需要计算校验和的byte数组
     * @param length 校验和位数
     * @return 计算出的校验和数组
     */
    public static byte[] sumCheck(byte[] msg, int length) {
        long mSum = 0;
        byte[] mByte = new byte[length];

        /** 逐Byte添加位数和 */
        for (byte byteMsg : msg) {
            long mNum = ((long)byteMsg >= 0) ? (long)byteMsg : ((long)byteMsg + 256);
            mSum += mNum;
        } /** end of for (byte byteMsg : msg) */

        /** 位数和转化为Byte数组 */
        for (int liv_Count = 0; liv_Count < length; liv_Count++) {
            mByte[length - liv_Count - 1] = (byte)(mSum >> (liv_Count * 8) & 0xff);
        } /** end of for (int liv_Count = 0; liv_Count < length; liv_Count++) */

        return mByte;
    }


    /**
     *
     * 合并byte数组
     *
     * @param byte1
     * @param byte2
     * @return 合并后的数组
     */
    public static byte[] mergeByteArray(byte[] byte1,byte[] byte2){
        byte[] unitByte = new byte[byte1.length + byte2.length];
        System.arraycopy(byte1, 0, unitByte, 0, byte1.length);
        System.arraycopy(byte2, 0, unitByte, byte1.length, byte2.length);
        return unitByte;
    }


    /**
     *
     * 转换成新的byte[]
     *
     * 将pagerID转换成16进制
     * 集合合并
     * 获取校验和byte[]
     * 合并成新的byte[]
     *
     * @param base
     * @param pageID
     * @return byte[]
     *
     *
     *
     * byte[] b1 =null;
    switch (pageID){
    case 1:
    b1 = new byte[]{0x01};
    break;
    case 2:
    b1 = new byte[]{0x02};
    break;
    case 3:
    b1 = new byte[]{0x03};
    break;
    case 4:
    b1 = new byte[]{0x04};
    break;
    case 5:
    b1 = new byte[]{0x05};
    break;
    }
     *
     *
     *
     *
     *
     *
     */
    public static byte[] conversion(byte[] base ,int pageID){
        String str = intToOx(pageID);
        byte[] b1 = AlgorithmUtils.toBytes(str);
        byte[] b2 = AlgorithmUtils.mergeByteArray(base,b1);
        byte[] b3 = AlgorithmUtils.sumCheck(b2,2);
        byte[] b4 = AlgorithmUtils.mergeByteArray(b2,b3);
        return b4;
    }

    /**
     *
     * 设置密码级别
     *
     * @param base
     * @param level
     * @return
     */
    public static byte[] conversion1(byte[] base ,int level){
        byte[] b1 =null;
        switch (level){
            case 1:
                b1 = new byte[]{0x01};
                break;
            case 2:
                b1 = new byte[]{0x02};
                break;
            case 3:
                b1 = new byte[]{0x03};
                break;
            case 4:
                b1 = new byte[]{0x04};
                break;
            case 5:
                b1 = new byte[]{0x05};
                break;
        }
        byte[] b2 = AlgorithmUtils.mergeByteArray(base,b1);
        byte[] b3 = AlgorithmUtils.sumCheck(b2,2);
        byte[] b4 = AlgorithmUtils.mergeByteArray(b2,b3);
        return b4;
    }


    /**
     * 将10进制转换成16进制
     *
     * @param pageID
     * @return
     */
    public static String intToOx(int pageID){
        if (pageID>255){
            String str = Integer.toHexString(pageID);
            if (str.length()==3){
                return "0"+str;
            }
            return str;
        }else {
            String str = Integer.toHexString(pageID);
            if (str.length()==1){
                return "000"+str;
            }else {
                return "00"+str;
            }

        }
    }

    /**
     *
     * 转换成16进制字符串
     *
     * @param buffer
     * @return
     */
    public static String byte2hex(byte [] buffer){
        String h = "";

        for(int i = 0; i < buffer.length; i++){
            String temp = Integer.toHexString(buffer[i] & 0xFF);
            if(temp.length() == 1){
                temp = "0" + temp;
            }
            h = h + " "+ temp;
        }
        return h;
    }

    /**
     *
     * 单个转换
     *
     * @param b
     * @return
     */
    public static String byteToString(byte b){

        String temp = Integer.toHexString(b & 0xFF);
        if(temp.length() == 1){
            temp = "0" + temp;
        }
        return temp;
    }


    /**
     *
     * 将16进制转换成10进制
     * @param str
     * @return
     */
    public static int getInt(String str){
        return Integer.parseInt(str, 16);
    }



}
