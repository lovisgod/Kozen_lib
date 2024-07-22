package com.lovisgod.kozenlib.core.data.dataInteractor

import EmvMessage
import com.lovisgod.kozenlib.core.data.models.EmvCardType
import com.lovisgod.kozenlib.core.data.utilsData.RequestIccData

interface EMVEvents {

    fun onInsertCard()
    fun onRemoveCard(isContactlessTransLimit: Boolean, message: String)
    fun onPinInput()
    fun onCardRead(pan: String, cardType: EmvCardType)
    fun onCardDetected(contact: Boolean = true)
    fun onEmvProcessing(message: String = "Please wait while we read card")
    fun onEmvProcessed(data: Any)
    fun onUserCanceled(message: String = "User interrupted the transaction")
    fun onTransactionCancelled(message: String = "Transaction cancelled")
}