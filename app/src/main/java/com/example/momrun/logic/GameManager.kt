package com.example.momrun.logic

import com.example.momrun.model.ObstacleType
import com.example.momrun.utilities.Constants
import kotlin.random.Random

class GameManager(
    private val rows: Int,
    private val laneCount: Int,
    private val lifeCount: Int
) {

    var playerLane: Int = laneCount / 2
        private set

    var lives: Int = lifeCount
        private set

    var tickCount: Int = 0
        private set

    private val obstacles: Array<Array<ObstacleType?>> =
        Array(rows) { arrayOfNulls<ObstacleType>(laneCount) }

    val obstaclesState: IntArray
        get() = IntArray(rows * laneCount) { i ->
            obstacles[i / laneCount][i % laneCount]?.ordinal ?: -1
        }

    fun getObstacle(row: Int, lane: Int): ObstacleType? = obstacles[row][lane]

    fun movePlayerLeft() {
        if (playerLane > 0)
            playerLane--
    }

    fun movePlayerRight() {
        if (playerLane < laneCount - 1)
            playerLane++
    }

    fun tick(): ObstacleType? {
        // 1. collision: obstacle in the last row, in the player's lane?
        val hit = obstacles[rows - 1][playerLane]
        if (hit != null)
            loseLife()

        // 2. move every row one step down:
        for (row in rows - 1 downTo 1)
            obstacles[row] = obstacles[row - 1]
        obstacles[0] = arrayOfNulls(laneCount)

        // 3. spawn a new obstacle in a random lane:
        if (tickCount % Constants.GameLogic.SPAWN_EVERY == 0)
            obstacles[0][Random.nextInt(laneCount)] = ObstacleType.entries.random()

        tickCount++
        return hit
    }

    fun restore(lane: Int, lives: Int, tickCount: Int, obstaclesState: IntArray) {
        if (obstaclesState.size != rows * laneCount)
            return // grid size has changed, keep new game data
        this.playerLane = lane.coerceIn(0, laneCount - 1)
        this.lives = lives.coerceIn(1, lifeCount)
        this.tickCount = tickCount
        obstaclesState.forEachIndexed { i, type ->
            obstacles[i / laneCount][i % laneCount] = ObstacleType.entries.getOrNull(type)
        }
    }

    private fun loseLife() {
        lives--
        if (lives == 0) // endless game:
            lives = lifeCount
    }
}