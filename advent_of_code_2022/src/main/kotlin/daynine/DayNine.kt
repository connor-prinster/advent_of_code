package daynine

import java.io.File

data class Movement(val direction: String, val amount: Int)

class DayNine {
    private fun parseFile(filename: String): List<Movement> {
        return File(filename)
            .useLines {
                it.toList().map { cliText ->
                    print(cliText)
                    val split = cliText.split(" ")
                    Movement(split[0], split[1].toInt())
                }
            }
    }

    fun main(filename: String) {
        print(parseFile(filename))
    }
}

fun main(args: Array<String>) {
    val filename = "advent_of_code_2022/src/main/kotlin/daynine/test_input.txt"
    println("${DayNine().main(filename)}")
}