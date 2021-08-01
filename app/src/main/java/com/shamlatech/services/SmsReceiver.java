package com.shamlatech.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.shamlatech.utils.Vars;

/**
 * Created by Vino on 4/27/2017.
 */

public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for (int i = 0; i < pdus.length; i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();
            String messageBody = smsMessage.getMessageBody();
            String OTP = "";
            if (sender.contains(Vars.SMS_SenderNumber)) {
                messageBody = messageBody.replace(Vars.SMS_Replace_Front, "");
                messageBody = messageBody.replace(Vars.SMS_Replace_Rear, "");
                OTP = messageBody;
            }
            if (mListener != null) {
                mListener.messageReceived(OTP.trim());
            }
        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
