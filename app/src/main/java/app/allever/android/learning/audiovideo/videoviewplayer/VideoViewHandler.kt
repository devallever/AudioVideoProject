package app.allever.android.learning.audiovideo.videoviewplayer

import android.media.MediaPlayer
import android.net.Uri
import android.widget.VideoView
import app.allever.android.lib.core.function.media.MediaBean

class VideoViewHandler : MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var videoView: VideoView

    fun initVideoView(videoView: VideoView, mediaBean: MediaBean) {
        this.videoView = videoView
        videoView.setOnCompletionListener(this)
        //处理开始播放时的短暂黑屏
        videoView.setOnPreparedListener(this)
        videoView.setVideoURI(mediaBean.uri)
    }

    override fun onCompletion(mediaPlayer: MediaPlayer) {

    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        this.mediaPlayer = mediaPlayer
    }

    fun play() {
        videoView.start()
    }

    fun stop() {
        videoView.pause()
        mediaPlayer.release()
    }
}