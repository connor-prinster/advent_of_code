package day14

import day12.HeightMap
import day12.HeightPair
import day12.MapPoint
import java.io.File
import java.util.StringJoiner

class DayFourteen {

    private fun parseFile(filename: String): String {

        File(filename)
            .useLines {
                it.toList().map { text ->
                    println()
                }
            }

        return ""
    }

    fun main(filename: String): String {
        return parseFile(filename)
    }
}

fun main() {
    val filename = "advent_of_code_2022/src/main/kotlin/day14/input.txt"
    println("Answer: ${DayFourteen().main(filename)}")
}