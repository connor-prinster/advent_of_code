package dayone

import java.io.File

class DayOne {
    fun retrieveDataFromFile(): List<Int> {
        return File("src/main/kotlin/dayone/input.txt")
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

fun main(args: Array<String>) {
    print("Max calories being carried: ${DayOne().retrieveDataFromFile().maxOrNull()}")
}