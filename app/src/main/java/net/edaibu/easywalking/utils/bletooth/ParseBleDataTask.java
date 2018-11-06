package net.edaibu.easywalking.utils.bletooth;

import android.content.Context;

import net.edaibu.easywalking.R;
import net.edaibu.easywalking.utils.LogUtils;

import java.io.InputStream;

/**
 * 解析锁传过来的蓝牙数据
 */
public class ParseBleDataTask {

    public static StringBuffer sb = new StringBuffer();

    //U消息的长度
    private static int uLength=0;

    /**
     * 解析数据
     * @return
     */
    public static int parseData(Context mContext,final byte[] data){
        int result=-1;
        //获取功能码和结果码
        final int funcationCode = ByteUtil.byteToInt(data[2]);
        final int resultCode = ByteUtil.byteToInt(data[4]);
        switch (funcationCode){
            //认证回执
            case 128:
                  result=getKEY(mContext,data);
                  break;
            default:
                break;
        }
        return result;
    }


    /**
     * 通过本地密钥文件获取密钥
     */
    private static int getKEY(final Context mContext, final byte[] recive) {
        InputStream inputStream = mContext.getResources().openRawResource(R.raw.key_table);
        byte[] key_table = AesUtils.decryptFile(inputStream);
        byte[] keys = AesUtils.getKey(recive[4], recive[5], key_table);
        AesUtils.cKey = keys;
        return BleStatus.BLE_CERTIFICATION_SUCCESS;
    }


    /**
     * 组装U消息
     */
    private static void uMessage(final byte[] data){
        if (data[0] == data[1] && ByteUtil.byteToInt(data[0]) == 186 && ByteUtil.byteToInt(data[2]) == 135) {
            uLength=0;
            sb.delete(0, sb.toString().length());
            int length = ByteUtil.byteToInt(data[3]);
            uLength=(length+8)*2;
            sb.append(ByteStringHexUtil.bytesToHexString(data));
            return ;
        }
        if(sb.length()>0){
            sb.append(ByteStringHexUtil.bytesToHexString(data));
        }
        final int length=sb.toString().length();
        if(length>0 && (length==uLength || length>uLength)){
            final String uData=sb.toString();
            sb.delete(0, length);
            LogUtils.e("U数据是=" + uData);

        }
    }
}
