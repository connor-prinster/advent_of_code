package inc.blubz.adventofcode25.days.dayOne

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

enum class Direction {
    RIGHT, LEFT
}

data class Rotations(val direction: Direction, val value: Int)

@HiltViewModel
class DayOneViewModel @Inject constructor() : ViewModel() {
    private val _currentPositionFlow = MutableStateFlow(START_POINT)
    val currentPositionFlow = _currentPositionFlow.asStateFlow()
    private val instructionsFlow = MutableStateFlow<List<Rotations>>(listOf())

    companion object {
        const val INPUT = DayOneInput.TEST_INPUT
        const val START_POINT = 50
        const val MAX_NUMBER = 99
        const val MIN_NUMBER = 0
    }

    init {
        instructionsFlow.value = INPUT.split("\n").map {
            val directionChar = it.first()

            val value = it.drop(1).toInt()

            Rotations(
                direction = when (directionChar) {
                    'R' -> Direction.RIGHT
                    else -> Direction.LEFT
                }, value = value
            )
        }

        Log.d("DayOneViewModel", "init: $instructionsFlow")
    }
}