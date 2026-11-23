package com.example.momrun.logic

class GameManager(private val laneCount: Int = 3) {
    var playerLane: Int = laneCount / 2
        private set

    fun movePlayerLeft() {
        if (playerLane > 0)
            playerLane--
    }

    fun movePlayerRight() {
        if (playerLane < laneCount - 1)
            playerLane++
    }
}