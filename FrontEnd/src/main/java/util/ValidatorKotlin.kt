package util

import kotlin.text.matches
import kotlin.text.toRegex

class ValidatorKotlin {

    fun isNumeric(s: String?): Boolean {
        return s != null && s.matches("-?\\d+(\\.\\d+)?".toRegex())
    }
}
