package com.lovisgod.kozenlib.core.utilities

import com.lovisgod.kozenlib.core.utilities.BerTlv
import com.lovisgod.kozenlib.core.utilities.BerTag
import java.util.ArrayList

class BerTlvs constructor(val list: List<BerTlv>) {
    fun find(aTag: BerTag?): BerTlv? {
        for (tlv in list) {
            val found = tlv.find(aTag!!)
            if (found != null) {
                return found
            }
        }
        return null
    }

    fun findAll(aTag: BerTag?): List<BerTlv> {
        val list: MutableList<BerTlv> = ArrayList()
        for (tlv in this.list) {
            list.addAll(tlv.findAll(aTag!!)!!)
        }
        return list
    }
}