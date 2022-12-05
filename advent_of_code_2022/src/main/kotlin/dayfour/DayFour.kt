package dayfour

import daythree.RuckSack
import java.io.File

data class AssignmentPairs(val pairOne: String, val pairTwo: String)

class DayFourPartOne {
    private fun getAssignmentPairs(filename: String): List<AssignmentPairs> {
        return File(filename)
            .useLines { it ->
                it.toList().map {
                    AssignmentPairs("", "")
                }
            }
    }

    fun countPairsContainingTheOther(filename: String = "src/main/kotlin/dayfour/input.txt"): Int {
        getAssignmentPairs(filename)
        return 0
    }
}

fun main(args: Array<String>) {
    println("Assignment pairs containing the other: ${DayFourPartOne().countPairsContainingTheOther("src/main/kotlin/dayfour/test_input.txt")}")
}