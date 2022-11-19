package com.example.nfcard;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;

public class CardEmulateService extends HostApduService {
    @Override
    public byte[] processCommandApdu(byte[] bytes, Bundle bundle) {
        return new byte[0];
    }

    @Override
    public void onDeactivated(int i) {
        String tag = "B4547445";
    }
}
