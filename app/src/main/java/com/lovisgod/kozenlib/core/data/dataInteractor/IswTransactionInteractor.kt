package com.lovisgod.kozenlib.core.data.dataInteractor

import android.content.Context
import com.lovisgod.kozenlib.core.data.datasource.IswTransactionDataSource
import com.lovisgod.kozenlib.core.data.utilsData.RequestIccData

class IswTransactionInteractor(val iswTransactionDataSource: IswTransactionDataSource) {

    suspend fun startTransaction(
        hasContactless: Boolean = true,
        hasContact: Boolean = true,
        amount: Long,
        amountOther: Long,
        transType: Int,
        emvEvents: EMVEvents
    ) = iswTransactionDataSource.startTransaction(
        hasContactless,
        hasContact,
        amount,
        amountOther,
        transType,
        emvEvents
    )

    suspend fun getTransactionData(): RequestIccData =
        iswTransactionDataSource.getTransactionData()

    suspend fun setEmvContect(context: Context) =
        iswTransactionDataSource.setEmvContect(context)

    suspend fun setPinMode(pinMode: Int) = iswTransactionDataSource.setEmvPINMODE(pinMode)

    suspend fun checkCard(
        hasContactless: Boolean = true,
        hasContact: Boolean = true,
        amount: Long,
        amountOther: Long,
        transType: Int,
        emvEvents: EMVEvents
    ) = iswTransactionDataSource.checkCard(
        hasContactless,
        hasContact,
        amount,
        amountOther,
        transType,
        emvEvents
    )
}