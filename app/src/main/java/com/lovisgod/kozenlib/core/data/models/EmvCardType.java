package com.lovisgod.kozenlib.core.data.models;

import com.pos.sdk.emvcore.POIEmvCoreManager;

public enum EmvCardType {

    DEFAULT(POIEmvCoreManager.EMV_CARD_NOT, ""),
    VERVE(POIEmvCoreManager.EMV_CARD_NOT, "VERVE"),
    AFRIGO(POIEmvCoreManager.EMV_CARD_NOT, "AFRIGO"),
    VISA(POIEmvCoreManager.EMV_CARD_VISA, "VISA"),
    UNIONPAY(POIEmvCoreManager.EMV_CARD_UNIONPAY, "UnionPay"),
    MASTERCARD(POIEmvCoreManager.EMV_CARD_MASTERCARD, "MasterCard"),
    AMERICAN_EXPRESS(POIEmvCoreManager.EMV_CARD_AMEX, "American express"),
    DISCOVER(POIEmvCoreManager.EMV_CARD_DISCOVER, "Discover"),
    MIR(POIEmvCoreManager.EMV_CARD_MIR, "Mir"),
    RUPAY(POIEmvCoreManager.EMV_CARD_RUPAY, "RuPay"),
    INTERAC(POIEmvCoreManager.EMV_CARD_INTERAC, "Interac");

    private final int    type;
    private final String name;

    EmvCardType(final int type, final String name) {
        this.type = type;
        this.name = name;
    }

    public static int getCardType(String name) {
        int ret = DEFAULT.type;
        for (EmvCardType val : EmvCardType.values()) {
            if (val.name.equals(name)) {
                ret = val.type;
                break;
            }
        }
        return ret;
    }

    public static String getCardType(int type) {
        String ret = DEFAULT.name;
        for (EmvCardType val : EmvCardType.values()) {
            if (val.type == type) {
                ret = val.name;
                break;
            }
        }
        return ret;
    }


    public static EmvCardType getCardTypeX(int type) {
        EmvCardType ret = DEFAULT;
        for (EmvCardType val : EmvCardType.values()) {
            if (val.type == type) {
                ret = val;
                break;
            }
        }
        return ret;
    }

    public String getName() {
        return name;
    }
}
