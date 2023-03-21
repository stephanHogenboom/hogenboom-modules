package financial

import java.math.BigDecimal
import java.time.LocalDate

data class FinancialRecord(val date: LocalDate, val description: String, val account: Account, val counterAccount: Account, val amount: BigDecimal, val comment: String) {
    fun toKey(): DateKey = DateKey(date.year, date.monthValue)
}


data class DateKey(val year: Int, val month: Int)

data class Account(val accountNumber: String)

enum class Mutation{
    Normal
}