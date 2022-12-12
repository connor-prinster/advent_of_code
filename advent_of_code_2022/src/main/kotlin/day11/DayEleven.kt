package day11

import java.io.File
import java.lang.Exception
import java.math.BigInteger

enum class OperationType {
    ADDITION,
    MULTIPLICATION,
    NULL
}
data class Test(
    val test: (BigInteger) -> Boolean = { _ -> false},
    var isTrueMonkeyId: Int = -1,
    var isFalseMonkeyId: Int = -1,
)
data class Operation(
    val second: BigInteger? = BigInteger.valueOf(-1),
    val type: OperationType = OperationType.NULL
)
data class Item(
    val id: Int,
    var worryLevel: BigInteger = BigInteger.valueOf(id.toLong())
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

                        val potentialSecond = operationVals[2].toIntOrNull()
                        monkeyMap[monkeyId]?.operation = Operation(
                            type = if (operationVals[1] == "*") OperationType.MULTIPLICATION else OperationType.ADDITION,
                            second = if (potentialSecond == null) null else BigInteger.valueOf(potentialSecond.toLong()),
                        )
                    }
                    if (text.contains("Test:")) {
                        monkeyMap[monkeyId]?.test = Test(
                            test = {itemVal: BigInteger ->
                                val divisibleBy = text.split("Test: divisible by ")[1].toLong()

                                itemVal.rem(BigInteger.valueOf(divisibleBy)) == BigInteger.valueOf(0)
                            }
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

    private fun performBusiness(map: HashMap<Int, Monkey>, isPartTwo: Boolean): BigInteger {
        var tempMap = map
        var round = 0
        val howManyRounds = if (isPartTwo) 10000 else 20
        while (round < howManyRounds) {
            tempMap = performRound(tempMap, isPartTwo)
            round += 1
//            if (round == 1 || round == 20 || round % 1000 == 0) printMonkeyBusiness(tempMap, round)
        }
        val list = tempMap.toList().sortedBy { pair ->
            pair.second.inspectedCount
        }.map { it.second }.reversed()

        return BigInteger.valueOf(list[0].inspectedCount.toLong()).times(BigInteger.valueOf(list[1].inspectedCount.toLong()))
    }

    private fun performRound(map: HashMap<Int, Monkey>, isPartTwo: Boolean): HashMap<Int, Monkey> {
        val monkeysPerRound = map.keys.size
        for (i in 0 until monkeysPerRound) {
            val monkey = map[i]
            if (monkey != null) {
                val itemsToRemove = arrayListOf<Item>()
                for (item in monkey.items) {
                    item.worryLevel = calculateWorryLevel(monkey, item, isPartTwo)
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

    private fun calculateWorryLevel(monkey: Monkey, item: Item, isPartTwo: Boolean): BigInteger {
        val init = BigInteger.valueOf(item.worryLevel.toLong())
        val second: BigInteger = monkey.operation.second ?: init
        return ( when(monkey.operation.type) {
            OperationType.ADDITION -> {
                init.add(second)
            }
            OperationType.MULTIPLICATION -> {
                init.times(second)
            }
            else -> {
                throw Exception("Something bad happened")
            }
        }).div(BigInteger.valueOf(if (isPartTwo) 1L else 3L))
    }

    private fun printMonkeyBusiness(map: HashMap<Int, Monkey>, round: Int) {
        println("=== After round $round ===")
        map.keys.toList().forEach {
            println(
                "Monkey $it inspected items ${map[it]!!.inspectedCount} times"
            )
        }
        println()
    }

    fun levelOfMonkeyBusiness(filename: String, isPartTwo: Boolean = false): BigInteger {
        return performBusiness(parseFile(filename = filename), isPartTwo)
    }
}

fun main(args: Array<String>) {
    val filename = "advent_of_code_2022/src/main/kotlin/day11/test_input.txt"
    println("Level of monkey business 20 rounds: ${ DayEleven().levelOfMonkeyBusiness(filename) }\n")
    println("Level of monkey business 10000 rounds: ${ DayEleven().levelOfMonkeyBusiness(filename, isPartTwo = true) }")
}