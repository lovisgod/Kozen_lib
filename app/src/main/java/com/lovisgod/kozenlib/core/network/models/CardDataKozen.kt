package com.lovisgod.kozenlib.core.network.models

import com.lovisgod.kozenlib.core.data.utilsData.RequestIccData
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "CardDataKozen", strict = false)
class CardDataKozen {
    @field:Element(name = "cardSequenceNumber", required = false)
    var cardSequenceNumber: String = ""

    @field:Element(name = "emvData", required = false)
    var emvData: RequestIccData? = null

//    @field:Element(name = "mifareData", required = false)
//    var mifareData: MiFareData? = null

    @field:Element(name = "track2Kozen", required = false)
    var track2Kozen: Track2Kozen? = null

    @field:Element(name = "wasFallback", required = false)
    var wasFallback: Boolean = false

    companion object {
        fun create(iccDataInfo: RequestIccData ) = CardDataKozen().apply {
            val track2data =iccDataInfo.TRACK_2_DATA
            println("track2Kozen data => ${track2data}")
            // extract pan and expiry
            val strTrack2 = track2data.split("F")[0]
            var panX = strTrack2.split("D")[0]
            val expiry = strTrack2.split("D")[1].substring(0, 4)
            val src = strTrack2.split("D")[1].substring(4, 7)

            cardSequenceNumber = "0${iccDataInfo.APP_PAN_SEQUENCE_NUMBER}"
            emvData = iccDataInfo
            track2Kozen = Track2Kozen().apply {
                pan = panX
                expiryMonth = expiry.takeLast(2)
                expiryYear = expiry.take(2)
                track2 = let {
                    val neededLength = strTrack2.length - 2
                    val isVisa = strTrack2.startsWith('4')
                    val hasCharacter = strTrack2.last().isLetter()

                    // remove character suffix for visa
                    if (isVisa && hasCharacter) strTrack2.substring(0..neededLength)
                    else strTrack2
                }
            }

        }
    }
}