package day3

import java.io.File

data class RuckSack(
    val items: List<Char>,
    val firstPartition: List<Char> =
        items.subList(0, items.size / 2).toList(),
    val secondPartition: List<Char> =
        items.subList(items.size / 2, items.size).toList()
) {
    init {
        if (firstPartition.size != secondPartition.size) {
            throw Exception("Not equal partitions!")
        }
    }
}

data class ElfSquad(val elves: List<RuckSack>)

class DayThreePartOne {
    fun findDuplicateItemPriorities(): Int {
        var priority: Int = 0
        val ruckSacks: List<RuckSack> = getRucksacks("src/main/kotlin/day3/input.txt")

        ruckSacks.forEachIndexed { whichRucksack, ruckSack ->
            val doubleLetters = findDuplicateItems(
                ruckSack.firstPartition, ruckSack.secondPartition
            )
            doubleLetters.forEach {
                priority += convertToPriority(it)
            }
        }

        return priority
    }
}
class DayThreePartTwo {
    fun retrieveBadgePriorities(): Int {
        var priority = 0
        val ruckSacks: List<RuckSack> = getRucksacks("src/main/kotlin/day3/input.txt")
        val squads = makeSquads(ruckSacks)
        squads.forEach {
            priority += convertToPriority(
                findDuplicateItems(
                    firstPartition = findDuplicateItems(
                        firstPartition = it.elves[0].items,
                        secondPartition = it.elves[1].items
                    ),
                    secondPartition = it.elves[2].items
            ).first())
        }

        return priority
    }
    private fun makeSquads(elves: List<RuckSack>): List<ElfSquad> {
        val squads = mutableListOf<ElfSquad>()
        for (i in elves.indices step 3) {
            squads.add(ElfSquad(listOf(elves[i], elves[i + 1], elves[i + 2])))
        }
        return squads
    }
}

private fun findDuplicateItems(firstPartition: List<Char>, secondPartition: List<Char>): List<Char> {
    val doubleLetters = mutableListOf<Char>()

    firstPartition.forEach { firstPartitionItem ->
        secondPartition.forEach {secondPartitionItem ->
            if (firstPartitionItem == secondPartitionItem && !(doubleLetters.contains(firstPartitionItem))) {
                doubleLetters.add(firstPartitionItem)
            }
        }
    }

    return doubleLetters
}

fun convertToPriority(char: Char): Int {
    return if (char.isLowerCase()) {
        (char.code - 96)
    } else {
        (char.code - 64) + 26
    }
}
fun getRucksacks(filename: String): List<RuckSack> {
    return File(filename)
        .useLines { it ->
            it.toList().map {
                RuckSack(items = it.toCharArray().toList())
            }
        }
}

fun main(args: Array<String>) {
    println("Sum of priorities of item: ${DayThreePartOne().findDuplicateItemPriorities()}")
    println("Sum of priorities of badges: ${DayThreePartTwo().retrieveBadgePriorities()}")
}