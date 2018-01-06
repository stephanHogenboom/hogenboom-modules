package main.java.util

import kotlin.text.matches
import kotlin.text.toRegex

class ValidatorKotlin {

    fun isNumeric(s: String?): Boolean {
        return s != null && s.matches("-?\\d+(\\.\\d+)?".toRegex())
    }

    fun isNumeric(vararg strings: String): Boolean {
        for (s in strings) {
            if (!isNumeric(s)) {
                System.out.printf("%s is not numeric %s", s, "\n")
                return false
            }
        }
        return true
    }
}
