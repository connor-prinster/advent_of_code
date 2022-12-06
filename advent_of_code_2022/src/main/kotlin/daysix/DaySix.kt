package daysix

import java.io.File
import java.util.Collections

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

    private fun findFirstDistinctCharacters(packet: String, numberOfCharacters: Int): Int {
        val characters: ArrayList<Char> = arrayListOf()
        packet.toList().forEachIndexed { index, char ->
            if (characters.size < numberOfCharacters) {
                characters.add(char)
            }
            else {
                if (listContainsAnythingTwice(characters)) {
                    characters.removeFirst()
                    characters.add(char)
                } else {
                    return index
                }
            }
        }

        return -1
    }

    private fun listContainsAnythingTwice(list: ArrayList<Char>): Boolean {
        for (item in list.distinct()) {
            if (Collections.frequency(list, item) > 1) return true
        }
        return false
    }

    fun howManyCharactersBeforeMarker(filename: String, numberOfCharacters: Int): String {
        val positions: ArrayList<Int> = arrayListOf()
        parseFile(filename).forEach {
            positions.add(findFirstDistinctCharacters(packet = it.packet, numberOfCharacters = numberOfCharacters))
        }
        return positions.toString()
    }
}

fun main(args: Array<String>) {
    val filename = "advent_of_code_2022/src/main/kotlin/daysix/input.txt"
//    [7, 5, 6, 10, 11]
    println("How many characters must be parsed for marker: ${DaySix().howManyCharactersBeforeMarker(filename, 4)}")
//    [19, 23, 23, 29, 26]
    println("How many characters must be parsed for message: ${DaySix().howManyCharactersBeforeMarker(filename, 14)}")
}