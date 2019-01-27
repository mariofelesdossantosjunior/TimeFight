package com.mario.timefight

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var score = 0
    private var gameStarted = false
    private val initialCountDown: Long = 60000
    private val countDownInterval: Long = 1000
    private lateinit var countDownTimer: CountDownTimer
    private var timeLeftOnTime: Long = 60000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTime = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }

        game_score_text_view.text = getString(R.string.your_score, score.toString())

        tap_me_button.setOnClickListener {
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
            it.startAnimation(bounceAnimation)
            incrementScore()
        }
    }

    private fun restoreGame() {
        game_score_text_view.text = getString(R.string.your_score, score.toString())
        val restoredTime = timeLeftOnTime / 1000
        time_left_text_view.text = getString(R.string.time_left, restoredTime.toString())

        countDownTimer = object : CountDownTimer(timeLeftOnTime, countDownInterval) {

            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTime = millisUntilFinished
                val timeLeft = millisUntilFinished / 1000
                time_left_text_view.text = getString(R.string.time_left, timeLeft.toString())
            }

            override fun onFinish() {
                endGame()
            }
        }

        countDownTimer.start()
        gameStarted = true
    }

    fun resetGame() {
        score = 0
        game_score_text_view.text = getString(R.string.your_score, score.toString())
        val initialTimeLeft = initialCountDown / 1000
        time_left_text_view.text = getString(R.string.time_left, initialTimeLeft.toString())

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {

            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = millisUntilFinished / 1000
                time_left_text_view.text = getString(R.string.time_left, timeLeft.toString())
            }

            override fun onFinish() {
                endGame()
            }
        }

        gameStarted = false
    }

    private fun incrementScore() {
        if (!gameStarted) {
            startGame()
        }

        score += 1
        val newScore = getString(R.string.your_score, score.toString())
        game_score_text_view.text = newScore

        val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink)
        game_score_text_view.startAnimation(blinkAnimation)

    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }

    fun endGame() {
        Toast.makeText(this, getString(R.string.game_over_message, score.toString()), Toast.LENGTH_SHORT).show()
        resetGame()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_about) {
            showInfo()
        }
        return true
    }

    private fun showInfo() {
        val dialogTitle = getString(R.string.about_title, BuildConfig.VERSION_NAME)
        val dialogMessage = getString(R.string.about_message)
        AlertDialog.Builder(this)
        .setTitle(dialogTitle)
        .setMessage(dialogMessage)
        .create().show()
    }

    companion object {
        private val SCORE_KEY = "SCORE_KEY"
        private val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }
}
