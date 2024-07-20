package com.lovisgod.kozenlib.core.data.utilsData

import com.lovisgod.kozenlib.BuildConfig
import com.lovisgod.kozenlib.core.network.models.AdditionalTransactionInfo
import com.lovisgod.kozenlib.core.network.models.MemoryPinData
import com.pixplicity.easyprefs.library.Prefs
import java.util.*

object Constants {

    var isHardWareKeyBoard = true
    var memoryPinData = MemoryPinData()

    // Amount Limits For Contactless Payments
    const val CONTACTLESS_CVM_LIMIT: Int = 500000
    const val CONTACTLESS_FLOOR_LIMIT: Int = 0
    const val CONTACTLESS_TRANS_LIMIT: Int = 2000000
    const val CONTACTLESS_TRANS_NO_DEVICE_LIMIT: Int = 2000000

    // Pref Keys For Contactless Payments
    const val KEY_CONTACTLESS_CVM_LIMIT = "contactless_cvm_limit"
    const val KEY_CONTACTLESS_FLOOR_LIMIT = "contactless_floor_limit"
    const val KEY_CONTACTLESS_TRANS_LIMIT = "contactless_trans_limit"
    const val KEY_CONTACTLESS_TRANS_NO_DEVICE_LIMIT = "contactless_trans_no_device_limit"


    val TOKEN: String = "TOKEN"
    internal const val EXCEPTION_CODE = 9000
    internal const val TERMINAL_INFO_KEY = "TERMINAL_INFO_KEY"
    internal const val ISW_TOKEN_URL = "/kimonotms/requesttoken/perform-process"

    // URL END POINTS
    internal const val CODE_END_POINT = "till.json"
    internal const val TRANSACTION_STATUS_QR = "transactions/qr"
    internal const val TRANSACTION_STATUS_USSD = "transactions/ussd.json"
    internal const val TRANSACTION_STATUS_TRANSFER = "virtualaccounts/transaction"
    internal const val BANKS_END_POINT = "till/short-codes/1"
    internal const val AUTH_END_POINT = "oauth/token"
    internal const val KIMONO_KEY_END_POINT = "/kmw/keydownloadservice"

    internal const val THANKYOU_MERCHANT_CODE = "THANKYOU_MERCHANT_CODE"


    //    internal const val KIMONO_END_POINT = "kmw/v2/kimonoservice"
    internal const val KIMONO_END_POINT = "kmw/kimonoservice"
    internal const val KIMONO_MERCHANT_DETAILS_END_POINT = "kmw/serialid/{terminalSerialNo}.xml"
    internal const val KIMONO_MERCHANT_DETAILS_END_POINT_AUTO = "kmw/v2/serialid/{terminalSerialNo}"

    // EMAIL
    internal const val EMAIL_END_POINT = "mail/send"
    internal const val EMAIL_TEMPLATE_ID = "d-c33c9a651cea40dd9b0ee4615593dcb4"

    internal const val KEY_PAYMENT_INFO = "payment_info_key"

    internal const val KEY_MASTER_KEY = "master_key"
    internal const val KEY_SESSION_KEY = "session_key"
    internal const val KEY_PIN_KEY = "pin_key"
    internal const val KIMONO_KEY = "kimono_key"

    const val KEY_ADMIN_PIN = "terminal_admin_access_pin_key"
    internal const val TERMINAL_CONFIG_TYPE = "kimono_or_nibss"
    internal const val SETTINGS_TERMINAL_CONFIG_TYPE = "settings_kimono_or_nibss"
    // UTIL CONSTANTS
     const val CALL_HOME_TIME_IN_MIN = "60"
     const val SERVER_TIMEOUT_IN_SEC = "60"
     const val TERMINAL_CAPABILITIES = "E040C8"
     const val CURRENCY_CODE = "566"
     const val COUNTRY_CODE = "566"



    const val THANKU_CASH_PROD = "https://api.thankucash.com/api/v1/thankuconnect/"
    const val THANKU_CASH_TEST = "https://testapi.thankucash.com/api/v1/thankuconnect/"
    const val THANKYOU_REWARD = "settletransaction"
    const val THANKYOU_BALANCE = "getbalance"
    const val THANKYOU_REDEEM = "redeem"
    const val THANKYOU_CONFIRM_REDEEM = "confirmredeem"
    const val THANKYOU_FAILED = "notifyfailedtransaction"
    const val THANKYOU_TEST_KEY = "7cffb3dd67d04770b713db09c8802d97"
    const val THANKYOU_LIVE_KEY = "bec8653108884269b5513c40e179b77e"



    val ISW_USSD_QR_BASE_URL: String get() {
//        val iswPos = IswPos.getInstance()
//        return if (iswPos.config.environment == Environment.Test) Test.ISW_USSD_QR_BASE_URL
//        else Production.ISW_USSD_QR_BASE_URL
        return if (checkEnv()) Production.ISW_USSD_QR_BASE_URL
        else Production.ISW_USSD_QR_BASE_URL
    }

    val ISW_TOKEN_BASE_URL: String get() {
//        val iswPos = IswPos.getInstance()
        return if (checkEnv()) Test.ISW_TOKEN_BASE_URL
        else Production.ISW_TOKEN_BASE_URL
    }

    val ISW_IMAGE_BASE_URL: String get() {
//        val iswPos = IswPos.getInstance()
        return if (checkEnv()) Test.ISW_IMAGE_BASE_URL
        else Production.ISW_IMAGE_BASE_URL
    }

    val ISW_TERMINAL_IP: String get() {
//        val iswPos = IswPos.getInstance()
        return if (!checkEnv()) Test.ISW_TERMINAL_IP_CTMS
        else Production.ISW_TERMINAL_IP_CTMS
    }

    val ISW_DEFAULT_TERMINAL_IP: String get() {
//        val iswPos = IswPos.getInstance()
        return if (checkEnv()) Test.ISW_TERMINAL_IP_CTMS
        else Production.ISW_TERMINAL_IP_CTMS
    }

    val ISW_DEFAULT_TERMINAL_PORT: String get() {
//        val iswPos = IswPos.getInstance()
        return if (checkEnv()) Test.ISW_TERMINAL_PORT_CTMS.toString()
        else Production.ISW_TERMINAL_PORT_CTMS.toString()
    }

    val ISW_TERMINAL_PORT_CTMS_FOR_SETTINGS: String get() {
//        val iswPos = IswPos.getInstance()
        return if (checkEnv()) Test.ISW_TERMINAL_PORT_CTMS.toString()
        else Production.ISW_TERMINAL_PORT_CTMS.toString()
    }

    val ISW_TERMINAL_IP_CTMS_FOR_SETTINGS: String get() {
//        val iswPos = IswPos.getInstance()
        return if (checkEnv()) Test.ISW_TERMINAL_IP_CTMS
        else Production.ISW_TERMINAL_IP_CTMS
    }

    val ISW_DUKPT_IPEK: String get() {
//        val iswPos = IswPos.getInstance()
        return if(checkEnv()) KeysUtils.testIPEK()
        else KeysUtils.productionIPEK()
    }


    val ISW_DUKPT_KSN: String get() {
//        val iswPos = IswPos.getInstance()
        return if(checkEnv()) KeysUtils.testKSN()
        else KeysUtils.productionKSN()
    }

    fun getCMS(isEpms: Boolean): String {
//        val iswPos = IswPos.getInstance()
        //return KeysUtils.testCMS()
        return if(!checkEnv()) KeysUtils.testCMS(isEpms)
        else KeysUtils.productionCMS(isEpms)

    }


    val ISW_KIMONO_BASE_URL: String get() {
        return if(checkEnv()) Production.ISW_KIMONO_BASE_URL
        else Production.ISW_KIMONO_BASE_URL
    }

    val ISW_KIMONO_URL: String get() {
        return if(checkEnv()) Test.ISW_KIMONO_URL
        else Production.ISW_KIMONO_URL
    }

    val PAYMENT_CODE: String get() {
        return if(checkEnv()) Test.PAYMENT_CODE
        else Production.PAYMENT_CODE
    }

    val ISW_TERMINAL_PORT: Int get() {
        return if(!checkEnv()) Test.ISW_TERMINAL_PORT_CTMS
        else Production.ISW_TERMINAL_PORT_CTMS
    }

    val ISW_KIMONO_KEY_URL: String
        get() {
            return Production.ISW_KEY_DOWNLOAD_URL
        }

    private object Production {

        const val ISW_USSD_QR_BASE_URL = "https://api.interswitchng.com/paymentgateway/api/v1/"
        const val ISW_TOKEN_BASE_URL = "https://passport.interswitchng.com/passport/"
        const val ISW_IMAGE_BASE_URL = "https://mufasa.interswitchng.com/p/paymentgateway/"
        const val ISW_KIMONO_URL = "https://kimono.interswitchng.com/kmw/v2/kimonoservice"
        const val ISW_KIMONO_BASE_URL = "https://kimono.interswitchng.com/"
        const val ISW_TERMINAL_IP_EPMS = "196.6.103.73"
        const val ISW_TERMINAL_PORT_EPMS = 5043

        const val ISW_TERMINAL_IP_CTMS = "196.6.103.18"
        const val ISW_TERMINAL_PORT_CTMS = 5008
        const val ISW_KEY_DOWNLOAD_URL = "http://kimono.interswitchng.com/kmw/keydownloadservice"
        const val PAYMENT_CODE = "04358001"
    }

    private object Test {
//        const val ISW_USSD_QR_BASE_URL = "https://api.interswitchng.com/paymentgateway/api/v1/"
        const val ISW_USSD_QR_BASE_URL = "https://project-x-merchant.k8.isw.la/paymentgateway/api/v1/"
        const val ISW_TOKEN_BASE_URL = "https://passport.interswitchng.com/passport/"
        const val ISW_IMAGE_BASE_URL = "https://mufasa.interswitchng.com/p/paymentgateway/"
        const val ISW_KIMONO_URL = "https://qa.interswitchng.com/kmw/v2/kimonoservice"
        const val ISW_KIMONO_BASE_URL = "https://qa.interswitchng.com/"
        const val ISW_TERMINAL_IP_EPMS = "196.6.103.72"
        const val ISW_TERMINAL_PORT_EPMS = 5043

        const val ISW_TERMINAL_IP_CTMS = "196.6.103.10"
        const val ISW_TERMINAL_PORT_CTMS = 55533
        const val PAYMENT_CODE = "051626554287"
    }


    fun checkEnv () : Boolean {
        return BuildConfig.DEBUG
    }

    /**
     * This method returns the next STAN (System Trace Audit Number)
     */
     fun getNextStan(): String {
        var stan = Prefs.getInt("STAN", 0)

        // compute and save new stan
        val newStan = if (stan > 999999) 0 else ++stan
        Prefs.putInt("STAN", newStan)

        return String.format(Locale.getDefault(), "%06d", newStan)
    }


    /**
     * This method returns the next STAN (System Trace Audit Number)
     */
    fun getNextKsnCounter(): String {
        var ksn = Prefs.getInt("KSNCOUNTER", 0)

        // compute and save new stan
        val newKsn = if (ksn >= 9) 1 else ++ksn
        Prefs.putInt("KSNCOUNTER", ksn)

        return newKsn.toString()
    }



    var CLSS_POS_DATA_CODE = "A10101711344101"
    var CONTACT_POS_DATA_CODE_PIN = "510101511344101"
    var CONTACT_POS_DATA_CODE_NO_PIN = "511101511344101"
    var POS_ENTRY_MODE = "051"
    var POS_DATA_CODE = ""
    const val TIMEOUT_CODE = "0x0x0"

     var additionalTransactionInfo = AdditionalTransactionInfo()

}