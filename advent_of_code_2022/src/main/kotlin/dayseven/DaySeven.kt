package dayseven

import java.io.File

class DaySeven {
    private fun parseFile(filename: String): ArrayList<String> {
        File(filename)
            .useLines {
                it.toList().forEach { packet ->
                }
            }

        return arrayListOf()
    }

    fun sumOfTotalSizeOfDirectories(filename: String) {
        val parsed = parseFile(filename)
    }
}


fun main(args: Array<String>) {
    val filename = "advent_of_code_2022/src/main/kotlin/dayseven/input.txt"
    println("How many characters must be parsed for marker: ${DaySeven().sumOfTotalSizeOfDirectories(filename)}")
}