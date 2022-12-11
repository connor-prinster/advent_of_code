package day8

import java.io.File

class DayEight {

    private fun parseFile(filename: String): List<List<Int>> {
        File(filename)
            .useLines {
                return it.toList().map { horizontalTree ->
                    horizontalTree.map { char ->
                        char.toString().toInt()
                    }
                }
        }
    }

    private fun checkGrid(treeGrid: List<List<Int>>): Int {
        var visibleTrees = 0

        treeGrid.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, _ ->
                visibleTrees +=
                    if (checkTreeIsVisible(
                            treeGrid,
                            rowIndex = rowIndex,
                            columnIndex = colIndex
                        )) 1 else 0
            }
        }

        return visibleTrees
    }

    private fun checkTreeIsVisible(treeGrid: List<List<Int>>, rowIndex: Int, columnIndex: Int): Boolean {
        if (isTreeOnEdge(treeGrid = treeGrid, rowIndex = rowIndex, columnIndex = columnIndex)) {
            return true
        }

        return visibleFromRow(
            row = treeGrid[rowIndex],
            columnIndex = columnIndex) ||
                visibleFromColumn(
                    column = retrieveColumnFromGrid(treeGrid, columnIndex),
                    rowIndex = rowIndex
                )
    }

    private fun isTreeOnEdge(treeGrid: List<List<Int>>, rowIndex: Int, columnIndex: Int): Boolean {
        return rowIndex == 0 || rowIndex == treeGrid.size - 1 || columnIndex == 0 || columnIndex == treeGrid[rowIndex].size - 1
    }

    private fun visibleFromColumn(column: List<Int>, rowIndex: Int): Boolean {
        val treeValue = column[rowIndex]
        column.forEachIndexed { idx, neighboringTree ->
            if (idx != rowIndex) {
                if (neighboringTree < treeValue) return true
            }
        }

        return false
    }

    private fun visibleFromRow(row: List<Int>, columnIndex: Int): Boolean {
        val treeValue = row[columnIndex]
        row.forEachIndexed { idx, neighboringTree ->
            if (idx != columnIndex) {
                if (neighboringTree < treeValue) return true
            }
        }

        return false
    }

    private fun retrieveColumnFromGrid(treeGrid: List<List<Int>>, columnIndex: Int): List<Int> {
        val columnVals = arrayListOf<Int>()
        treeGrid.forEachIndexed { _, row ->
            row.forEachIndexed { colIdx, colVal ->
                if (colIdx == columnIndex) columnVals.add(colVal)
            }
        }
        return columnVals
    }

    fun countTheTrees(filename: String): Int {
        return checkGrid(parseFile(filename))
    }
}

fun main(args: Array<String>) {
    val filename = "advent_of_code_2022/src/main/kotlin/day8/test_input.txt"
    println("How many trees are visible: ${DayEight().countTheTrees(filename)}")
}