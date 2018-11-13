package net.edaibu.easywalking.utils.bletooth;

import android.content.Context;
import net.edaibu.easywalking.R;
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
        int result=BleStatus.BLE_NORMAL_STATE;
        //获取功能码
        final int funcationCode = ByteUtil.byteToInt(data[2]);
        //获取动作指令
        final int playCode = ByteUtil.byteToInt(data[3]);
        //获取结果码
        final int resultCode=ByteUtil.byteToInt(data[5]);
        switch (funcationCode){
            //认证回执
            case 128:
                  result=getKEY(mContext,data);
                  break;
            //锁动作回执
            case 129:
                  switch (playCode){
                      //开锁回执
                      case 1:
                           switch (resultCode){
                               //开锁成功
                               case 83:
                                    result=BleStatus.BLE_OPEN_LOCK_SUCCESS;
                                    break;
                               //开锁失败
                               case 70:
                                    result=BleStatus.BLE_OPEN_LOCK_FAILURE;
                                    break;
                           }
                          break;
                      //结算还车回执
                      case 2:
                           switch (resultCode){
                               //结算还车成功
                               case 83:
                                   result=BleStatus.BLE_PAY_CLOSE_LOCK_SUCCESS;
                                   break;
                                //结算锁车失败：当前处于禁停区
                               case 1:
                                    result=BleStatus.BLE_PAY_CLOSE_LOCK_FAILURE_JTQ;
                                    break;
                                //结算锁车失败：当前处于骑行围栏外
                               case 2:
                                    result=BleStatus.BLE_PAY_CLOSE_LOCK_FAILURE_QJWL;
                                    break;
                           }
                           break;
                      //临时关锁回执
                      case 3:
                           switch (resultCode){
                               //临时关锁成功
                               case 83:
                                   result=BleStatus.BLE_CLOSE_LOCK_SUCCESS;
                                   break;
                               //临时关锁失败：当前处于禁停区
                               case 1:
                                   result=BleStatus.BLE_CLOSE_LOCK_FAILURE_JTQ;
                                   break;
                               //临时关锁失败：当前处于骑行围栏外
                               case 2:
                                   result=BleStatus.BLE_CLOSE_LOCK_FAILURE_QXWL;
                                   break;
                           }
                           break;
                      //强制锁车回执
                      case 4:
                           switch (resultCode){
                               //强制锁车成功
                               case 83:
                                   result=BleStatus.FORCE_CLOSE_LOCK_SUCCESS;
                                   break;
                               //强制锁车失败
                               case 70:
                                   result=BleStatus.FORCE_CLOSE_LOCK_FAILURE;
                                   break;
                           }
                           break;
                      //响铃回执
                      case 5:
                          switch (resultCode){
                              //响铃成功
                              case 83:
                                  result=BleStatus.BLE_FLASH_SUCCESS;
                                  break;
                              //响铃失败
                              case 70:
                                  result=BleStatus.BLE_FLASH_FAILURE;
                                  break;
                          }
                          break;
                      //一键关锁回执
                      case 6:
                          switch (resultCode){
                              //一键关锁成功
                              case 83:
                                  result=BleStatus.BLE_PAY_CLOSE_LOCK_SUCCESS;
                                  break;
                              //一键关锁失败：当前处于禁停区
                              case 1:
                                  break;
                              //一键关锁失败：当前处于骑行围栏外
                              case 2:
                                  break;
                          }
                           break;
                       default:
                           break;
                  }
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
