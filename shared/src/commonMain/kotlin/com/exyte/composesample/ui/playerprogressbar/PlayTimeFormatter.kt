package com.exyte.composesample.ui.playerprogressbar


internal class PlayTimeFormatter {
    fun format(playbackTimeSeconds: Long): String {
        val minutes = playbackTimeSeconds / 60
        val seconds = playbackTimeSeconds % 60
        return buildString {
            if (minutes < 10) append(0)
            append(minutes)
            append(":")
            if (seconds < 10) append(0)
            append(seconds)
        }
    }
}