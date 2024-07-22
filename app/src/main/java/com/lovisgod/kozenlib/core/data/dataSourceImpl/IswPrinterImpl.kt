package com.lovisgod.kozenlib.core.data.dataSourceImpl

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.lovisgod.kozenlib.core.data.dataInteractor.PrinterEvent
import com.lovisgod.kozenlib.core.data.datasource.IswPrinterDataSource
import com.lovisgod.kozenlib.core.data.models.printer.PrintObject
import com.lovisgod.kozenlib.core.data.models.printer.PrintStringConfiguration
import com.pos.sdk.printer.POIPrinterManager
import com.pos.sdk.printer.models.BitmapPrintLine
import com.pos.sdk.printer.models.PrintLine
import com.pos.sdk.printer.models.TextPrintLine

class IswPrinterImpl: IswPrinterDataSource {
    // screen character length
    private val SCREEN_LARGE_LENGTH = 24
    private val SCREEN_NORMAL_LENGTH = 32

    lateinit var context: Context
    lateinit var printerManager: POIPrinterManager
    lateinit var printerEvent: PrinterEvent

    private val line: String = "-".repeat(SCREEN_NORMAL_LENGTH)

    override suspend fun print(context: Context,
                               printerEvent: PrinterEvent, bitmapx: Bitmap) {
        this.context = context
        this.printerManager  = POIPrinterManager(context)
        this.printerEvent = printerEvent

        printerManager.open()
        val state = printerManager.printerState
        Log.d("printer kozen", "printer state = $state")
        //printerManager.setPrintFont("/system/fonts/Android-1.ttf");
        //printerManager.setPrintFont("/system/fonts/Android-1.ttf");
        printerManager.setPrintGray(500)
        printerManager.setLineSpace(10)
        //printerManager.cleanCache();
        //printerManager.cleanCache();
//        val str1 = "This is an example of a receipt"
//        val p1: PrintLine = TextPrintLine(str1, PrintLine.CENTER)
//        printerManager.addPrintLine(p1)
//        val bitmap =
        printerManager.addPrintLine(BitmapPrintLine(bitmapx, PrintLine.CENTER))

        if (state == 4) {
            printerManager.close()
            this.printerEvent.onPrintFail(4)
            return
        }
        printerManager.beginPrint(listener)
    }

    override suspend fun print(
        context: Context,
        printerEvent: PrinterEvent,
        slip: List<PrintObject>
    ) {
        this.context = context
        this.printerManager  = POIPrinterManager(context)
        this.printerEvent = printerEvent

        printerManager.open()
        val state = printerManager.printerState

        printerManager.setPrintGray(500)
        printerManager.setLineSpace(0)

        for (item in slip) {
            addPrintLine(printerManager, item)
        }

        if (state == 4) {
            printerManager.close()
            this.printerEvent.onPrintFail(4)
            return
        }

        printerManager.beginPrint(listener)
    }

    private fun addPrintLine(printerManager: POIPrinterManager, printObject: PrintObject) {
        when(printObject) {
            is PrintObject.BitMap -> {
                printerManager.addPrintLine(BitmapPrintLine(printObject.bitmap, PrintLine.CENTER))
            }
            is PrintObject.Line -> {
                printerManager.setLineSpace(-15)
                printerManager.addPrintLine(TextPrintLine(line, PrintLine.CENTER, 43))
            }

            is PrintObject.Data -> {
                // get string print config
                val printConfig = printObject.config
                val isBold = printConfig.isBold || printConfig.isTitle

                val isCenter = when(printConfig.displayCenter) {
                    true -> PrintLine.CENTER
                    else -> PrintLine.LEFT
                }

                val fontType = when {
                    printConfig.isTitle -> FontType.LARGE
                    else -> FontType.MEDIUM
                }
                val fontSize = when(fontType) {
                    FontType.LARGE -> {
                        printerManager.setLineSpace(-5)
                        36
                    }
                    FontType.SMALL -> {
                        printerManager.setLineSpace(-8)
                        16
                    }
                    else -> {
                        printerManager.setLineSpace(-8)
                        24
                    }
                }

                printerManager.addPrintLine(TextPrintLine(printObject.value, isCenter, fontSize, isBold))
            }
        }
    }

    var listener: POIPrinterManager.IPrinterListener = object : POIPrinterManager.IPrinterListener {
        override fun onStart()  {
            this@IswPrinterImpl.printerEvent.onPrintStart()
        }
        override fun onFinish() {
            //printerManager.cleanCache();
            printerManager.close()
            this@IswPrinterImpl.printerEvent.onPrintSuccess(0)
        }

        override fun onError(code: Int, msg: String) {
            Log.e("POIPrinterManager", "code: " + code + "msg: " + msg)
            printerManager.close()
            this@IswPrinterImpl.printerEvent.onPrintFail(4)
        }
    }

    enum class FontType {
        LARGE,
        MEDIUM,
        SMALL
    }
}