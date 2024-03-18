package com.sps.tictactoe

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.PostProcessor
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.BaseFeature
import com.sps.tictactoe.TicTacToeFeature.*
import io.reactivex.Observable


/**
 * Complete State and feature with required logic..
 * Right now this is extending ActorReducerFeature. Feel Free To change it if you need it.
 * This is just an example, so if you don't need something, feel free to delete it.
 * E.g: You don't need the News? Delete them and pass <Nothing> instead as News Type
 * ActorReducerFeature<Wish, Effect, State, Nothing>
 */

class TicTacToeFeature : BaseFeature<Wish, Action, Effect, State, Nothing>(
    initialState = State(),
    reducer = ReducerImpl(),
    actor = ActorImpl(),
    postProcessor = PostProcessorImpl(),
    wishToAction = Action::Execute,
) {

    data class State(
        val gameState: GameState = GameState.READY_TO_PLAY,
        val player: Player = Player.PLAYER_X,
        val result: Result = Result(),
        val board: Board = Board(),
        val turn: String = ""
    ) {
        enum class GameState {
            READY_TO_PLAY, WON, DRAW
        }
        enum class Player {
            PLAYER_X, PLAYER_O
        }
        data class Result(
            val x_Wins: Int  = 0,
            val o_Wins: Int = 0,
            val draws_: Int = 0,
        )

        // O LISTA
        data class Board(
            val c1: CellState = CellState.EMPTY, //Index 0
            val c2: CellState = CellState.EMPTY, //Index 1
            val c3: CellState = CellState.EMPTY, //Index 2
            val c4: CellState = CellState.EMPTY, //Index 3
            val c5: CellState = CellState.EMPTY, //Index 4
            val c6: CellState = CellState.EMPTY, //Index 5
            val c7: CellState = CellState.EMPTY, //Index 6
            val c8: CellState = CellState.EMPTY, //Index 7
            val c9: CellState = CellState.EMPTY, //Index 8
        ) {
            fun asCellList() = listOf(c1, c2, c3, c4, c5, c6, c7, c8, c9)

            }
        }

    enum class CellState {
            EMPTY, X, O;
        }

    sealed class Effect {
        object CleanCells : Effect()
        data class PiecePlaced(val cell: Int) : Effect()
        object GameContinues : Effect()
        data class GameWon(val winner: State.Player) : Effect()
        object GameDraw : Effect()

    }

    sealed class Wish {
        data class PlacePiece(val cell: Int) : Wish()
        object ResetGame : Wish()
    }

    sealed class Action {
        data class Execute(val wish: Wish) : Action()
        data class CheckIfPlayerWon(val player: State.Player) : Action()
        object ChangePlayer : Action()
    }

    sealed class News {
        object SomeNews : News()
    }



    // *_*_*_*_*_*_*_*_*_*_*_* ACTOR *_*_*_*_*_*_*_*_*_*_*_*

    private class ActorImpl : Actor<State, Action, Effect> {
        override fun invoke(state: State, action: Action): Observable<out Effect> = when (action) {
            is Action.Execute -> when (action.wish) {

                is Wish.PlacePiece -> if(state.board.asCellList().elementAt(action.wish.cell) == CellState.EMPTY && state.gameState == State.GameState.READY_TO_PLAY) Observable.just(Effect.PiecePlaced(action.wish.cell)) else {
                    Observable.empty()}
                is Wish.ResetGame -> Observable.just(Effect.CleanCells)
            }
            is Action.CheckIfPlayerWon -> checkBoardAfterPlacingPiece(state.board, action.player).let { Observable.just(it) }
            is Action.ChangePlayer -> Observable.just(Effect.GameContinues)
        }

        fun checkBoardAfterPlacingPiece(board: State.Board, player: State.Player): Effect {
            val pieceType = if (player == State.Player.PLAYER_X) CellState.X else CellState.O
            val winner: List<List<Int>> = listOf(
                // horizontal
                listOf(0, 1, 2),
                listOf(3, 4, 5),
                listOf(6, 7, 8),
                // vertical
                listOf(0, 3, 6),
                listOf(1, 4, 7),
                listOf(2, 5, 8),
                // diagonal
                listOf(0, 4, 8),
                listOf(2, 4, 6)
            )

            val hasWon = checkWinnerCombination(winner, pieceType, board)

            return if (hasWon) {
                Effect.GameWon(player)
            } else {
                if (board.asCellList().all { it != CellState.EMPTY }) Effect.GameDraw
                else {
                    Effect.GameContinues
                }
            }
        }

        fun checkWinnerCombination(list: List<List<Int>>, pieceType: CellState, board: State.Board): Boolean {
            list.forEach { listOfCombinations ->
                val isWinner = listOfCombinations.all {
                    board.asCellList()[it] == pieceType
                }
                if(isWinner) return isWinner
            }
            return false
        }
    }


    // *_*_*_*_*_*_*_*_*_*_*_* REDUCER *_*_*_*_*_*_*_*_*_*_*_*

    private class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {

            is Effect.PiecePlaced -> {
                    state.copy(
                        board = state.board.copy(
                            c1 = if (effect.cell == 0) {setPlayerPiece(state.player, state.board.c1)} else state.board.c1,
                            c2 = if (effect.cell == 1) {setPlayerPiece(state.player, state.board.c2)} else state.board.c2,
                            c3 = if (effect.cell == 2) {setPlayerPiece(state.player, state.board.c3)} else state.board.c3,
                            c4 = if (effect.cell == 3) {setPlayerPiece(state.player, state.board.c4)} else state.board.c4,
                            c5 = if (effect.cell == 4) {setPlayerPiece(state.player, state.board.c5)} else state.board.c5,
                            c6 = if (effect.cell == 5) {setPlayerPiece(state.player, state.board.c6)} else state.board.c6,
                            c7 = if (effect.cell == 6) {setPlayerPiece(state.player, state.board.c7)} else state.board.c7,
                            c8 = if (effect.cell == 7) {setPlayerPiece(state.player, state.board.c8)} else state.board.c8,
                            c9 = if (effect.cell == 8) {setPlayerPiece(state.player, state.board.c9)} else state.board.c9,
                        )
                    )
                }

            is Effect.GameContinues -> {
                state.copy(
                    player = changePlayerAfterCheckingBoard(state.player)
                )
            }

            is Effect.CleanCells ->
                state.copy(
                    gameState = State.GameState.READY_TO_PLAY,
                    player = State.Player.PLAYER_X,
                    board = state.board.copy(
                        c1 = CellState.EMPTY,
                        c2 = CellState.EMPTY,
                        c3 = CellState.EMPTY,
                        c4 = CellState.EMPTY,
                        c5 = CellState.EMPTY,
                        c6 = CellState.EMPTY,
                        c7 = CellState.EMPTY,
                        c8 = CellState.EMPTY,
                        c9 = CellState.EMPTY,
                        ),
                )
            is Effect.GameWon ->
                state.copy(
                    gameState = State.GameState.WON,
                    result = state.result.copy(
                        x_Wins = if (state.player == State.Player.PLAYER_X) state.result.x_Wins + 1 else state.result.x_Wins,
                        o_Wins = if (state.player == State.Player.PLAYER_O) state.result.o_Wins + 1 else state.result.o_Wins,
                        draws_ = state.result.draws_
                    )
                )
            is Effect.GameDraw ->
                state.copy(
                    gameState = State.GameState.DRAW,
                    result = state.result.copy(
                        x_Wins = state.result.x_Wins,
                        o_Wins = state.result.o_Wins,
                        draws_ = state.result.draws_+1
                    )

                )
        }

        fun setPlayerPiece(player: State.Player, cellState: CellState):CellState {
                if (cellState == CellState.EMPTY) {
                    return when (player) {
                        State.Player.PLAYER_X -> CellState.X
                        State.Player.PLAYER_O -> CellState.O
                    }
                }
                 return cellState
        }

        fun changePlayerAfterCheckingBoard(player: State.Player): State.Player {
                return if (player == State.Player.PLAYER_X) return State.Player.PLAYER_O
                else State.Player.PLAYER_X
        }

    }


    // *_*_*_*_*_*_*_*_*_*_*_* POST PROCESSOR *_*_*_*_*_*_*_*_*_*_*_*

    class PostProcessorImpl : PostProcessor<Action, Effect, State> {
        override fun invoke(action: Action, effect: Effect, state: State): Action? = when (effect){
            is Effect.PiecePlaced -> Action.CheckIfPlayerWon(state.player)

            else -> {null}
        }
    }

    /*

    private class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(action: Wish, effect: Effect, state: State): News? {
            return null
        }
    }
     */
}

