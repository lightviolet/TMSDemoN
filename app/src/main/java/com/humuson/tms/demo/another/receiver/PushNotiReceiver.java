package com.humuson.tms.demo.another.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.tms.sdk.TMS;

/**
 * Created by daeyoon on 2016. 11. 6..
 */

public class PushNotiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "push notiMsg : " + intent.getStringExtra(TMS.KEY_NOTI_MSG),
                Toast.LENGTH_SHORT).show();
    }

}
