package net.edaibu.easywalking.http.base;

import android.os.Handler;
import android.os.Message;
/**
 * Created by lyn on 2017/5/9.
 */

public class BaseRequst {

    public static void sendMessage(Handler handler, int wat, Object obj) {
        if(null==handler){
            return;
        }
        Message message = Message.obtain();
        message.what = wat;
        message.obj = obj;
        handler.sendMessage(message);
    }
}
