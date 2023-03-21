package movefiles

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import arrow.core.computations.either
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

class MoveFiles {


    fun moveMp4s(from: Path, to: Path): Either<IOException, Unit> = either.eager {
        createDir(to).bind()
        moveVideos(from, to)
    }

     fun renameFiles(path: Path): Either<IOException, Unit> {
        return Either.catch {
            File(path.toString()).walk().forEach {
                val validSibling = it.toPath().resolveSibling(Paths.get(it.name.replace("IMG-", "")))
                Files.move(it.toPath(), validSibling)
                print("moved file {${validSibling.fileName}}")
            }
        }.mapLeft { IOException(it.message) }
    }





    /**
     * for files in the format YYMMDD_hhmmss
     */
    private fun moveVideos(from: Path, to: Path): Either<IOException, Unit> {
       return either.eager {
            Either.catch {
                File(from.toString()).walk().forEach {
                    val name = it.name
                    if (name.matches(Regex("[0-9]{8}.*")) && name.endsWith(".mp4")) {
                        println(name)
                        val year = name.substring(0, 4)
                        val month = name.substring(4, 6)
                        val day = name.substring(6, 8)
                        val videoDir = to.resolve(Paths.get("videos"))
                        createDir(videoDir)

                        val time = name.substring(name.indexOf("_") + 1)
                        val newName = "${year}_${month}_${day}_${time.substring(0, 2)}${time.substring(2, 4)}${time.substring(4, 6)}.mp4"
                        val newNameTo = videoDir.resolve(Paths.get(newName))

                        copyFiles(newNameTo, it).bind()
                    }
                }
            }.mapLeft { IOException(it.message) }

        }

    }

    fun sortJpegsByYearAndMonth(from: Path, to: Path) {
        if (!Files.exists(to)) {
            Files.createDirectories(to);
        }
        File(from.toString()).walk().forEach {
            val name = it.name
            if (name.matches(Regex("[0-9]{8}.*")) && name.endsWith(".jpg")) {
                val year = name.substring(0, 4)
                val month = name.substring(4, 6)
                val day = name.substring(6, 8)

                val yearTo = to.resolve(Paths.get(year));
                val monthTo = yearTo.resolve(Paths.get(month))

                createDir(yearTo)
                createDir(monthTo)

                val time = name.substring(name.indexOf("_") + 1)
                val newName = "${day}_${time.substring(0, 2)}${time.substring(2, 4)}${time.substring(4, 6)}.jpg"
                val newNameTo = monthTo.resolve(Paths.get(newName))

                copyFiles(newNameTo, it)
            }
        }
    }

     fun sortJpegsByYearAndMonthWa(from: Path, to: Path) {
        if (!Files.exists(to)) {
            Files.createDirectories(to);
        }
        File(from.toString()).walk().forEach {
            val name = it.name
            if (name.matches(Regex("[0-9]{8}.*")) &&  name.contains("WA") && name.endsWith(".jpg")) {
                val year = name.substring(0, 4)
                val month = name.substring(4, 6)
                val day = name.substring(6, 8)

                val yearTo = to.resolve(Paths.get(year));
                val monthTo = yearTo.resolve(Paths.get(month))

                createDir(yearTo)
                createDir(monthTo)

                val secondPart = name.substring(name.indexOf("-") + 1 )
                val newName = "${day}_${secondPart}"
                val newNameTo = monthTo.resolve(Paths.get(newName))

                copyFiles(newNameTo, it)
            }
        }
    }

    private fun copyFiles(newNameTo: Path, it: File): Either<IOException, Unit> {
        return Either.catch {
            if (!Files.exists(newNameTo)) {
                Files.copy(it.toPath(), Files.newOutputStream(newNameTo, StandardOpenOption.CREATE_NEW))
                println("created file $newNameTo")
            }
        }.mapLeft { IOException(it.message) }
    }

    private fun createDir(yearTo: Path): Either<IOException, Unit> {
        return Either.catch {
            if (!Files.exists(yearTo)) {
                Files.createDirectories(yearTo)
            }
        }.mapLeft { IOException(it.message) }
    }
}