package com.lovisgod.kozenlib.core.utilities

enum class Model {
    L200, P10, P13, P3, N4, UNKNOWN
}

fun getDeviceModel(): Model {
    val name = android.os.Build.MODEL
    return when {
        name.contains("L200", ignoreCase = true) -> Model.L200
        name.contains("P10", ignoreCase = true) -> Model.P10
        name.contains("P13", ignoreCase = true) -> Model.P13
        name.contains("P3", ignoreCase = true) -> Model.P3
        name.contains("N4", ignoreCase = true) -> Model.N4
        else -> Model.UNKNOWN
    }
}
