package com.lovisgod.kozenlib.core.data.dataSourceImpl

import android.content.Context
import com.lovisgod.kozenlib.core.data.dataInteractor.EMVEvents
import com.lovisgod.kozenlib.core.data.datasource.IswTransactionDataSource
import com.lovisgod.kozenlib.core.data.models.TransactionData
import com.lovisgod.kozenlib.core.data.utilsData.RequestIccData
import com.lovisgod.kozenlib.core.utilities.CardCheckerHandler2
import com.lovisgod.kozenlib.core.utilities.EmvHandler

class IswTransactionImpl(val emvHandler: EmvHandler,
                         val checkerHandler: CardCheckerHandler2): IswTransactionDataSource {
    override suspend fun startTransaction(
        hasContactless: Boolean,
        hasContact: Boolean,
        amount: Long,
        amountOther: Long,
        transType: Int,
        emvEvents: EMVEvents
    ) {
        emvHandler.startTransaction(
            hasContactless,
            hasContact,
            amount,
            amountOther,
            transType,
            emvEvents)
    }

    override suspend fun getTransactionData(): RequestIccData {
       return emvHandler.iccData!!
    }

    override suspend fun setEmvContect(context: Context) {
        emvHandler.setEmvContext(context)
    }

    override suspend fun setEmvPINMODE(pinMode: Int) {
        emvHandler.setEmvPINMODE(pinMode)
    }

    override suspend fun checkCard(
        hasContactless: Boolean,
        hasContact: Boolean,
        amount: Long,
        amountOther: Long,
        transType: Int,
        emvEvents: EMVEvents
    ) {
        checkerHandler.checkCard(
            hasContactless,
            hasContact,
            amount,
            amountOther,
            transType,
            emvEvents
        )
    }
}