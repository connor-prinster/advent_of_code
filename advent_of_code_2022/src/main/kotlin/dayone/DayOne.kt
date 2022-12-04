package dayone

import java.io.File

class DayOnePartOne {
    fun retrieveDataFromFile(filename: String = "src/main/kotlin/dayone/input.txt"): List<Int> {
        return File(filename)
            .useLines { it ->
                it.toList().joinToString().split(", ,").map {elfList ->
                    var calories = 0
                    elfList.splitToSequence(",").forEach{ item ->
                        calories += item.filter { !it.isWhitespace() }.toInt()
                    }
                    calories
                }
            }
    }
}

class DayOnePartTwo {
    fun findTopThreeElvesCalories(): Int {
        val listOfCalories = DayOnePartOne().retrieveDataFromFile()
            .sortedBy {
                it
            }.reversed()

        return listOfCalories[0] + listOfCalories[1] + listOfCalories[2]
    }
}

fun main(args: Array<String>) {
    println("Max calories being carried: ${DayOnePartOne().retrieveDataFromFile().maxOrNull()}")
    println("Top three calories: ${DayOnePartTwo().findTopThreeElvesCalories()}")
}