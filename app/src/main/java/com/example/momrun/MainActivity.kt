package com.example.momrun

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.momrun.logic.GameManager
import com.example.momrun.model.ObstacleType
import com.example.momrun.utilities.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var gameManager: GameManager

    private lateinit var main_FAB_left: FloatingActionButton

    private lateinit var main_FAB_right: FloatingActionButton

    private lateinit var main_IMG_players: Array<AppCompatImageView>

    private lateinit var main_IMG_lives: Array<AppCompatImageView>

    private lateinit var main_IMG_obstacles: Array<Array<AppCompatImageView>>

    private var toast: Toast? = null

    private val handler = Handler(Looper.getMainLooper())

    private val gameLoop: Runnable = object : Runnable {
        override fun run() {
            gameTick()
            handler.postDelayed(this, Constants.Timer.DELAY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        gameManager = GameManager(
            rows = main_IMG_obstacles.size,
            laneCount = main_IMG_players.size,
            lifeCount = main_IMG_lives.size
        )
        savedInstanceState?.let { restoreInstanceState(it) }
        initViews()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(Constants.StateKeys.PLAYER_LANE, gameManager.playerLane)
        outState.putInt(Constants.StateKeys.LIVES, gameManager.lives)
        outState.putInt(Constants.StateKeys.TICK_COUNT, gameManager.tickCount)
        outState.putIntArray(Constants.StateKeys.OBSTACLES, gameManager.obstaclesState)
    }

    private fun restoreInstanceState(savedInstanceState: Bundle) {
        val obstacles = savedInstanceState
            .getIntArray(Constants.StateKeys.OBSTACLES) ?: return
        gameManager.restore(
            lane = savedInstanceState.getInt(Constants.StateKeys.PLAYER_LANE),
            lives = savedInstanceState.getInt(Constants.StateKeys.LIVES),
            tickCount = savedInstanceState.getInt(Constants.StateKeys.TICK_COUNT),
            obstaclesState = obstacles
        )
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(gameLoop, Constants.Timer.DELAY)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(gameLoop)
    }

    private fun initViews() {
        main_FAB_left.setOnClickListener {
            gameManager.movePlayerLeft()
            refreshUI()
        }
        main_FAB_right.setOnClickListener {
            gameManager.movePlayerRight()
            refreshUI()
        }
        refreshUI()
    }

    private fun gameTick() {
        val hit = gameManager.tick()
        if (hit != null)
            crash(hit)
        refreshUI()
    }

    private fun crash(obstacle: ObstacleType) {
        showToast(getString(obstacle.messageRes))
        vibrate()
    }

    private fun showToast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private fun vibrate() {
        val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    Constants.Timer.VIBRATION,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(Constants.Timer.VIBRATION)
        }
    }

    private fun refreshUI() {
        // obstacles:
        main_IMG_obstacles.forEachIndexed { row, cells ->
            cells.forEachIndexed { lane, cell ->
                val obstacle = gameManager.getObstacle(row, lane)
                if (obstacle == null) {
                    cell.visibility = View.INVISIBLE
                } else {
                    cell.setImageResource(obstacle.imageRes)
                    cell.visibility = View.VISIBLE
                }
            }
        }

        // player:
        main_IMG_players.forEachIndexed { index, player ->
            player.visibility = if (index == gameManager.playerLane)
                View.VISIBLE
            else
                View.INVISIBLE
        }

        // lives:
        main_IMG_lives.forEachIndexed { index, life ->
            life.visibility = if (index < gameManager.lives)
                View.VISIBLE
            else
                View.INVISIBLE
        }
    }

    private fun findViews() {
        main_FAB_left = findViewById(R.id.main_FAB_left)
        main_FAB_right = findViewById(R.id.main_FAB_right)
        main_IMG_players = arrayOf(
            findViewById(R.id.main_IMG_player_0),
            findViewById(R.id.main_IMG_player_1),
            findViewById(R.id.main_IMG_player_2)
        )
        main_IMG_lives = arrayOf(
            findViewById(R.id.main_IMG_life0),
            findViewById(R.id.main_IMG_life1),
            findViewById(R.id.main_IMG_life2)
        )
        main_IMG_obstacles = arrayOf(
            arrayOf(findViewById(R.id.main_IMG_obstacle_0_0), findViewById(R.id.main_IMG_obstacle_0_1), findViewById(R.id.main_IMG_obstacle_0_2)),
            arrayOf(findViewById(R.id.main_IMG_obstacle_1_0), findViewById(R.id.main_IMG_obstacle_1_1), findViewById(R.id.main_IMG_obstacle_1_2)),
            arrayOf(findViewById(R.id.main_IMG_obstacle_2_0), findViewById(R.id.main_IMG_obstacle_2_1), findViewById(R.id.main_IMG_obstacle_2_2)),
            arrayOf(findViewById(R.id.main_IMG_obstacle_3_0), findViewById(R.id.main_IMG_obstacle_3_1), findViewById(R.id.main_IMG_obstacle_3_2)),
            arrayOf(findViewById(R.id.main_IMG_obstacle_4_0), findViewById(R.id.main_IMG_obstacle_4_1), findViewById(R.id.main_IMG_obstacle_4_2)),
            arrayOf(findViewById(R.id.main_IMG_obstacle_5_0), findViewById(R.id.main_IMG_obstacle_5_1), findViewById(R.id.main_IMG_obstacle_5_2))
        )
    }
}