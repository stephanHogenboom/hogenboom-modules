package financial.mortgage

import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class MortgageStatement(val id: UUID, val mortgageId: UUID, val date: LocalDate, val debtAmount: BigDecimal) {
}