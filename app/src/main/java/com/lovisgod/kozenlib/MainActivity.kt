package com.lovisgod.kozenlib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.lovisgod.kozenlib.core.data.dataInteractor.EMVEvents
import com.lovisgod.kozenlib.core.data.dataInteractor.PrinterEvent
import com.lovisgod.kozenlib.core.data.models.EmvCardType
import com.lovisgod.kozenlib.core.data.models.TerminalInfo
import com.lovisgod.kozenlib.core.utilities.PrinterUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity(), EMVEvents, PrinterEvent {
    lateinit var testBtn : AppCompatButton
    lateinit var applicationHandler: ApplicationHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testBtn = findViewById(R.id.test_btn)
        applicationHandler = ApplicationHandler()
        handleClicks()
    }

    private fun handleClicks() {
        testBtn.setOnClickListener {
            runBlocking {
                val terminalInfo = TerminalInfo(terminalCode = "2ISW0001", merchantId = "2ISW1234567TEST")
                applicationHandler.saveTerminalInfo(terminalInfo)
                applicationHandler.loadTerminal(terminalInfo)
                applicationHandler.loadAllConfig()

                delay(2000)

                applicationHandler.checkCard(
                    true, true, 400000L, 0L, 0,  this@MainActivity
                )

            }
        }
    }

    override fun onInsertCard() {
        println("insert card called")
    }

    override fun onRemoveCard(isContactlessTransLimit: Boolean, message: String) {
        println("remove card")
        println(message)
    }

    override fun onPinInput() {
        println("input pin called")
    }

    override fun onCardRead(pan: String, cardType: EmvCardType) {
        println("card read called ${cardType.name}")
    }

    override fun onCardDetected(contact: Boolean) {
        println("card is detected is contact ::${contact}")
    }

    override fun onEmvProcessing(message: String) {
        println("processing is called")
    }

    override fun onEmvProcessed(data: Any) {
        println("emv processed is called")
    }

    override fun onUserCanceled(message: String) {
        println(message)
    }

    override fun onTransactionCancelled(message: String) {
        println(message)
    }

    override fun onPrintSuccess(code: Int) {
        println("print status:::: $code")
    }

    override fun onPrintFail(code: Int) {
       println("print fail status:::: $code")
    }

    override fun onPrintStart() {
        println("print started")
    }
}