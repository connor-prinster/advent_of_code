package daythree

import java.io.File

data class RuckSack(
    val items: String,
    val firstPartition: List<String> =
        items.substring(0, items.length / 2).map { it.toString() }.toList(),
    val secondPartition: List<String> =
        items.substring(items.length / 2, items.length).map { it.toString() }.toList(),
    private val firstSize: Int = firstPartition.size,
    private val secondSize: Int = secondPartition.size
) {
    init {
        if (firstSize != secondSize) {
            throw Exception("Not equal partitions!")
        }
    }
}

class DayThreePartOne {
    fun retrieveDataFromFile(): List<RuckSack> {
        return File("src/main/kotlin/daythree/input.txt")
            .useLines { it ->
                it.toList().map {
                    RuckSack(items = it)
                }
            }
    }
}

fun main(args: Array<String>) {
    val thing = DayThreePartOne().retrieveDataFromFile()
    print(thing)
}