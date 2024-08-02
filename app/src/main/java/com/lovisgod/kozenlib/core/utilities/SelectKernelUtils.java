package com.lovisgod.kozenlib.core.utilities;

import android.os.Bundle;

import com.pos.sdk.emvcore.POIEmvCoreManager;
import com.pos.sdk.emvcore.POIEmvCoreManager.EmvCardInfoConstraints;

import java.util.List;

public class SelectKernelUtils {

    private static final byte KERNEL_VISA       = 0x01;
    private static final byte KERNEL_UNIONPAY   = 0x02;
    private static final byte KERNEL_MASTERCARD = 0x03;
    private static final byte KERNEL_DISCOVER   = 0x04;
    private static final byte KERNEL_AMEX       = 0x05;
    private static final byte KERNEL_JCB        = 0x06;
    private static final byte KERNEL_MIR        = 0x07;
    private static final byte KERNEL_RUPAY      = 0x08;
    private static final byte KERNEL_PURE       = 0x09;
    private static final byte KERNEL_INTERAC    = 0x0A;
    private static final byte KERNEL_EFTPOS     = 0x0B;

    private static final String[] visa       = new String[]{"A000000003"};
    private static final String[] mastercard = new String[]{"A000000004"};

    private static final String[] pure = new String[]{"A000000371"};


    public static void doSelectKernel(byte[] data) {
        byte kernel = 0;

        List<BerTlv> tlvs = new BerTlvParser().parse(data).findAll(new BerTag("DF01"));
        for (BerTlv tlv : tlvs) {
            BerTlvs tlvs1 = new BerTlvParser().parse(tlv.getBytesValue());
            BerTlv aid = tlvs1.find(new BerTag("4F"));
//            BerTlv label = tlvs1.find(new BerTag("50"));
//            BerTlv preferredName = tlvs1.find(new BerTag("9F12"));
//            BerTlv priority = tlvs1.find(new BerTag("87"));
            if (aid != null && aid.getHexValue().length() >= 5) {
                String val = HexUtil.toHexString(aid.getBytesValue(), 0, 5);
                if (is(val, visa)) {
                    kernel = KERNEL_VISA;
                } else if (is(val, mastercard)) {
                    kernel = KERNEL_MASTERCARD;
                } else if (is(val, pure)) {
                    kernel = KERNEL_PURE;
                }
            }
        }

        Bundle bundle = new Bundle();

        if (kernel == 0) {
//            bundle.putByteArray(EmvCardInfoConstraints.OUT_TLV, new byte[0]);
            POIEmvCoreManager.getDefault().onSetCardInfoResponse(bundle);
            return;
        }

        BerTlvBuilder tlvBuilder = new BerTlvBuilder();
        tlvBuilder.addByte(new BerTag("DF10"), kernel);
//        bundle.putByteArray(EmvCardInfoConstraints.OUT_TLV, tlvBuilder.buildArray());
        POIEmvCoreManager.getDefault().onSetCardInfoResponse(bundle);
    }

    private static boolean is(String aid, String[] aids) {
        for (String var : aids) {
            if (aid.equalsIgnoreCase(var)) {
                return true;
            }
        }
        return false;
    }
}
