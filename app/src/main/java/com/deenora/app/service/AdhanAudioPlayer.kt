package com.deenora.app.service

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.deenora.app.R
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Plays the bundled adhan MP3 offline. The worker remains foreground until the
 * recording finishes, while the notification action can stop it immediately.
 */
object AdhanAudioPlayer {
    private val lock = Any()
    private var player: MediaPlayer? = null
    private var completion: CancellableContinuation<Unit>? = null

    suspend fun playAndWait(context: Context) = suspendCancellableCoroutine<Unit> { continuation ->
        val mediaPlayer = MediaPlayer.create(context.applicationContext, R.raw.adhan)
        if (mediaPlayer == null) {
            continuation.resume(Unit)
            return@suspendCancellableCoroutine
        }

        synchronized(lock) {
            releaseLocked()
            player = mediaPlayer
            completion = continuation
        }

        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
        mediaPlayer.setOnCompletionListener { finish(it) }
        mediaPlayer.setOnErrorListener { mp, _, _ ->
            finish(mp)
            true
        }
        continuation.invokeOnCancellation { stop() }

        try {
            mediaPlayer.start()
        } catch (_: IllegalStateException) {
            finish(mediaPlayer)
        }
    }

    fun stop() {
        val continuation: CancellableContinuation<Unit>?
        synchronized(lock) {
            val current = player
            player = null
            continuation = completion
            completion = null
            safelyRelease(current)
        }
        if (continuation?.isActive == true) continuation.resume(Unit)
    }

    private fun finish(mediaPlayer: MediaPlayer) {
        val continuation: CancellableContinuation<Unit>?
        synchronized(lock) {
            if (player !== mediaPlayer) return
            player = null
            continuation = completion
            completion = null
            safelyRelease(mediaPlayer)
        }
        if (continuation?.isActive == true) continuation.resume(Unit)
    }

    private fun releaseLocked() {
        safelyRelease(player)
        player = null
        completion = null
    }

    private fun safelyRelease(mediaPlayer: MediaPlayer?) {
        if (mediaPlayer == null) return
        try {
            mediaPlayer.setOnCompletionListener(null)
            mediaPlayer.setOnErrorListener(null)
            if (mediaPlayer.isPlaying) mediaPlayer.stop()
        } catch (_: IllegalStateException) {
        } finally {
            try { mediaPlayer.release() } catch (_: Exception) { }
        }
    }
}
