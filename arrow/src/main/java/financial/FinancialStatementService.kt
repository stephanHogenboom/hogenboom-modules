package financial

import arrow.core.*
import java.nio.file.Path

class FinancialStatementService(private val reader: FinancialRecordReader) {

    fun printStatements(path: Path) {

        val list = reader.read(path)

        val recordList = list
            .mapNotNull { when (it) {
                is Either.Right -> it.value
                is Either.Left -> null
            }}
            .toList()

        val errors = list
            .mapNotNull { if (it.isRight()) null else it.left() }
            .toList()


        errors.forEach { println(it) }
      //  printSortedListOfMonthlyMutations(recordList.filter { it.description.contains("ACTION", true) })
        printSortedListOfMonthlyMutations(recordList)
    }

    private fun handleEither(either: Either<String, FinancialRecord>): FinancialRecord? {
        if (either.isLeft()) {
            println(either.handleError { println(it) })
        }
        return either.getOrElse { null }
    }


    private fun printSortedListOfMonthlyMutations(list: List<FinancialRecord>) {
        sortByYearAndMonth(list)
            .toSortedMap(compareBy<DateKey> { it.year }.thenBy { it.month })
            .forEach { entry -> run { println("${entry.key} : ${entry.value.map { it.amount }.sumOf { it }}") } }
    }

    private fun sortByYearAndMonth(list: List<FinancialRecord>) = list.groupBy { it.toKey() }
}