package com.samnn.diceroller

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Dice(private val numSides: Int) {

    fun roll(): Int {
        return (1..numSides).random()
    }
}

/**
 * This activity allows the user to roll a dice and view the result
 * on the screen.
 *
 * Sound Effect by <a href="https://pixabay.com/users/u_qpfzpydtro-29496424/?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=142528">u_qpfzpydtro</a> from <a href="https://pixabay.com/sound-effects//?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=142528">Pixabay</a>
 */
class MainActivity : AppCompatActivity() {
    private val scope = MainScope()
    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val player = MediaPlayer.create(this, R.raw.dice_roll)

        val rollButton: Button = findViewById(R.id.roll_button)
        rollButton.setOnClickListener {
            var result = 1
            val repeatTurn: Long = (12..30).random().toLong()
            scope.launch {
                player.start()
                rollButton.text = "Is rolling"
                rollButton.isActivated = false
                for (i in 1..repeatTurn) {
                    rollDice().also { result = it }
                    delay(1000/repeatTurn)
                }
                val toast = Toast.makeText(this@MainActivity, "You rolled $result", Toast.LENGTH_SHORT)
                toast.show()
                rollButton.text = "Roll"
                rollButton.isActivated = true
            }
        }

        rollDice()
    }

    /**
     * Roll the dice and update the screen with the result.
     */
    private fun rollDice(): Int {
        // Create new Dice object with 6 sides and roll it
        val dice = Dice(6)
        val diceRoll = dice.roll()

        // Find the ImageView in the layout
        val diceImage: ImageView = findViewById(R.id.imageView)

        // Determine which drawable resource ID to use based on the dice roll
        val drawableResource = when (diceRoll) {
                1 -> R.drawable.dice_1
                2 -> R.drawable.dice_2
                3 -> R.drawable.dice_3
                4 -> R.drawable.dice_4
                5 -> R.drawable.dice_5
                6 -> R.drawable.dice_6
            else -> R.drawable.dice_broke
        }
        // Update the ImageView with the correct drawable resource ID
        diceImage.setImageResource(drawableResource)

        // Update the content description
        diceImage.contentDescription = diceRoll.toString()

        return diceRoll
    }

}