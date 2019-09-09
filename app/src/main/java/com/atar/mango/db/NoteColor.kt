package com.atar.mango.db

import com.atar.mango.R

enum class NoteColor(val id: Int, val value: Int) {
    DEFAULT(0, R.color.white),
    RED(1, R.color.red_accent_100),
    PINK(2, R.color.pink_accent_100),
    PURPLE(3, R.color.purple_accent_100),
    PURPLE_DEEP(4, R.color.purple_deep_accent_100),
    INDIGO(5, R.color.indigo_accent_100),
    BLUE(6, R.color.blue_accent_100),
    LIGHT_BLUE(7, R.color.light_blue_accent_100),
    CYAN(8, R.color.cyan_accent_100),
    TEAL(9, R.color.teal_accent_100),
    GREEN(10, R.color.green_accent_100),
    LIGHT_GREEN(11, R.color.light_green_accent_100),
    LIME(12, R.color.lime_accent_100),
    YELLOW(13, R.color.yellow_accent_100),
    AMBER(14, R.color.amber_accent_100),
    ORANGE(15, R.color.orange_accent_100),
    DEEP_ORANGE(16, R.color.deep_orange_accent_100),
}