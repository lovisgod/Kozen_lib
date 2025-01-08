package com.lovisgod.kozenlib.core.data.dataSourceImpl

import android.util.Log
import com.google.gson.Gson
import com.lovisgod.kozenlib.core.data.datasource.IswDetailsAndKeyDataSource
import com.lovisgod.kozenlib.core.data.models.IswTerminalModel
import com.lovisgod.kozenlib.core.data.models.TerminalInfo
import com.lovisgod.kozenlib.core.data.utilsData.Constants.TERMINAL_INFO_KEY
import com.lovisgod.kozenlib.core.data.utilsData.Constants.TOKEN
import com.lovisgod.kozenlib.core.network.AuthInterfaceKozen
import com.lovisgod.kozenlib.core.network.KimonoInterfaceKozen
import com.lovisgod.kozenlib.core.network.models.TokenRequestModelKozen
import com.lovisgod.kozenlib.core.network.models.convertConfigResponseToAllTerminalInfo
import com.lovisgod.kozenlib.core.utilities.HexUtil
import com.lovisgod.kozenlib.core.utilities.ISWGeneralException
import com.pixplicity.easyprefs.library.Prefs
import com.pos.sdk.security.POIHsmManage
import com.pos.sdk.security.PedKcvInfo
import com.pos.sdk.security.PedKeyInfo
import kotlin.jvm.Throws

class IswDetailsAndKeysImpl(val authInterfaceKozen: AuthInterfaceKozen,
                            val kimonoInterfaceKozen: KimonoInterfaceKozen): IswDetailsAndKeyDataSource {
    override suspend fun writeDukPtKey(keyIndex: Int, keyData: String, KsnData: String): Int {
        val kcvInfo = PedKcvInfo(0, ByteArray(5))
//        Prefs.putString("IPEK", keyData)
//        Prefs.putString("KSN", KsnData.dropLast(1))

        val hexData = padArray(HexUtil.parseHex(keyData), 16)

        val writeDukptResult = POIHsmManage.getDefault().PedWriteTIK(
            keyIndex,
            0,
            hexData.size,
            hexData,
            HexUtil.parseHex(KsnData),
            kcvInfo
        )

        return writeDukptResult
    }

    private fun padArray(original: ByteArray, targetSize: Int, paddingByte: Byte = 0xFF.toByte()): ByteArray {
        return ByteArray(targetSize) { i ->
            if (i < original.size) original[i] else paddingByte
        }
    }

    override suspend fun writePinKey(keyIndex: Int, keyData: String): Int {
        val pedKeyInfo =
            PedKeyInfo(0, 0, POIHsmManage.PED_TPK, keyIndex, 0, 16, HexUtil.parseHex(keyData))
        return POIHsmManage.getDefault().PedWriteKey(
            pedKeyInfo,
            PedKcvInfo(0, ByteArray(5)))
    }

    override suspend fun loadMasterKey(masterkey: String) {
        val pedKeyInfo =
            PedKeyInfo(0, 0, POIHsmManage.PED_TMK, 1, 0, 16, HexUtil.parseHex(masterkey))
         POIHsmManage.getDefault().PedWriteKey(
            pedKeyInfo,
            PedKcvInfo(0, ByteArray(5)))
    }

    override suspend fun downloadTerminalDetails(terminalData: IswTerminalModel) {
       var response = authInterfaceKozen.getMerchantDetails(terminalData.serialNumber).run()
       if (response.isSuccessful) {
           if (response.body() != null) {
               var configData = response.body()
               configData.let {
                   if (it != null) {
                       convertConfigResponseToAllTerminalInfo(it).let {
                           it.terminalInfo?.let { info ->
                               println("terminal info ::::: ${info.toString()}")
                               info.qtbMerchantCode = "MX1065"
                               info.qtbMerchantAlias = "002208"
                               info.nibbsKey = it.tmsRouteTypeConfig?.key.toString()
                               saveTerminalInfo(info)
                           }
                       }
                   }
               }
           }
       }
    }

    @Throws (ISWGeneralException::class)
    override suspend fun getISWToken(tokenRequestModelKozen: TokenRequestModelKozen) {
        var response = kimonoInterfaceKozen.getISWToken(tokenRequestModelKozen).run()
        if (response.isSuccessful) {
            if (response.body() != null) {
                var tokenData = response.body()
                tokenData?.token.let {
                    if (!it.isNullOrEmpty()) {
                        println("token gotten: => ${it}")
                        Prefs.putString(TOKEN, it)
                    }
                }
            }
        } else {
         throw ISWGeneralException()
        }
    }


    override suspend fun readTerminalInfo(): TerminalInfo {
        val dataString = Prefs.getString(TERMINAL_INFO_KEY)
        return Gson().fromJson(dataString, TerminalInfo::class.java) ?: TerminalInfo()
    }

    override suspend fun eraseKey(): Int {
        return POIHsmManage.getDefault().PedErase()
    }

    override suspend fun saveTerminalInfo(data: TerminalInfo) {
        val dataString = Gson().toJson(data)
        Prefs.putString(TERMINAL_INFO_KEY, dataString)
    }

}