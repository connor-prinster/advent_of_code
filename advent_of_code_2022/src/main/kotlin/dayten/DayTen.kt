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

    private fun returnCrtScreen(commands: List<Command>): String {
        var cycle = 0
        var register = 1
        var screen = ""

        commands.forEach { command ->
            when(command.type) {
                CommandType.ADDX -> {
                    for (i in 0 until 2) {
                        screen += printWhat(cycle, register)
                        cycle += 1

                    }
                    register += command.integer!!
                }
                else -> {
                    screen += printWhat(cycle, register)
                    cycle += 1
                }
            }
        }

        return screen
    }

    private fun addToListOfSignalStrengths(signalStrengths: ArrayList<Int>, cycle: Int, registerValue: Int): ArrayList<Int> {
        if (cycle == 20 || (cycle - 20) % 40 == 0) {
            val signalStrength = cycle * registerValue
            signalStrengths.add(signalStrength)
        }
        return signalStrengths
    }

    private fun printWhat(cycle: Int, register: Int): String {
        return (
                litOrDark(register = register, cycle = cycle) +
                        newLineOrNot(cycle = cycle)
        )
    }

    private fun litOrDark(register: Int, cycle: Int): String {
        return if(listOf(register - 1, register, register + 1).contains(cycle % 40)) {
            "#"
        }
        else {
            " "
        }
    }

    private fun newLineOrNot(cycle: Int): String {
        return if (cycle == 0) {
            ""
        } else if ((cycle + 1) % 40 == 0) {
            "\n"
        } else {
            ""
        }

    }

    fun main(filename: String, isPartTwo: Boolean = false): String {
        val commands = parseFile(filename)
        return if(isPartTwo){
            returnCrtScreen(commands)
        } else {
            var sum = 0
            returnSignalStrengths(commands).forEach {
                sum += it
            }
            sum.toString()
        }
    }
}

fun main(args: Array<String>) {
    val filename = "advent_of_code_2022/src/main/kotlin/dayten/input.txt"
    println("Sum of the 20th, 60th, 100th, ..., 220th strengths: ${DayTen().main(filename)}")
    println("CrtScreen:\n ${DayTen().main(filename, isPartTwo = true)}")
}