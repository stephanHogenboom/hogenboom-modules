package financial.mortgage

import com.fasterxml.jackson.databind.ObjectMapper
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MortgageHandler {
    fun createMortgageAndEditOnCommandLine() {
        println("Creating new mortgage")
        println("What is the street name?")


        val scanner: Scanner = Scanner(System.`in`)
        val streetName = scanner.nextLine();

        println("what is the number of the address")

        val number = scanner.nextLine().toInt()

        println(" WHat is the postal code of the address?")
        val postalCode = scanner.nextLine();

        val address: Address = Address(streetName, number, postalCode);
        val house: House = House(address)

        println("What date did the mortgage start? yyyy-mm-dd")

        val startDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        val mortgage = Mortgage(UUID.randomUUID(), house, startDate, null, ArrayList())

        do {
            println("add mortage statement? (y/n)")
            var answer = scanner.nextLine();

            if (!answer.trim().lowercase().equals("y")) break


            println("What date is this statement? yyyy-mm-dd")

            val dateOfStatement = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            println("What was the remaining debt of this statement?")

            var amount = scanner.nextLine().toBigDecimal()


            val mortgageStatement: MortgageStatement = MortgageStatement(UUID.randomUUID(), mortgage.id, dateOfStatement, amount)
            mortgage.statements.add(mortgageStatement)

        } while (true)
        val objectMapper = ObjectMapper()
        Files.write(Paths.get("statements.json"), objectMapper.writeValueAsString(mortgage).toByteArray())
    }
}