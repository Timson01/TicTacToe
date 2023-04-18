package space.timur.tictactoe.ui.two_players

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import space.timur.tictactoe.Cell
import space.timur.tictactoe.TicTacToeField
import space.timur.tictactoe.databinding.FragmentTwoPlayersBinding

class TwoPlayersFragment : Fragment() {

    private lateinit var binding: FragmentTwoPlayersBinding
    var isFirstPlayer = true
    private var numberOfWinsPlayer1 = 0
    private var numberOfWinsPlayer2 = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTwoPlayersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ticTacToeField.ticTacToeField = TicTacToeField(3, 3)

        binding.ticTacToeField.actionListener = { row, column, field ->
            val cell = field.getCell(row, column)
            if (cell == Cell.EMPTY) {

                if (isFirstPlayer) {
                    field.setCell(row, column, Cell.PLAYER_1)
                } else {
                    field.setCell(row, column, Cell.PLAYER_2)
                }

                if(checkForDraw(binding.ticTacToeField.ticTacToeField!!.cells)){
                    showMessage("Draw")
                    binding.ticTacToeField.ticTacToeField = TicTacToeField(3, 3)
                }

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

                isFirstPlayer = !isFirstPlayer
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