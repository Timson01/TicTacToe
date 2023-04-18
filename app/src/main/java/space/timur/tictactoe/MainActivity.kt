package space.timur.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import space.timur.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var isFirstPlayer = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ticTacToeField.ticTacToeField = TicTacToeField(3, 3)

        binding.ticTacToeField.actionListener = { row, column, field ->
            val cell = field.getCell(row, column)
            if (cell == Cell.EMPTY) {
                if (isFirstPlayer) {
                    field.setCell(row, column, Cell.PLAYER_1)
                } else {
                    field.setCell(row, column, Cell.PLAYER_2)
                }
                isFirstPlayer = !isFirstPlayer
            }
        }

        binding.randomFieldButton.setOnClickListener {
            for(i in 1..3){
                println()
                for(j in 0..2){
                    print("${binding.ticTacToeField.ticTacToeField!!.getCell(i,j)} ,")
                }
            }
        }
    }
}