package day5

import java.io.File

data class Instructions(val howMany: Int, val from: Int, val to: Int)
data class FileInfo(val instructions: List<Instructions>, val verticalCrates: ArrayList<ArrayList<String>>)

class DayFive {
    private fun parseFile(filename: String): FileInfo {
        val arrangement = arrayListOf<String>()
        val instructions = arrayListOf<Instructions>()
        File(filename)
            .useLines {
                var loadingArrangement = true
                it.toList().forEach { row ->
                    if (loadingArrangement){
                        if (row.isEmpty()) {
                            loadingArrangement = false
                        } else {
                            arrangement.add(row)
                        }
                    } else {
                        instructions.add(
                            convertToInstructions(row)
                        )
                    }
                }
            }

        return FileInfo(
            instructions = instructions,
            verticalCrates = convertToVerticalCrates(arrangementTexts = arrangement)
        )
    }

    private fun convertToInstructions(row: String): Instructions {
        val textInstructions = row.replace("move ", "")
            .replace(" from ", ",")
            .replace(" to ", ",")
            .splitToSequence(",").toList()

        return Instructions(
            howMany = textInstructions[0].toInt(),
            from = textInstructions[1].toInt(),
            to = textInstructions[2].toInt()
        )
    }

    private fun convertToVerticalCrates(arrangementTexts: List<String>): ArrayList<ArrayList<String>> {
        val largestNumber = arrangementTexts.last().split(" ").last().toInt()

        val horizontalCrates = arrangementTexts.map {
            val removedBraces = it.replace("] [", ",")
                .replace("[", ",")
                .replace("]", ",")

            if (removedBraces[0] == ',') {
                removedBraces.removeRange(0, 0)
            }

            val withoutBlanks = arrayListOf<String>()
            removedBraces.split(",").map { potentiallyBlankSection ->
                if (potentiallyBlankSection != "") {
                    withoutBlanks.add(potentiallyBlankSection)
                }
            }

            while (withoutBlanks.size < largestNumber) {
                withoutBlanks.add("    ")
            }

            withoutBlanks
        }
        (horizontalCrates as ArrayList).remove(horizontalCrates.last())

        val verticalCrates = arrayListOf<ArrayList<String>>()
        for (i in 0 until largestNumber) {
            val verticalCrate = arrayListOf<String>()
            horizontalCrates.forEach {
                verticalCrate.add(it[i])
            }
            verticalCrate.removeIf { it == "?" }
            verticalCrates.add(verticalCrate)
        }

        return verticalCrates
    }

    private fun returnTopCrates9000(fileInfo: FileInfo, is9001: Boolean = false): String {
        return moveCrates(fileInfo, is9001).map {
            it[0]
        }.toString()
    }

    private fun moveCrates(fileInfo: FileInfo, is9001: Boolean = false): ArrayList<ArrayList<String>> {
        fileInfo.instructions.forEach { instruction ->
            val moved: ArrayList<String> = arrayListOf()
            for (i in 0 until instruction.howMany) {
                moved.add(
                    fileInfo.verticalCrates[instruction.from - 1]
                        .removeFirst()
                )
            }

            if (is9001) {
                moved.reverse()
            }

            moved.forEach { movedCrate ->
                fileInfo.verticalCrates[instruction.to - 1].add(0, movedCrate)
            }
        }

//        GNFBSBJLH
        return fileInfo.verticalCrates
    }

    fun findTopCrate(filename: String, is9001: Boolean = false): String {
        return returnTopCrates9000(parseFile(filename), is9001 = is9001)
            .replace("[", "")
            .replace("]", "")
            .replace(", ", "")
    }
}

fun main(args: Array<String>) {
    // Top of each stack Crane9000: JRVNHHCSJ
    println("Top of each stack Crane9000: ${DayFive().findTopCrate("advent_of_code_2022/src/main/kotlin/day5/input.txt")}")
    // Top of each stack Crane9001: GNFBSBJLH
    println("Top of each stack Crane9001: ${DayFive().findTopCrate("advent_of_code_2022/src/main/kotlin/day5/input.txt", is9001 = true)}")
}