package com.lovisgod.kozenlib.core.utilities

import com.lovisgod.kozenlib.core.data.models.EmvCardType


object CardTypeUtils {

    private const val PATTERN_VISA = "^4[0-9]{12}(?:[0-9]{3})?$"
    private const val PATTERN_MASTERCARD = "^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$"
    private const val PATTERN_AMERICAN_EXPRESS = "^3[47][0-9]{13}$"
    private const val PATTERN_VERVE = "^((506([01]))|(507([89]))|(6500))[0-9]{12,15}$"
    private const val PATTERN_CUP = "^(62|88)\\d+\$"
    private const val PATTERN_DINERS_CLUB = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$"
    private const val PATTERN_DISCOVER = "^6(?:011|5[0-9]{2})[0-9]{12}$"
    private const val PATTERN_JCB = "^(?:2131|1800|35[0-9]{3})[0-9]{11}$"
    private const val PATTERN_UNION_PAY = "^(62[0-9]{14,17})$"
    private const val PATTERN_AFRIGO = "^5640\\d{10,15}$"

    fun getCardType(cardPan: String): EmvCardType{
        if(PATTERN_VISA.toRegex().matches(cardPan)) return EmvCardType.VISA
        if(PATTERN_MASTERCARD.toRegex().matches(cardPan)) return EmvCardType.MASTERCARD
        if(PATTERN_AMERICAN_EXPRESS.toRegex().matches(cardPan)) return EmvCardType.AMERICAN_EXPRESS
        if(PATTERN_VERVE.toRegex().matches(cardPan)) return EmvCardType.VERVE
        if(PATTERN_CUP.toRegex().matches(cardPan)) return EmvCardType.INTERAC
        if(PATTERN_AFRIGO.toRegex().matches(cardPan)) return EmvCardType.AFRIGO
        if(PATTERN_UNION_PAY.toRegex().matches(cardPan)) return EmvCardType.UNIONPAY

        return EmvCardType.DEFAULT
    }
}