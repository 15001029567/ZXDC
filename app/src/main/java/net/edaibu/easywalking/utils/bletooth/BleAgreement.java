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


    //租用开锁命令
    public static byte[] rentLock(String imei) {
        byte[] im = imeiToByte(imei,16);
        byte[] result = new byte[im.length + 4];
        result[0] = (byte) 0xAB;
        result[1] = (byte) 0xAB;
        result[2] = (byte) 0x8C;
        result[3] = (byte) im.length;

        for (int i = 0; i < im.length; i++) {
            result[i + 4] = im[i];
        }
        return result;
    }

    //开锁命令
    public static byte[] openLock(String imei) {
        byte[] im = imeiToByte(imei,16);
        byte[] result = new byte[im.length + 4];
        result[0] = (byte) 0xAB;
        result[1] = (byte) 0xAB;
        result[2] = (byte) 0x81;
        result[3] = (byte) im.length;

        for (int i = 0; i < im.length; i++) {
            result[i + 4] = im[i];
        }
        return result;
    }


    //临时关锁命令
    public static byte[] closeLock(String imei) {
        byte[] im = imeiToByte(imei,16);
        byte[] result = new byte[im.length + 4];
        result[0] = (byte) 0xAB;
        result[1] = (byte) 0xAB;
        result[2] = (byte) 0x82;
        result[3] = (byte) im.length;

        for (int i = 0; i < im.length; i++) {
            result[i + 4] = im[i];
        }
        return result;
    }


    //结算还车
    public static byte[] balanceCar(String imei) {
        byte[] im = imeiToByte(imei,16);
        byte[] result = new byte[im.length + 4];
        result[0] = (byte) 0xAB;
        result[1] = (byte) 0xAB;
        result[2] = (byte) 0x8B;
        result[3] = (byte) im.length;

        for (int i = 0; i < im.length; i++) {
            result[i + 4] = im[i];
        }
        return result;
    }

    //强制结算还车
    public static byte[] forcedBalance(String imei) {
        byte[] im = imeiToByte(imei,16);
        byte[] result = new byte[im.length + 4];
        result[0] = (byte) 0xAB;
        result[1] = (byte) 0xAB;
        result[2] = (byte) 0x8F;
        result[3] = (byte) im.length;

        for (int i = 0; i < im.length; i++) {
            result[i + 4] = im[i];
        }
        return result;
    }


    //寻车响铃命令
    public static byte[] findLock(String imei, int type) {
        byte[] im = imeiToByte(imei,16);
        byte[] result = new byte[im.length + 5];
        result[0] = (byte) 0xAB;
        result[1] = (byte) 0xAB;
        result[2] = (byte) 0x83;
        result[3] = (byte) (im.length + 1);

        for (int i = 0; i < im.length; i++) {
            result[i + 4] = im[i];
        }
        result[result.length - 1] = (byte) type;
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



    //告诉锁U数据上报失败了
    public static byte[] uError() {
        byte[] result = new byte[5];
        result[0] = (byte) 0xAB;
        result[1] = (byte) 0xAB;
        result[2] = (byte) 0x87;
        result[3] = (byte) 0x01;
        result[4] = (byte) 0x46;
        return result;
    }


    //告诉锁U数据上报成功了
    public static byte[] uSuccess() {
        byte[] result = new byte[5];
        result[0] = (byte) 0xAB;
        result[1] = (byte) 0xAB;
        result[2] = (byte) 0x87;
        result[3] = (byte) 0x01;
        result[4] = (byte) 0x53;
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



    //发送开始扫描信标
    public static byte[] sendStartScanMark() {
        byte[] result = new byte[5];
        result[0] = (byte) 0xAB;
        result[1] = (byte) 0xAB;
        result[2] = (byte) 0x92;
        result[3] = (byte) 0x07;
        result[4] = (byte) 0x01;
        return result;
    }


    //发送扫描信标成功命令
    public static byte[] sendScanMarkSuccess(String macAddress) {
        byte[] result = new byte[11];
        result[0] = (byte) 0xAB;
        result[1] = (byte) 0xAB;
        result[2] = (byte) 0x92;
        result[3] = (byte) 0x07;
        result[4] = (byte) 0x53;

        byte[] macBy=ByteStringHexUtil.hexStringToByte(macAddress);
        if(null!=macBy){
            for(int i=0,len=macBy.length;i<len;i++){
                result[i+5]=macBy[i];
            }
        }
        return result;
    }

    //发送扫描信标失败命令
    public static byte[] sendScanMarkFailure() {
        byte[] result = new byte[5];
        result[0] = (byte) 0xAB;
        result[1] = (byte) 0xAB;
        result[2] = (byte) 0x92;
        result[3] = (byte) 0x07;
        result[4] = (byte) 0x46;
        return result;
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
