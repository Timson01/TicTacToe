package space.timur.tictactoe.ui.one_player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import space.timur.tictactoe.Cell
import space.timur.tictactoe.TicTacToeField
import space.timur.tictactoe.databinding.FragmentOnePlayerBinding

class OnePlayerFragment : Fragment() {

    private lateinit var binding: FragmentOnePlayerBinding
    private var numberOfWinsPlayer1 = 0
    private var numberOfWinsPlayer2 = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnePlayerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ticTacToeField.ticTacToeField = TicTacToeField(3, 3)

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
    }

    private fun check(){
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
    }

    private fun step(field: TicTacToeField): Boolean{

        // Check rows

        for(row in 0..2){
            if(field.cells[row][0] != Cell.EMPTY && field.cells[row][1] != Cell.EMPTY && field.cells[row][0] == field.cells[row][1] && field.cells[row][2] == Cell.EMPTY){
                field.setCell(row, 2, Cell.PLAYER_2)
                return true
            } else if(field.cells[row][2] != Cell.EMPTY && field.cells[row][1] != Cell.EMPTY && field.cells[row][2] == field.cells[row][1] && field.cells[row][0] == Cell.EMPTY){
                field.setCell(row, 0, Cell.PLAYER_2)
                return true
            }
        }

        // Check columns

        for(col in 0..2){
            if(field.cells[0][col] != Cell.EMPTY && field.cells[1][col] != Cell.EMPTY && field.cells[0][col] == field.cells[1][col] && field.cells[2][col] == Cell.EMPTY){
                field.setCell(2, col, Cell.PLAYER_2)
                return true
            } else if(field.cells[1][col] != Cell.EMPTY && field.cells[2][col] != Cell.EMPTY && field.cells[2][col] == field.cells[1][col] && field.cells[0][col] == Cell.EMPTY){
                field.setCell(0, col, Cell.PLAYER_2)
                return true
            }
        }

        // Check diagonals

        if (field.cells[0][0] != Cell.EMPTY && field.cells[0][0] == field.cells[1][1] && field.cells[2][2] == Cell.EMPTY) {
            field.setCell(2,2, Cell.PLAYER_2)
            return true
        } else if(field.cells[1][1] != Cell.EMPTY && field.cells[1][1] == field.cells[2][2] && field.cells[0][0] == Cell.EMPTY){
            field.setCell(0,0, Cell.PLAYER_2)
            return true
        } else if(field.cells[0][0] != Cell.EMPTY && field.cells[2][2] != Cell.EMPTY && field.cells[0][0] == field.cells[2][2] && field.cells[1][1] == Cell.EMPTY){
            field.setCell(1,1, Cell.PLAYER_2)
            return true
        }

        if (field.cells[0][2] != Cell.EMPTY && field.cells[0][2] == field.cells[1][1] && field.cells[2][0] == Cell.EMPTY) {
            field.setCell(2,0, Cell.PLAYER_2)
            return true
        } else if(field.cells[2][0] != Cell.EMPTY && field.cells[2][0] == field.cells[1][1] && field.cells[0][2] == Cell.EMPTY){
            field.setCell(0,2, Cell.PLAYER_2)
            return true
        } else if(field.cells[0][2] != Cell.EMPTY && field.cells[2][0] != Cell.EMPTY && field.cells[0][2] == field.cells[2][0] && field.cells[1][1] == Cell.EMPTY){
            field.setCell(1,1, Cell.PLAYER_2)
            return true
        }


        // Check center

        for(i in 0..2){

            // Check rows

            if(field.cells[i][0] != Cell.EMPTY && field.cells[i][2] != Cell.EMPTY && field.cells[i][0] == field.cells[i][2] && field.cells[i][1] == Cell.EMPTY){
                field.setCell(i, 1, Cell.PLAYER_2)
                return true
            }

            // Check columns

            if(field.cells[0][i] != Cell.EMPTY && field.cells[2][i] != Cell.EMPTY && field.cells[0][i] == field.cells[2][i] && field.cells[1][i] == Cell.EMPTY){
                field.setCell(1, i, Cell.PLAYER_2)
                return true
            }

        }

        // Default
        for(row in 0..2){
            for(col in 0..2){
                if(field.cells[row][col] == Cell.EMPTY){
                    field.setCell(row, col, Cell.PLAYER_2)
                    return true
                }
            }
        }

        return false

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