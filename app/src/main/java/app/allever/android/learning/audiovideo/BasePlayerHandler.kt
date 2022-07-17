package app.allever.android.learning.audiovideo

import android.media.MediaPlayer
import app.allever.android.lib.core.function.media.MediaBean
import app.allever.android.lib.core.function.work.TimerTask2

abstract class BasePlayerHandler:  MediaPlayer.OnCompletionListener{

    protected lateinit var mMediaPlayer: MediaPlayer
    protected lateinit var mMediaBean: MediaBean
    protected var mStatusListener: StatusListener? = null

    private val timerTask = TimerTask2(1000L, true) {
        mStatusListener?.onVideoPlaying(mMediaPlayer.currentPosition)
    }

    fun isPlaying() = mMediaPlayer.isPlaying

    fun getMediaPlayer() = mMediaPlayer

    open fun play() {
        mStatusListener?.onVideoPlay()
        timerTask.start()
    }

    open fun pause() {
        timerTask.cancel()
        mStatusListener?.onVideoPause()
    }

    open fun stop() {
        timerTask.cancel()
        mStatusListener?.onVideoPause()
    }

    open fun seekTo(value: Int) {
    }

    override fun onCompletion(mp: MediaPlayer?) {
        mStatusListener?.onVideoError()
    }
}