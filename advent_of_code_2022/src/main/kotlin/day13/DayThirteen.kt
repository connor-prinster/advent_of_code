package day13

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import java.io.File

enum class Type {
    ARRAY, INTEGER
}
data class PacketValue(var integer: Int? = null, var array: List<PacketValue>? = null, val type: Type)
data class Packet(val left: List<PacketValue> = listOf(), val right: List<PacketValue> = listOf())
data class JsonPacket(var left: JsonArray = Json.parseToJsonElement("[]").jsonArray, var right: JsonArray = Json.parseToJsonElement("[]").jsonArray)

class DayThirteen {

    private fun parseFile(filename: String): ArrayList<JsonPacket> {
        val packets: ArrayList<JsonPacket> = arrayListOf()
        var first = true
        val jsonPacket = JsonPacket()
        File(filename)
            .useLines {
                it.toList().forEach { text ->
                    if (text == "") {
                        packets.add(jsonPacket.copy())
                        return@forEach
                    }
                    else if (first) {
                        jsonPacket.left = Json.parseToJsonElement(text).jsonArray
                    } else {
                        jsonPacket.right = Json.parseToJsonElement(text).jsonArray
                    }
                    first = !first
                }
            }

        return packets
    }

    private fun parseToPackets(list: List<JsonPacket>): List<Packet> {
        return list.map {
            parseToPacket(it)
        }.toList()
    }

    private fun parseToPacket(packet: JsonPacket): Packet {
        val left = listToPacket(packet.left)
        val right = listToPacket(packet.right)

        return Packet(
            left = left,
            right = right
        )
    }

    private fun listToPacket(array: JsonArray): List<PacketValue> {
        return array.map {
            if (it is JsonPrimitive) {
                PacketValue(integer = it.toString().toInt(), type = Type.INTEGER)
            } else {
                PacketValue(array = listToPacket(it.jsonArray), type = Type.ARRAY)
            }
        }
    }

    private fun checkPairs(jsonPackets: List<Packet>): Int {
        val rightIndices: ArrayList<Int> = arrayListOf()
        jsonPackets.forEachIndexed { index, packet ->
            if(isLeftLessThan(packet.left.toMutableList(), packet.right.toMutableList())) {
                rightIndices.add(index + 1)
            }
        }

        return rightIndices.sum()
    }

    private fun isLeftLessThan(left: MutableList<PacketValue>, right: MutableList<PacketValue>): Boolean {
        while (left.size > 0 && right.size > 0) {
            if (isLeftLessThan(left.removeAt(0), right.removeAt(0))) {
                return true
            }
        }

        if (left.size == 0) return true
        if (right.size == 0) return false

        return false
    }

    private fun isLeftLessThan(left: MutableList<PacketValue>, right: Int): Boolean {
        while(left.size > 0) {
            val tempLeft = left.removeAt(0)
            if (tempLeft.type == Type.INTEGER && isLeftLessThan(tempLeft.integer!!, right)) {
                return true
            } else if (tempLeft.type == Type.ARRAY && isLeftLessThan(tempLeft.array!!.toMutableList(), right)) {
                return true
            }
        }
        return false
    }

    private fun isLeftLessThan(left: Int, right: MutableList<PacketValue>): Boolean {
        while(right.size > 0) {
            val tempRight = right.removeAt(0)
            if (tempRight.type == Type.INTEGER && isLeftLessThan(left, tempRight.integer!!)) {
                return true
            } else if (tempRight.type == Type.ARRAY && isLeftLessThan(left, tempRight.array!!.toMutableList())) {
                return true
            }
        }
        return false
    }

    private fun isLeftLessThan(left: Int, right: Int): Boolean {
        return left < right
    }

    private fun isLeftLessThan(left: PacketValue, right: PacketValue): Boolean {
        if (left.type == Type.INTEGER && right.type == Type.INTEGER && isLeftLessThan(left.integer!!, right.integer!!)) {
            return true
        } else if (left.type == Type.INTEGER && right.type == Type.ARRAY && isLeftLessThan(left = left.integer!!, right = right.array!!.toMutableList())) {
            return true
        } else if (left.type == Type.ARRAY && right.type == Type.INTEGER && isLeftLessThan(left = left.array!!.toMutableList(), right = right.integer!!)) {
            return true
        } else if (left.type == Type.ARRAY && right.type == Type.ARRAY && isLeftLessThan(left.array!!.toMutableList(), right.array!!.toMutableList())) {
            return true
        }

        return false
    }



    fun sumOfCorrectIndices(filename: String): Int {
        return checkPairs(parseToPackets(parseFile(filename)))
    }
}

fun main() {
    val filename = "advent_of_code_2022/src/main/kotlin/day13/test_input.txt"
    // not 10675
    println("Sum of correctIndices: ${ DayThirteen().sumOfCorrectIndices(filename) }")
}