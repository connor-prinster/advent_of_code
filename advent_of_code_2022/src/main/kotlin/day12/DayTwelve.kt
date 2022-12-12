package day12

import java.io.File

enum class Direction {
    UP, DOWN, RIGHT, LEFT
}
data class HeightPair(val row: Int, val column: Int)
data class MapPoint(val height: Int, var visited: Boolean = false, var direction: String = ".")
data class HeightMap(
    var position: HeightPair = HeightPair(-1, -1),
    var bestSignal: HeightPair = HeightPair(-1, -1),
    var map: ArrayList<ArrayList<MapPoint>> = arrayListOf(arrayListOf()),
    var steps: Int = -1,
    var finished: Boolean = false
)

class DayTwelve {
    private fun parseFile(filename: String): HeightMap {
        val heightMap = HeightMap()

        File(filename)
            .useLines {
                var rowSize: Int
                var rowIdx: Int
                var rowCtr = 0
                it.toList().map { text ->
                    rowSize = text.length
                    text.split("")
                        .filter { string -> string != "" }
                        .map { string -> string.toCharArray()[0] }
                        .forEachIndexed { index, char ->
                            rowIdx = rowCtr / rowSize
                            rowCtr += 1
                            val colIdx = index % rowSize
                            heightMap.map = addRow(heightMap.map, rowIdx)
                            when (char.code) {
                                'S'.code -> {
                                    heightMap.position = HeightPair(rowIdx, colIdx)
                                    heightMap.map[rowIdx].add(MapPoint('a'.code))
                                }
                                'E'.code -> {
                                    heightMap.bestSignal = HeightPair(rowIdx, colIdx)
                                    heightMap.map[rowIdx].add(MapPoint('z'.code))
                                }
                                else -> {
                                    heightMap.map[rowIdx].add(MapPoint(char.code))
                                }
                            }
                        }
                }
            }

        return heightMap
    }

    private fun traverseMap(maps: ArrayList<HeightMap>): HeightMap {
        val finishedMaps: ArrayList<HeightMap> = arrayListOf()
        while (maps.isNotEmpty()) {
            val mapToAnalyze = maps.removeFirst()
            if (mapToAnalyze.position != mapToAnalyze.bestSignal) {
                val directions = checkDirections(map = mapToAnalyze, position = mapToAnalyze.position)
                directions.forEach { direction ->
                    maps.add(movePosition(mapToAnalyze, direction))
                }
            } else {
                finishedMaps.add(mapToAnalyze)
            }
        }

        return finishedMaps.sortedBy { it.steps }.reversed()[0]
    }

    private fun movePosition(map: HeightMap, direction: Direction): HeightMap {
        val newMap = when(direction) {
            Direction.LEFT -> {
                val newCol = map.position.column - 1
                map.map[map.position.row][newCol].visited = true
                map.map[map.position.row][newCol].direction = "<"
                map.copy(
                    position = HeightPair(map.position.row, newCol),
                    map = map.map,
                    steps = if(map.steps != -1) map.steps + 1 else 1
                )
            }
            Direction.RIGHT -> {
                val newCol = map.position.column + 1
                map.map[map.position.row][newCol].visited = true
                map.map[map.position.row][newCol].direction = ">"
                map.copy(
                    position = HeightPair(map.position.row, newCol),
                    map = map.map,
                    steps = if(map.steps != -1) map.steps + 1 else 1
                )
            }
            Direction.UP -> {
                val newRow = map.position.row - 1
                map.map[newRow][map.position.column].visited = true
                map.map[newRow][map.position.column].direction = "^"
                map.copy(
                    position = HeightPair(newRow, map.position.column),
                    map = map.map,
                    steps = if(map.steps != -1) map.steps + 1 else 1
                )
            }
            Direction.DOWN -> {
                val newRow = map.position.row + 1
                map.map[newRow][map.position.column].visited = true
                map.map[newRow][map.position.column].direction = "v"
                map.copy(
                    position = HeightPair(newRow, map.position.column),
                    map = map.map,
                    steps = if(map.steps != -1) map.steps + 1 else 1
                )
            }
        }

        return newMap
    }

    private fun checkDirections(map: HeightMap, position: HeightPair): ArrayList<Direction> {
        val directions = arrayListOf<Direction>()
        val startPoint = map.map[position.row][position.column]

        if (position.column - 1 >= 0 && checkShouldMove(startPoint, map.map[position.row][position.column - 1]) && !map.map[position.row][position.column - 1].visited) {
            directions.add(Direction.LEFT)
        }

        if (position.column + 1 < map.map[0].size && checkShouldMove(startPoint, map.map[position.row][position.column + 1]) && !map.map[position.row][position.column + 1].visited) {
            directions.add(Direction.RIGHT)
        }

        if (position.row - 1 >= 0 && checkShouldMove(startPoint, map.map[position.row - 1][position.column]) && !map.map[position.row - 1][position.column].visited) {
            directions.add(Direction.UP)
        }

        if (position.row + 1 < map.map.size && checkShouldMove(startPoint, map.map[position.row + 1][position.column]) && !map.map[position.row + 1][position.column].visited) {
            directions.add(Direction.DOWN)
        }

        return directions
    }

    private fun checkShouldMove(startPoint: MapPoint, endPoint: MapPoint): Boolean {
        return if (startPoint.height >= endPoint.height) {
            true
        } else startPoint.height == endPoint.height - 1
    }

    private fun addRow(map: ArrayList<ArrayList<MapPoint>>, rowIdx: Int): ArrayList<ArrayList<MapPoint>> {
        if (map.size <= rowIdx) {
            map.add(arrayListOf())
        }

        return map
    }

    fun findMinimumStepsToBestSignal(filename: String, isPartTwo: Boolean): Int {
        return traverseMap(arrayListOf(parseFile(filename))).steps
    }
}

fun main(args: Array<String>) {
    val filename = "advent_of_code_2022/src/main/kotlin/day12/input.txt"
    println("Minimum number of steps: ${DayTwelve().findMinimumStepsToBestSignal(filename, isPartTwo = false)}")
}