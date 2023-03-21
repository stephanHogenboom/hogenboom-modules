import financial.FinancialRecordReader
import financial.FinancialStatementService
import financial.mortgage.MortgageHandler
import movefiles.MoveFiles
import java.nio.file.Paths

class Main {
}


/**
 * why is the bind only available in a coroutine?
 */
fun main(args: Array<String>) {
//     MoveFiles().renameFiles(Paths.get("/home/stephan/saar/Camera"))
//     MoveFiles().moveMp4s(java.nio.file.Paths.get("/home/stephan/saar/Camera"), Paths.get("/home/stephan/saar/sorted"))
//}

    val handler = MortgageHandler()
    handler.createMortgageAndEditOnCommandLine()


}


private fun getMaybe(): Double? {
    val random = Math.random()
    if (random < 0.5)
        return null
    else return random;


}


private suspend fun moveImageAndVideos() {
    val moveService = MoveFiles()
    val from = Paths.get("/")
    val to = from.resolve(Paths.get("../sorted-fotos")).normalize()
    runCatching { moveService.moveMp4s(from, to) }
}

private fun runFInancialStatements() {
    val service = FinancialStatementService(FinancialRecordReader())
    service.printStatements(Paths.get("input.csv"))
}


