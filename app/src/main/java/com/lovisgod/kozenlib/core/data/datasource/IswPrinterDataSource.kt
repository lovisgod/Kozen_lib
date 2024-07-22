package com.lovisgod.kozenlib.core.data.datasource

import android.content.Context
import android.graphics.Bitmap
import com.lovisgod.kozenlib.core.data.dataInteractor.PrinterEvent
import com.lovisgod.kozenlib.core.data.models.printer.PrintObject

interface IswPrinterDataSource {

    suspend fun print(context: Context, printerEvent: PrinterEvent, bitmap: Bitmap)

    suspend fun print(context: Context, printerEvent: PrinterEvent, slip: List<PrintObject>)
}