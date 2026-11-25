package com.example.momrun.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.momrun.R

enum class ObstacleType(
    @DrawableRes val imageRes: Int,
    @StringRes val messageRes: Int
) {
    POOP(R.drawable.poop, R.string.hit_poop),
    DIAPER(R.drawable.diaper, R.string.hit_diaper),
    TOY(R.drawable.baby_toy, R.string.hit_toy)
}