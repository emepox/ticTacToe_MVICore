package com.sps.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.rxjava2.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sps.tictactoe.MainActivity.ViewModel.CellState.EMPTY
import com.sps.tictactoe.composables.*
import com.sps.tictactoe.ui.theme.TicTacToeTheme
import io.reactivex.Observable
import io.reactivex.ObservableSource

class MainActivity : ComponentActivity() {

    /**
     * This is a suggested viewmodel.
     * You can change it to fits your needs.
     * You will have to change the view if you change the vm.
     */

    data class ViewModel(
        val board: Board = Board(),
        val gameCounter: GameCounter = GameCounter(),
        val activePlayer: String = "",
        val result: String = ""
    ) {
        /**
         *   |c1|c2|c3|
         *   |c4|c5|c6|
         *   |c7|c8|c9|
         */
        data class Board(
            val c1: CellState = EMPTY, //Index 0
            val c2: CellState = EMPTY, //Index 1
            val c3: CellState = EMPTY, //Index 2
            val c4: CellState = EMPTY, //Index 3
            val c5: CellState = EMPTY, //Index 4
            val c6: CellState = EMPTY, //Index 5
            val c7: CellState = EMPTY, //Index 6
            val c8: CellState = EMPTY, //Index 7
            val c9: CellState = EMPTY, //Index 8
        ) {
            fun asCellList() = listOf(c1, c2, c3, c4, c5, c6, c7, c8, c9)
        }

        enum class CellState {
            EMPTY, X, O;
        }

        data class GameCounter(
            val xWins: Int = 0,
            val oWins: Int = 0,
            val draws: Int = 0,
        )
    }

    private val featureState = TicTacToeFeature()
    private val viewModelObservable: Observable<ViewModel> = featureState.wrapToObservable().map {
        StateToViewModel(it)
    }

    private fun mapViewModel(state: TicTacToeFeature.State): ViewModel {
        //TODO: map your state to the expectedViewModel here
        // Given a state in the feature, change the viewmodel


        return ViewModel()
    }

    private fun onResetClicked() {
        // TODO: invoke whatever you need here to reset the feature
        featureState.accept(TicTacToeFeature.Wish.ResetGame)
    }

    private fun onCellClicked(cellIndex: Int) {
        // TODO: invoke whatever you need here to place a piece in the board
        featureState.accept(TicTacToeFeature.Wish.PlacePiece(cellIndex))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val boardState = viewModelObservable.map { it.board }.subscribeAsState(ViewModel.Board())

                    val counterState =
                        viewModelObservable.map { it.gameCounter }
                            .subscribeAsState(ViewModel.GameCounter())
                    val currentPlayer =
                        viewModelObservable.map { it.activePlayer }.subscribeAsState(initial = "")

                    val gameResult =
                        viewModelObservable.map { it.result }.subscribeAsState(initial = "")

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TitleText(myTitle = currentPlayer.value.toString())
                        ResultText(myResult = gameResult.value.toString())
                        GameBoard(board = boardState.value) {
                            onCellClicked(it)
                        }
                        ResetButton(::onResetClicked)
                        GameCounter(gameCounter = counterState.value)
                    }

                }
            }
        }
    }

    private fun <T> ObservableSource<out T>.wrapToObservable(): Observable<T> =
        Observable.wrap(cast())

    private inline fun <reified T> Any?.cast(): T = this as T

}






