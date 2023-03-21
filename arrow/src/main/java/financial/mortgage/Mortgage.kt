package financial.mortgage

import java.time.LocalDate
import java.util.*

data class Mortgage(val id: UUID, val house: House, val startDate: LocalDate, val endDate: LocalDate?, val statements: ArrayList<MortgageStatement>)
