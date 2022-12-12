package day11

import java.io.File
import java.lang.Exception

enum class OperationType {
    ADDITION,
    MULTIPLICATION,
    NULL
}
data class Test(
    val test: (Int) -> Boolean = { _ -> false},
    var isTrueMonkeyId: Int = -1,
    var isFalseMonkeyId: Int = -1,
)
data class Operation(
    val second: Int? = -1,
    val type: OperationType = OperationType.NULL
)
data class Item(
    val id: Int,
    var worryLevel: Int = id
)
data class Monkey(
    val id: Int,
    val items: ArrayList<Item> = arrayListOf(),
    var operation: Operation = Operation(),
    var test: Test = Test(),
    var inspectedCount: Int = 0
)

class DayEleven {
    private fun parseFile(filename: String): HashMap<Int, Monkey> {
        val monkeyMap = hashMapOf<Int, Monkey>()
        var monkeyId = -1
        File(filename)
            .useLines {
                it.toList().forEach { text ->
                    if (text.contains("Monkey")) {
                        monkeyId = text.split(" ", ":")[1].toInt()
                        monkeyMap.put(
                            monkeyId,
                            Monkey(id = monkeyId)
                        )
                    }
                    if (text.contains("Starting items:")) {
                        text.split("Starting items: ")[1]
                            .split(", ")
                            .map { itemString -> itemString.toInt() }
                            .forEach { itemInt ->
                                monkeyMap[monkeyId]?.items?.add(Item(id = itemInt))
                            }
                    }
                    if (text.contains("Operation: ")) {
                        val operationVals = text
                            .split("Operation: new = ")[1]
                            .split(" ")

                        monkeyMap[monkeyId]?.operation = Operation(
                            type = if (operationVals[1] == "*") OperationType.MULTIPLICATION else OperationType.ADDITION,
                            second = operationVals[2].toIntOrNull(),
                        )
                    }
                    if (text.contains("Test:")) {
                        monkeyMap[monkeyId]?.test = Test(
                            test = {itemVal: Int -> itemVal % text.split("Test: divisible by ")[1].toInt() == 0 }
                        )
                    }
                    if (text.contains("If true:")) {
                        monkeyMap[monkeyId]?.test?.isTrueMonkeyId = text.split("If true: throw to monkey ")[1].toInt()
                    }
                    if (text.contains("If false:")) {
                        monkeyMap[monkeyId]?.test?.isFalseMonkeyId = text.split("If false: throw to monkey ")[1].toInt()
                    }

                }
            }

        return monkeyMap
    }

    private fun performBusiness(map: HashMap<Int, Monkey>): Int {
        var tempMap = map
        var round: Int = 0
        while (round < 20) {
            tempMap = performRound(tempMap)
            round += 1
        }
        val list = tempMap.toList().sortedBy { pair ->
            pair.second.inspectedCount
        }.map { it.second }.reversed()

        return (list[0].inspectedCount * list[1].inspectedCount)
    }

    private fun performRound(map: HashMap<Int, Monkey>): HashMap<Int, Monkey> {
        val monkeysPerRound = map.keys.size
        for (i in 0 until monkeysPerRound) {
            val monkey = map[i]
            if (monkey != null) {
                val itemsToRemove = arrayListOf<Item>()
                for (item in monkey.items) {
                    item.worryLevel = calculateWorryLevel(monkey, item)
                    map[monkey.id]?.inspectedCount = map[monkey.id]?.inspectedCount?.plus(1)!!
                    map[throwToWhich(item, monkey.test)]?.items?.add(item)
                    itemsToRemove.add(item)
                }
                for (item in itemsToRemove) {
                    map[monkey.id]?.items?.remove(item)
                }
            }
        }

        return map
    }

    private fun throwToWhich(item: Item, test: Test): Int {
        val whichMonkey = test.test(item.worryLevel)
        return if (whichMonkey) test.isTrueMonkeyId else test.isFalseMonkeyId
    }

    private fun calculateWorryLevel(monkey: Monkey, item: Item): Int {
        val init = item.worryLevel
        val second: Int = monkey.operation.second ?: init
        return ( when(monkey.operation.type) {
            OperationType.ADDITION -> {
                init + second
            }
            OperationType.MULTIPLICATION -> {
                init * second
            }
            else -> {
                throw Exception("Something bad happened")
            }
        }) / 3
    }

    fun levelOfMonkeyBusiness(filename: String, isPartTwo: Boolean = false): Int {
        return performBusiness(parseFile(filename = filename))
    }
}

fun main(args: Array<String>) {
    val filename = "advent_of_code_2022/src/main/kotlin/day11/input.txt"
    println("Level of monkey business: ${ DayEleven().levelOfMonkeyBusiness(filename) }")
}