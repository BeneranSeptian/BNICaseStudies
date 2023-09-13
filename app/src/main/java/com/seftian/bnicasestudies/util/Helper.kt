package com.seftian.bnicasestudies.util

import com.seftian.bnicasestudies.core.domain.model.QRData
import java.util.Locale

object Helper {
    fun parseQRString(qrString: String): QRData? {
        val parts = qrString.split(".")
        return if (parts.size == 4) {
            QRData(parts[0], parts[1], parts[2], parts[3])
        } else {
            null
        }
    }

    fun convertLongToCurrencyString(value: Long): String {
        return String.format(Locale("id", "ID"), "Rp%,d", value)
    }

    fun isQrStringValid(input: String): Boolean {
        val pattern = """^([A-Z]+)\.([A-Z]{2})(\d+)\.(.+)\.(\d+)$""".toRegex()
        val matchResult = pattern.matchEntire(input)

        return matchResult != null
    }

}