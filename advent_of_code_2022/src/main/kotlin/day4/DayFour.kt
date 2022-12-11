package day4

import java.io.File

data class AssignmentPairs(val pairOne: FirstLast, val pairTwo: FirstLast)
data class FirstLast(val start: Int, val finish: Int) {
    fun size(): Int = finish - start
}

class DayFourPartOne {
    private fun getAssignmentPairs(filename: String): List<AssignmentPairs> {
        return File(filename)
            .useLines {
                it.toList().map {pairs ->
                    val assignmentPairs = pairs.split(",")
                    val firstLasts = assignmentPairs.map { firstLastString ->
                        val firstLastInts = firstLastString.split("-")
                        FirstLast(firstLastInts[0].toInt(), firstLastInts[1].toInt())
                    }

                    AssignmentPairs(firstLasts[0], firstLasts[1])
                }
            }
    }

    private fun countFullyContainedPairs(pairs: List<AssignmentPairs>): Int {
        var count = 0

        pairs.forEach {pair ->
            count += if (
                doesPairContainTheOther(
                    pairOne = pair.pairOne,
                    pairTwo = pair.pairTwo
                ) || doesPairContainTheOther(
                    pairOne = pair.pairTwo,
                    pairTwo = pair.pairOne
                )) 1 else 0
        }

        return count
    }

    private fun countOverlappingPairs(pairs: List<AssignmentPairs>): Int {
        var count = 0


        pairs.forEach {pair ->
            val arrayOne = makeArrayFromPair(pair.pairOne)
            val arrayTwo = makeArrayFromPair(pair.pairTwo)

            count += if (
                doesPairOverlapTheOther(
                    arrayOne, arrayTwo
                ) || doesPairOverlapTheOther(
                    arrayTwo, arrayOne
                )) 1 else 0
        }

        return count
    }

    private fun makeArrayFromPair(firstLast: FirstLast): Array<Int> {
        val array = mutableListOf<Int>()
        for (i in firstLast.start.. firstLast.finish) {
            array.add(i)
        }

        return array.toTypedArray()
    }

    private fun doesPairContainTheOther(pairOne: FirstLast, pairTwo: FirstLast): Boolean {
        return pairOne.start <= pairTwo.start &&
                        pairOne.finish >= pairTwo.finish
    }

    private fun doesPairOverlapTheOther(pairOne: Array<Int>, pairTwo: Array<Int>): Boolean {
        return pairOne.intersect(pairTwo.toList().toSet()).isNotEmpty()
    }

    fun countPairsContainingTheOther(filename: String): Int {
        return countFullyContainedPairs(getAssignmentPairs(filename))
    }
    fun countPairsOverlappingTheOther(filename: String): Int {
        return countOverlappingPairs(getAssignmentPairs(filename))
    }
}

fun main(args: Array<String>) {
    println("Assignment pairs containing the other: ${DayFourPartOne().countPairsContainingTheOther("advent_of_code_2022/src/main/kotlin/day4/input.txt")}")
    println("Assignment pairs overlapping at all: ${DayFourPartOne().countPairsOverlappingTheOther("advent_of_code_2022/src/main/kotlin/day4/input.txt")}")
}