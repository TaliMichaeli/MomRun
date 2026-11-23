package com.example.momrun.model

import androidx.annotation.DrawableRes
import com.example.momrun.R

enum class ObstacleType(@DrawableRes val imageRes: Int) {
    POOP(R.drawable.poop),
    DIAPER(R.drawable.diaper),
    TOY(R.drawable.baby_toy)
}