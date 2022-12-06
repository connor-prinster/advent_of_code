package daysix

import java.io.File

data class Packet(val packet: String)

class DaySix {
    private fun parseFile(filename: String): ArrayList<Packet> {
        val packets: ArrayList<Packet> = arrayListOf()
        File(filename)
            .useLines {
                it.toList().forEach { packet ->
                    packets.add(Packet(packet = packet))
                }
            }

        return packets
    }

    fun howManyCharacters(filename: String): String {
        val packets = parseFile(filename)
        return packets.toString()
    }
}

fun main(args: Array<String>) {
    println("How many characters must be parsed: ${DaySix().howManyCharacters("advent_of_code_2022/src/main/kotlin/daysix/test_input.txt")}")
//    println("Top of each stack Crane9001: ${DayFive().findTopCrate("advent_of_code_2022/src/main/kotlin/dayfive/input.txt", is9001 = true)}")
}