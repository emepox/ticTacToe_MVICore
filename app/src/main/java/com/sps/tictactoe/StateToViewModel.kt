package com.sps.tictactoe

import com.sps.tictactoe.MainActivity.*
import com.sps.tictactoe.TicTacToeFeature.*
import com.sps.tictactoe.TicTacToeFeature.State.Player.*

object StateToViewModel : (State) -> ViewModel {
    override fun invoke(state: State): ViewModel =
        with(state) {
            ViewModel(
                board = board.toVmBoard(),
                gameCounter = result.toGameCounter(),
                activePlayer = player.toActivePlayer(),
                result = gameState.resultToTitle()
            )
        }

    private fun State.Result.toGameCounter(): ViewModel.GameCounter {
        return ViewModel.GameCounter(
            xWins = x_Wins,
            oWins = o_Wins,
            draws = draws_
        )
    }

    private fun State.Board.toVmBoard(): ViewModel.Board {
        return ViewModel.Board(
            c1 = c1.toVMCell(),
            c2 = c2.toVMCell(),
            c3 = c3.toVMCell(),
            c4 = c4.toVMCell(),
            c5 = c5.toVMCell(),
            c6 = c6.toVMCell(),
            c7 = c7.toVMCell(),
            c8 = c8.toVMCell(),
            c9 = c9.toVMCell(),
        )
    }

    private fun CellState.toVMCell(): ViewModel.CellState =
        when (this) {
            CellState.EMPTY -> ViewModel.CellState.EMPTY
            CellState.O -> ViewModel.CellState.O
            CellState.X -> ViewModel.CellState.X
        }

    private fun State.Player.toActivePlayer() =
        if (this == PLAYER_X) "Player X" else "Player O"

    private fun State.GameState.resultToTitle() = when (this) {
        State.GameState.WON -> "WON"
        State.GameState.DRAW -> "DRAW"
        else -> ""
    }
}