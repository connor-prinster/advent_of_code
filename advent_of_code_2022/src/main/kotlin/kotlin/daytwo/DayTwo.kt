package daytwo

import java.io.File

class DayTwoPartOne {
    // ROCK, PAPER, SCISSORS
    enum class OpponentMove(val points: Int) { A(1), B(2), C(3) }
    enum class YourMove(val points: Int) { X(1), Y(2), Z(3) }
    enum class Cases(val points: Int) { WON(6), DRAW(3), LOSS(0) }

    data class Strategy(val opponentMove: OpponentMove, val yourMove: YourMove) {
        fun pointsWon(): Int {
            return if ((opponentMove == OpponentMove.A && yourMove == YourMove.X) || (opponentMove == OpponentMove.B && yourMove == YourMove.Y) || (opponentMove == OpponentMove.C && yourMove == YourMove.Z)) {
                Cases.DRAW.points + yourMove.points
            } else if ((opponentMove == OpponentMove.A && yourMove == YourMove.Z) || (opponentMove == OpponentMove.B && yourMove == YourMove.X) || (opponentMove == OpponentMove.C && yourMove == YourMove.Y)) {
                Cases.LOSS.points + yourMove.points
            } else {
                Cases.WON.points + yourMove.points
            }
        }
    }

    fun calculateStrategy(): Int {
        var points: Int = 0

        File("src/main/kotlin/daytwo/input.txt")
            .useLines {
                    it.toMutableList()
            }
            .map { strategyTextList ->
                val splitList = strategyTextList.split(" ")
                Strategy(
                    opponentMove = OpponentMove.valueOf(value = splitList[0]),
                    yourMove = YourMove.valueOf(splitList[1])
                )
            }
            .forEach {
                points += it.pointsWon()
            }

        return points
    }
}

class DayTwoPartTwo {
    // ROCK, PAPER, SCISSORS
    enum class OpponentMove { A, B, C }
    // LOSE, DRAW, WIN
    enum class Cases(val points: Int) { X(0), Y(3), Z(6) }
    enum class YourMove(val points: Int) {
        ROCK(1),
        PAPER(2),
        SCISSORS(3)
    }


    data class Strategy(val opponentMove: OpponentMove, val case: Cases) {
        fun pointsWon(): Int {
            return case.points + when (case) {
                Cases.X -> { // LOSS
                    when(opponentMove) {
                        OpponentMove.A -> YourMove.SCISSORS.points
                        OpponentMove.B -> YourMove.ROCK.points
                        else -> YourMove.PAPER.points
                    }
                }
                Cases.Y -> { // DRAW
                    when(opponentMove) {
                        OpponentMove.A -> YourMove.ROCK.points
                        OpponentMove.B -> YourMove.PAPER.points
                        else -> YourMove.SCISSORS.points
                    }
                }
                else -> { // WIN
                    when(opponentMove) {
                        OpponentMove.A -> YourMove.PAPER.points
                        OpponentMove.B -> YourMove.SCISSORS.points
                        else -> YourMove.ROCK.points
                    }
                }
            }
        }
    }

    fun calculateStrategy(): Int {
        var points: Int = 0

        File("src/main/kotlin/daytwo/input.txt")
            .useLines {
                    it.toMutableList()
            }
            .map { strategyTextList ->
                val splitList = strategyTextList.split(" ")
                Strategy(
                    opponentMove = OpponentMove.valueOf(value = splitList[0]),
                    case = Cases.valueOf(splitList[1])
                )
            }
            .forEach {
                points += it.pointsWon()
            }

        return points
    }
}

fun main(args: Array<String>) {
    println("Points won first way: ${DayTwoPartOne().calculateStrategy()}")
    println("Points won second way: ${DayTwoPartTwo().calculateStrategy()}")
}