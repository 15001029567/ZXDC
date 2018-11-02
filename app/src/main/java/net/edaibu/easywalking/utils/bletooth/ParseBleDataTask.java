package net.edaibu.easywalking.utils.bletooth;

import android.content.Context;

import net.edaibu.easywalking.R;
import net.edaibu.easywalking.service.BleService;

import java.io.InputStream;

/**
 * 解析锁传过来的蓝牙数据
 */
public class ParseBleDataTask {

    /**
     * 解析数据
     * @return
     */
    public static int parseData(Context mContext,final byte[] data){
        int result=-1;
        if(null==data){
            return result;
        }
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
}
