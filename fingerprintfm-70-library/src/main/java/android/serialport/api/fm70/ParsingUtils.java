package android.serialport.api.fm70;

/**
 * @author suhu
 * @data 2018/7/7 0007.
 * @description
 */

public class ParsingUtils {

    /**
     * 根据标志位进行裁剪
     *
     * @param response
     * @param index
     * @return
     */
    public static String parsing(byte[] response ,int index){
        if (response==null) return "error";
        return AlgorithmUtils.byteToString(response[index]);
    }







}
