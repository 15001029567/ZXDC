package net.edaibu.easywalking.utils.bletooth;

import android.text.TextUtils;

/**
 * 发送的蓝牙指令
 */
public class BleAgreement {

    //验证消息
    public static byte[] checkLock(String imei) {
        byte[] im = imeiToByte(imei,16);
        byte[] result = new byte[im.length + 4];
        result[0] = (byte) 0xAB;
        result[1] = (byte) 0xAB;
        result[2] = (byte) 0x80;
        result[3] = (byte) im.length;

        for (int i = 0; i < im.length; i++) {
            result[i + 4] = im[i];
        }
        return result;
    }


    //开锁命令
    public static byte[] openLock(String orderCode) {
        byte[] im = imeiToByte(orderCode,16);
        byte[] result = new byte[im.length + 4];
        result[0] = (byte) 0xAB;
        result[1] = (byte) 0xAB;
        result[2] = (byte) 0x81;
        result[3] = (byte) 0x01;
        result[4] = (byte) im.length;

        for (int i = 0; i < im.length; i++) {
            result[i + 4] = im[i];
        }
        return result;
    }


    //临时关锁命令
    public static byte[] closeLock(String orderCode) {
        byte[] im = imeiToByte(orderCode,16);
        byte[] result = new byte[im.length + 4];
        result[0] = (byte) 0xAB;
        result[1] = (byte) 0xAB;
        result[2] = (byte) 0x81;
        result[3] = (byte) 0x03;
        result[4] = (byte) im.length;

        for (int i = 0; i < im.length; i++) {
            result[i + 4] = im[i];
        }
        return result;
    }


    //结算还车
    public static byte[] balanceCar(String orderCode) {
        byte[] im = imeiToByte(orderCode,16);
        byte[] result = new byte[im.length + 4];
        result[0] = (byte) 0xAB;
        result[1] = (byte) 0xAB;
        result[2] = (byte) 0x81;
        result[3] = (byte) 0x02;
        result[4] = (byte) im.length;

        for (int i = 0; i < im.length; i++) {
            result[i + 4] = im[i];
        }
        return result;
    }

    //强制锁车
    public static byte[] forcedCloseLock(String orderCode) {
        byte[] im = imeiToByte(orderCode,16);
        byte[] result = new byte[im.length + 4];
        result[0] = (byte) 0xAB;
        result[1] = (byte) 0xAB;
        result[2] = (byte) 0x81;
        result[3] = (byte) 0x04;
        result[4] = (byte) im.length;

        for (int i = 0; i < im.length; i++) {
            result[i + 4] = im[i];
        }
        return result;
    }


    //寻车响铃命令
    public static byte[] findLock(int code) {
        byte[] result = new byte[6];
        result[0] = (byte) 0xAB;
        result[1] = (byte) 0xAB;
        result[2] = (byte) 0x81;
        result[3] = (byte) 0x05;
        result[4] = (byte) 0x01;
        result[5] = (byte) code;
        return result;
    }


    /**
     * 播放广告音频
     * @param imei
     * @return
     */
    public static byte[] playAbvertMedia(String imei,String audio){
        byte[] im = imeiToByte(imei,16);
        byte[] result = new byte[im.length + 7];
        result[0] = (byte) 0xAB;
        result[1] = (byte) 0xAB;
        result[2] = (byte) 0x9A;
        result[3] = (byte) (im.length + 3);

        for (int i = 0; i < im.length; i++) {
            result[i + 4] = im[i];
        }
        byte[] audioBy=hex2Byte(Integer.toHexString(Integer.parseInt(audio)).toLowerCase());
        result[12] = audioBy[0];
        result[13] = (byte) 0x00;
        result[14] = (byte) 0x00;
        return result;
    }


    /**
     * 旋轮命令
     * @param beforeCode：前轮code
     * @param afterCode：后轮code
     * @param dration：展示时长
     * @return
     */
    public static String beforeCode,afterCode,dration;
    public static byte[] sendDIY(){
        byte[] result = new byte[14];
        result[0] = (byte) 0xAB;
        result[1] = (byte) 0xAB;
        result[2] = (byte) 0x98;
        result[3] = (byte) 0x0A;
        byte[] leftBy=codeToByte(beforeCode);
        for(int i=0,len=leftBy.length;i<len;i++){
            result[i+4]=leftBy[i];
        }

        byte[] rightBy=codeToByte(afterCode);
        for(int i=0,len=rightBy.length;i<len;i++){
            result[i+8]=rightBy[i];
        }

        byte[] drationBy=drationToByte(dration);
        for(int i=0,len=drationBy.length;i<len;i++){
            result[i+12]=drationBy[i];
        }
        return result;
    }


    /**
     * 将前后轮code转换为16进制的byte数组
     * @param code
     * @return
     */
    public static byte[] codeToByte(String code){
        if(!TextUtils.isEmpty(code)){
            String codeHx = Long.toHexString(Long.valueOf(code));
            int len=codeHx.length();
            if(len<8){
                for(int i=0;i<8-len;i++){
                    codeHx="0"+codeHx;
                }
                return hex2Byte(codeHx.toLowerCase());
            }
            return hex2Byte(codeHx.toLowerCase());
        }else{
            byte[] b=new byte[]{0x00,0x00,0x00,0x00};
            return b;
        }
    }


    /**
     * 将展示时长转换为16进制的byte数组
     * @param dration
     * @return
     */
    public static byte[] drationToByte(String dration){
        if(!TextUtils.isEmpty(dration) && !dration.equals("0")){
            String codeHx = Long.toHexString(Long.valueOf(dration));
            int len=codeHx.length();
            if(len<4){
                for(int i=0;i<4-len;i++){
                    codeHx="0"+codeHx;
                }
                return hex2Byte(codeHx.toLowerCase());
            }
            return hex2Byte(codeHx.toLowerCase());
        }else{
            byte[] b=new byte[]{0x00,0x00};
            return b;
        }
    }

    /**
     * 将字符串转换为16进制的byte数组
     * @param imei
     * @param num
     * @return
     */
    public static byte[] imeiToByte(String imei,int num) {
        if (TextUtils.isEmpty(imei)) {
            return null;
        }
        String imeiHx = Long.toHexString(Long.valueOf(imei));
        int len = imeiHx.length();
        if (len <= num) {
            for (int i = 0; i < num - len; i++) {
                imeiHx = "0" + imeiHx;
            }
            return hex2Byte(imeiHx.toLowerCase());
        }
        return null;
    }

    //  十六进制的字符串转换成byte数组
    public static byte[] hex2Byte(String hex) {
        if (hex == null) {
            return new byte[]{};
        }
        String digital = "0123456789abcdef";
        char[] hex2char = hex.toCharArray();

        byte[] bytes = new byte[hex.length() / 2];
        int temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = digital.indexOf(hex2char[2 * i]) * 16;
            temp += digital.indexOf(hex2char[2 * i + 1]);
            bytes[bytes.length - i - 1] = (byte) (temp & 0xff);
        }
        return bytes;
    }
}
