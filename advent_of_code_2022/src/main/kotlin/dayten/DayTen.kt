package dayten

import java.io.File

enum class CommandType {
    ADDX, NOOP
}

data class Command(val type: CommandType, val integer: Int?)


class DayTen {
    private fun parseFile(filename: String): List<Command> {
        return File(filename)
            .useLines {
                it.toList().map { commandText ->
                    val split = commandText.split(" ")
                    when(split.size) {
                        1 -> Command(type = CommandType.NOOP, integer = null)
                        else -> Command(type = CommandType.ADDX, integer = split[1].toInt())
                    }
                }
            }
    }

    private fun returnSignalStrengths(commands: List<Command>): List<Int> {
        var cycle = 0
        var register = 1
        var signalStrengths: ArrayList<Int> = arrayListOf()

        commands.forEach { command ->
            when(command.type) {
                CommandType.ADDX -> {
                    for (i in 0 until 2) {
                        cycle += 1
                        signalStrengths = addToListOfSignalStrengths(
                            signalStrengths = signalStrengths,
                            cycle = cycle,
                            registerValue = register
                        )
                    }
                    register += command.integer!!
                }
                else -> {
                    cycle += 1
                    signalStrengths = addToListOfSignalStrengths(
                        signalStrengths = signalStrengths,
                        cycle = cycle,
                        registerValue = register)

                }
            }
        }

        return signalStrengths
    }

    private fun addToListOfSignalStrengths(signalStrengths: ArrayList<Int>, cycle: Int, registerValue: Int): ArrayList<Int> {
        if (cycle == 20 || (cycle - 20) % 40 == 0) {
            val signalStrength = cycle * registerValue
            println("cycle: $cycle = $signalStrength")
            signalStrengths.add(signalStrength)
        }
        return signalStrengths
    }

    fun main(filename: String): Int {
        var sum = 0
        returnSignalStrengths(parseFile(filename)).forEach {
            sum += it
        }
        return sum
    }
}

fun main(args: Array<String>) {
    val filename = "advent_of_code_2022/src/main/kotlin/dayten/input.txt"
    println("Sum of the 20th, 60th, 100th, ..., 220th strengths: ${DayTen().main(filename)}")
}