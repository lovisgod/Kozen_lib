package com.lovisgod.kozenlib.core.utilities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.interswitchng.smartpos.shared.utilities.console
import com.lovisgod.kozenlib.core.data.dataInteractor.EMVEvents
import com.lovisgod.kozenlib.core.data.models.EmvCard
import com.lovisgod.kozenlib.core.data.models.EmvCardType
import com.lovisgod.kozenlib.core.data.models.EmvPinData
import com.lovisgod.kozenlib.core.data.utilsData.Constants
import com.lovisgod.kozenlib.core.data.utilsData.REQUEST_TAGS
import com.lovisgod.kozenlib.core.data.utilsData.getIccData
import com.pos.sdk.emvcore.IPosEmvCoreListener
import com.pos.sdk.emvcore.POIEmvCoreManager
import com.pos.sdk.emvcore.POIEmvCoreManager.EmvCardInfoConstraints
import com.pos.sdk.emvcore.PosEmvErrorCode


class CardCheckerHandler {

    // communication channel with cardreader
//    private val channel = Channel<com.interswitchng.smartpos.coreFunctionality.core.data.models.EmvMessage>()

    var emvCoreManager: POIEmvCoreManager? = null
    var emvCoreListener: POIEmvCoreListener? = null
    var mode:Int = 0
    var cardType: Int = 0
    var context: Context? = null
    var activity: Activity? = null
    var emvEvents: EMVEvents? = null
    var emvCardType = EmvCardType.DEFAULT


    fun setEmvContext(context: Context) {
        this.context = context
    }


    fun checkCard(
        hasContactless: Boolean = true,
        hasContact: Boolean = true,
        amount: Long,
        amountOther: Long,
        transType: Int,
        emvEvents: EMVEvents
    ) {

        try {
            this.emvEvents = emvEvents
            this.emvEvents!!.onInsertCard()

            emvCoreManager = POIEmvCoreManager.getDefault()
            emvCoreListener = POIEmvCoreListener()

            val bundle = Bundle()
            bundle.putInt(POIEmvCoreManager.EmvTransDataConstraints.TRANS_TYPE, transType)
            bundle.putLong(POIEmvCoreManager.EmvTransDataConstraints.TRANS_AMOUNT, amount)
            bundle.putLong(
                POIEmvCoreManager.EmvTransDataConstraints.TRANS_AMOUNT_OTHER,
                amountOther
            )

            var mode = 0
            if (hasContact) {
                mode = mode or POIEmvCoreManager.DEVICE_CONTACT
            }
            if (hasContactless) {
                mode = mode or POIEmvCoreManager.DEVICE_CONTACTLESS
            }

            bundle.putInt(POIEmvCoreManager.EmvTransDataConstraints.TRANS_MODE, mode)

            bundle.putInt(POIEmvCoreManager.EmvTransDataConstraints.TRANS_TIMEOUT, 60)

            // Adds a delay after the card is detected before command interchange between the card and terminal begins
            // Serves to fix a bug where the card chip sometimes does not have sufficient time to power up before the Terminal starts to send commands
            bundle.putBoolean(POIEmvCoreManager.EmvTransDataConstraints.SPECIAL_CONTACT, true)
            bundle.putBoolean(POIEmvCoreManager.EmvTransDataConstraints.SPECIAL_MAGSTRIPE, true)
            bundle.putInt(POIEmvCoreManager.EmvTransDataConstraints.SPECIAL_CONTACT_TIME, 1000)
            bundle.putInt(POIEmvCoreManager.EmvTransDataConstraints.SPECIAL_MAGSTRIPE_TIME, 1000)

            // check if it gets here
            val result = emvCoreManager!!.startTransaction(bundle, emvCoreListener)
            if (PosEmvErrorCode.EXCEPTION_ERROR == result) {
                Log.e("EXCEPTION_ERROR", "startTransaction exception error")
            } else if (PosEmvErrorCode.EMV_ENCRYPT_ERROR == result) {
                Log.e("EMV_ERROR", "startTransaction encrypt error")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }






    // POI LISTENER
    inner class POIEmvCoreListener : IPosEmvCoreListener.Stub() {
        private val TAG = "PosEmvCoreListener"
        override fun onEmvProcess(type: Int, bundle: Bundle?) {
            cardType = type
            when (type) {
                POIEmvCoreManager.DEVICE_CONTACT -> {
                    console.log("card transaction type","Contact Card Trans")
                    this@CardCheckerHandler.emvEvents?.onCardDetected(true)
                }
                POIEmvCoreManager.DEVICE_CONTACTLESS -> {
                    console.log("card transaction type", "Contactless Card Trans")
                    this@CardCheckerHandler.emvEvents?.onCardDetected(false)
                }
                POIEmvCoreManager.DEVICE_MAGSTRIPE -> { console.log("card transaction type","Magstripe Card Trans") }
                else -> {
                }
            }

        }

        override fun onSelectApplication(list: List<String>, isFirstSelect: Boolean) {
            console.log("", "called application selection")
            val names = list.toTypedArray()
            emvCoreManager?.onSetSelectResponse(1)

        }

        override fun onConfirmCardInfo(mode: Int, bundle: Bundle?) {
            var cardPanRead = bundle?.getByteArray(EmvCardInfoConstraints.TRACK2)
            println("this is cardread2 $cardPanRead")
            val outBundle = Bundle()
            if (mode == POIEmvCoreManager.CMD_AMOUNT_CONFIG) {
                outBundle.putString(EmvCardInfoConstraints.OUT_AMOUNT, "11")
                outBundle.putString(EmvCardInfoConstraints.OUT_AMOUNT_OTHER, "22")
            } else if (mode == POIEmvCoreManager.CMD_TRY_OTHER_APPLICATION) {
                outBundle.putBoolean(EmvCardInfoConstraints.OUT_CONFIRM, true)
            } else if (mode == POIEmvCoreManager.CMD_ISSUER_REFERRAL) {
                outBundle.putBoolean(EmvCardInfoConstraints.OUT_CONFIRM, true)
            }
            emvCoreManager?.onSetCardInfoResponse(outBundle)
        }

        override fun onKernelType(type: Int) {
            emvCardType = EmvCardType.getCardTypeX(type)
//            this@CardCheckerHandler.emvEvents?.onCardRead("", emvCardType)
        }

        override fun onSecondTapCard() {
            console.log("","second tap needed")
        }

        override fun onRequestInputPin(bundle: Bundle?) {
            console.log("pin input", "kindly input pin")
            emvCoreManager?.stopTransaction()

        }

        override fun onRequestOnlineProcess(bundle: Bundle) {
            emvCoreManager?.stopTransaction()
        }

        override fun onTransactionResult(result: Int, bundle: Bundle) {


            when (result) {
                PosEmvErrorCode.EMV_CANCEL -> {
                    println("Transaction cancelled")
                    this@CardCheckerHandler.emvEvents?.onTransactionCancelled()
                    return
                }

                PosEmvErrorCode.EMV_TIMEOUT -> {
                    println("Transaction timed out")
                    this@CardCheckerHandler.emvEvents?.onTransactionCancelled()
                    return
                }

                PosEmvErrorCode.EMV_OTHER_INTERFACE -> {
//                    this@CardCheckerHandler.emvEvents?.onRemoveCard(true, "Contactless Transaction Limit Exceeded")
                    this@CardCheckerHandler.emvEvents?.onTransactionCancelled("Use Other ICC Interface - test")
                }

                PosEmvErrorCode.EMV_COMMAND_FAIL -> {
                    this@CardCheckerHandler.emvEvents?.onTransactionCancelled("Transaction Cancelled - EMV Command Failed")
                }

                PosEmvErrorCode.EMV_NOT_ALLOWED,
                PosEmvErrorCode.EMV_APP_EMPTY,
                PosEmvErrorCode.EMV_NOT_ACCEPTED -> {
                    println("Transaction cancelled")
                    this@CardCheckerHandler.emvEvents?.onTransactionCancelled("User interrupted the transaction")
                    return
                }
                else -> {
                }
            }
            var data: ByteArray?
            var encryptData: ByteArray?
            var cardPanRead = bundle.getByteArray(EmvCardInfoConstraints.TRACK2)
            println("this is cardread2 $cardPanRead")
            Log.d(TAG, "onTransactionResult $result")

            data = bundle.getByteArray(POIEmvCoreManager.EmvResultConstraints.EMV_DATA)
            if (data != null) {
                Log.d(TAG, "Trans Data : " + HexUtil.toHexString(data))
//                iccData?.iccAsString = HexUtil.toHexString(data)

                if (data != null) {
                    val tlvBuilder = BerTlvBuilder()
                    val tlvParser = BerTlvParser()
                    val tlvs = tlvParser.parse(data)
                    for (tag in REQUEST_TAGS) {
                        if (tlvs.find(BerTag(tag.tag)) != null) {
                            println("not null here ::::")
                            tlvBuilder.addBerTlv(tlvs.find(BerTag(tag.tag))!!)
                        }
                    }

                    data = tlvBuilder.buildArray()

                    if (data != null) {
                        EmvHandler.iccString = HexUtil.toHexString(data)
                        Log.d(TAG, "Trans Data ccccccc:::::::: " + EmvHandler.iccString)
                    }
                    if (data != null) {
                        val emvCard = EmvCard(data)
                        var  iccDataX =  getIccData(data)
                        if (emvCard.getCardNumber() != null) {
                            iccDataX?.EMC_CARD_ = emvCard
                        }

                        if (!Constants.memoryPinData.pinBlock.isNullOrEmpty()) {
                            iccDataX?.EMV_CARD_PIN_DATA = EmvPinData(
                                CardPinBlock = Constants.memoryPinData.pinBlock,
                                ksn = Constants.memoryPinData.ksn
                            )

                        } else {
                            iccDataX?.EMV_CARD_PIN_DATA = EmvPinData(
                                CardPinBlock = "",
                                ksn = ""
                            )
                        }
                        iccDataX?.haspin = !iccDataX?.EMV_CARD_PIN_DATA?.CardPinBlock.isNullOrEmpty()

                        val isIcSlot = cardType == POIEmvCoreManager.DEVICE_CONTACT

                        if(isIcSlot) {
                            Constants.POS_ENTRY_MODE = "051"
                            if (iccDataX?.haspin!!) {
                                Constants.POS_DATA_CODE = Constants.CONTACT_POS_DATA_CODE_PIN
                            } else {
                                Constants.POS_DATA_CODE = Constants.CONTACT_POS_DATA_CODE_PIN
                            }
                        } else {
                            Constants.POS_ENTRY_MODE = "071"
                            Constants.POS_DATA_CODE = Constants.CLSS_POS_DATA_CODE
                        }

                        iccDataX.EMC_CARD_?.cardNumber?.let {
                            println("card pan::::: $it")
                            this@CardCheckerHandler.emvEvents?.onCardRead(
                                it,
                                CardTypeUtils.getCardType(it))
                        }

                        println(
                            "iccData => {" +
                                    "date: ${iccDataX?.TRANSACTION_DATE} " +
                                    "name: ${iccDataX?.CARD_HOLDER_NAME} " +
                                    "amount: ${iccDataX?.TRANSACTION_AMOUNT} " +
                                    "Track2Data ${iccDataX?.TRACK_2_DATA}" +
                                    "haspin ${iccDataX?.haspin}" +
                                    "iccString ${iccDataX?.iccAsString}" +
                                    "}"
                        )


                        println("iccccc =>->=>>>>>>>>>>>>>>> ${EmvHandler.iccString}")
                    }
                }
            }
            encryptData =
                bundle.getByteArray(POIEmvCoreManager.EmvResultConstraints.ENCRYPT_DATA)
            if (encryptData != null) {
                Log.d(TAG, "Encrypt Data : " + HexUtil.toHexString(encryptData))
                if (!HexUtil.toHexString(encryptData).isNullOrEmpty()) {
//                    iccData?.iccAsString = HexUtil.toHexString(data)
                }
            }
        }
    }


    companion object {

    }


}




