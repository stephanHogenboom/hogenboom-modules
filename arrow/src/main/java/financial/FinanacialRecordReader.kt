package financial

import arrow.core.Either
import financial.headers.accountHeader
import financial.headers.amountheader
import financial.headers.codeHeader
import financial.headers.commentHeader
import financial.headers.counterAccountHeader
import financial.headers.dateHeader
import financial.headers.descriptionHeader
import financial.headers.mutationKindHeader
import financial.headers.signHeader
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.IOException
import java.math.BigDecimal
import java.nio.charset.Charset
import java.nio.file.Path
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FinancialRecordReader {

    fun read(path: Path): List<Either<Exception, FinancialRecord>> {
        val format = createFormat()
        val parser: CSVParser = CSVParser.parse(path, Charset.defaultCharset(), format);
           return parser.records
                .map {
                    Either.catch {
                    val isPositive: Boolean = it.get(signHeader).equals("Bij")
                    val amount =
                        if (isPositive) BigDecimal(it.get(amountheader).replace(",", ".")) else BigDecimal(
                            it.get(
                                amountheader
                            ).replace(",", ".")
                        ).multiply(BigDecimal("-1"))

                    FinancialRecord(
                        LocalDate.parse(it.get(dateHeader), DateTimeFormatter.ofPattern("yyyyMMdd")),
                        it.get(descriptionHeader),
                        Account(it.get(accountHeader)),
                        Account(it.get(counterAccountHeader)),
                        amount,
                        it.get(commentHeader)
                    )
                }.mapLeft { IOException(it.message) }
            }.toList()
    }

    private fun createFormat(): CSVFormat  =
        CSVFormat.DEFAULT.builder()
            .setSkipHeaderRecord(true)
            .setDelimiter(",")
            .setHeader(
                dateHeader,
                descriptionHeader,
                accountHeader,
                counterAccountHeader,
                codeHeader,
                signHeader,
                amountheader,
                mutationKindHeader,
                commentHeader
            ).build()

}

object headers {
    const val dateHeader: String = "Datum"
    const val descriptionHeader: String = "Naam / Omschrijving"
    const val accountHeader: String = "Rekening"
    const val counterAccountHeader: String = "Tegenrekening"
    const val codeHeader: String = "code"
    const val signHeader: String =  "Af Bij"
    const val amountheader: String =  "Bedrag (EUR)"
    const val mutationKindHeader: String = "Mutatiesoort"
    const val commentHeader: String =   "Mededelingen"
}

