package space.timur.tictactoe.ui.one_player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import space.timur.tictactoe.Cell
import space.timur.tictactoe.TicTacToeField
import space.timur.tictactoe.databinding.FragmentPlayBinding

class OnePlayerFragment : Fragment() {

    private lateinit var binding: FragmentPlayBinding
    private var numberOfWinsPlayer1 = 0
    private var numberOfWinsPlayer2 = 0
    var isFirstPlayer = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addRequest = AdRequest.Builder().build()
        binding.adView.loadAd(addRequest)

        val numberOfPlayers: Int = arguments?.getInt("numberOfPlayers") ?: 1

        binding.ticTacToeField.ticTacToeField = TicTacToeField(3, 3)

        if(numberOfPlayers == 1){
            binding.ticTacToeField.actionListener = { row, column, field ->
                val cell = field.getCell(row, column)

                if (cell == Cell.EMPTY) {

                    field.setCell(row, column, Cell.PLAYER_1)
                    check()
                    handler.postDelayed({
                        step(binding.ticTacToeField.ticTacToeField!!)
                        check()
                    }, 200)

                }
            }
        } else {
            binding.ticTacToeField.actionListener = { row, column, field ->
                val cell = field.getCell(row, column)
                if (cell == Cell.EMPTY) {

                    if (isFirstPlayer) {
                        field.setCell(row, column, Cell.PLAYER_1)
                    } else {
                        field.setCell(row, column, Cell.PLAYER_2)
                    }

                    check()

                    isFirstPlayer = !isFirstPlayer
                }
            }
        }

        binding.resetBtn.setOnClickListener {
            numberOfWinsPlayer1 = 0
            numberOfWinsPlayer2 = 0
            binding.playerOneCount.text = numberOfWinsPlayer1.toString()
            binding.playerTwoCount.text = numberOfWinsPlayer2.toString()
        }
    }

    private fun step(field: TicTacToeField): Boolean {

        val emptyCells = mutableListOf<Pair<Int, Int>>()

        // Check rows and columns
        for (i in 0..2) {
            if (field.cells[i][0] == field.cells[i][1] && field.cells[i][0] != Cell.EMPTY && field.cells[i][2] == Cell.EMPTY) {
                field.setCell(i, 2, Cell.PLAYER_2)
                return true
            } else if (field.cells[i][1] == field.cells[i][2] && field.cells[i][1] != Cell.EMPTY && field.cells[i][0] == Cell.EMPTY) {
                field.setCell(i, 0, Cell.PLAYER_2)
                return true
            } else if (field.cells[0][i] == field.cells[1][i] && field.cells[0][i] != Cell.EMPTY && field.cells[2][i] == Cell.EMPTY) {
                field.setCell(2, i, Cell.PLAYER_2)
                return true
            } else if (field.cells[1][i] == field.cells[2][i] && field.cells[1][i] != Cell.EMPTY && field.cells[0][i] == Cell.EMPTY) {
                field.setCell(0, i, Cell.PLAYER_2)
                return true
            } else {
                for (j in 0..2) {
                    if (field.cells[i][j] == Cell.EMPTY) {
                        emptyCells.add(Pair(i, j))
                    }
                }
            }
        }

        // Check diagonals and center
        if (field.cells[1][1] != Cell.EMPTY) {
            if (field.cells[0][0] == field.cells[1][1] && field.cells[2][2] == Cell.EMPTY) {
                field.setCell(2, 2, Cell.PLAYER_2)
                return true
            } else if (field.cells[2][2] == field.cells[1][1] && field.cells[0][0] == Cell.EMPTY) {
                field.setCell(0, 0, Cell.PLAYER_2)
                return true
            } else if (field.cells[0][2] == field.cells[1][1] && field.cells[2][0] == Cell.EMPTY) {
                field.setCell(2, 0, Cell.PLAYER_2)
                return true
            } else if (field.cells[2][0] == field.cells[1][1] && field.cells[0][2] == Cell.EMPTY) {
                field.setCell(0, 2, Cell.PLAYER_2)
                return true
            }
        } else if (field.cells[1][1] == Cell.EMPTY && emptyCells.contains(Pair(1, 1))) {
            field.setCell(1, 1, Cell.PLAYER_2)
            return true
        }

        // Default
        if (emptyCells.isNotEmpty()) {
            val (row, col) = emptyCells.random()
            field.setCell(row, col, Cell.PLAYER_2)
            return true
        }

        return false

    }

    private fun check(){
        if(checkForDraw(binding.ticTacToeField.ticTacToeField!!.cells)){
            showMessage("Draw")
            binding.ticTacToeField.ticTacToeField = TicTacToeField(3, 3)
        } else {
            when(checkForWinner(binding.ticTacToeField.ticTacToeField!!.cells)){
                Cell.PLAYER_1 -> {
                    showMessage("Player 1 won the game")
                    numberOfWinsPlayer1++
                    binding.playerOneCount.text = numberOfWinsPlayer1.toString()
                    binding.ticTacToeField.ticTacToeField = TicTacToeField(3, 3)
                }
                Cell.PLAYER_2 -> {
                    showMessage("Player 2 won the game")
                    numberOfWinsPlayer2++
                    binding.playerTwoCount.text = numberOfWinsPlayer2.toString()
                    binding.ticTacToeField.ticTacToeField = TicTacToeField(3, 3)
                }
                else -> {}
            }
        }
    }

    private fun checkForDraw(field: Array<Array<Cell>>): Boolean{
        for(row in 0..2){
            for(col in 0..2){
                if(field[row][col] == Cell.EMPTY){
                    return false
                }
            }
        }
        return true
    }

    private fun checkForWinner(field: Array<Array<Cell>>): Cell {
        // Check rows
        for (row in 0..2) {
            if (field[row][0] != Cell.EMPTY && field[row][0] == field[row][1] && field[row][1] == field[row][2]) {
                return field[row][0]
            }
        }

        // Check columns
        for (col in 0..2) {
            if (field[0][col] != Cell.EMPTY && field[0][col] == field[1][col] && field[1][col] == field[2][col]) {
                return field[0][col]
            }
        }

        // Check diagonals
        if (field[0][0] != Cell.EMPTY && field[0][0] == field[1][1] && field[1][1] == field[2][2]) {
            return field[0][0]
        }
        if (field[0][2] != Cell.EMPTY && field[0][2] == field[1][1] && field[1][1] == field[2][0]) {
            return field[0][2]
        }

        return Cell.EMPTY // No winner
    }

    private fun showMessage(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}