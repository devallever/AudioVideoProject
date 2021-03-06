package app.allever.android.learning.audiovideo.surfaceviewplayer

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import app.allever.android.learning.audiovideo.BasePlayerView
import app.allever.android.learning.audiovideo.R
import app.allever.android.learning.audiovideo.StatusListener
import app.allever.android.learning.audiovideo.databinding.SurfacePlayerViewBinding
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.toast
import app.allever.android.lib.core.function.media.MediaBean
import app.allever.android.lib.core.helper.DisplayHelper
import app.allever.android.lib.core.helper.ViewHelper
import app.allever.android.lib.core.util.TimeUtils
import kotlin.math.abs
import kotlin.math.ceil

class SurfacePlayerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : BasePlayerView(context, attrs), StatusListener {
    private var binding: SurfacePlayerViewBinding
    private val playerHandler: SurfaceViewHandler by lazy {
        SurfaceViewHandler()
    }

    init {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.surface_player_view,
            this,
            true
        )

        initListener()
    }

    private fun initListener() {
        binding.ivBack.setOnClickListener {
            toast("finish")
            (context as? Activity)?.finish()
        }
        binding.ivPlayPause.setOnClickListener {
            if (playerHandler.isPlaying()) {
                playerHandler.pause()
            } else {
                playerHandler.play()
            }
        }
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) {
                    playerHandler.seekTo(p1)
                }
                binding.tvProgress.text = TimeUtils.formatTime(p1.toLong(), TimeUtils.FORMAT_mm_ss)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        binding.ivRotate.setOnClickListener {
            val tag = if (binding.ivRotate.tag == null) false else binding.ivRotate.tag as Boolean
            (context as Activity).requestedOrientation =
                if (!tag) ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            binding.ivRotate.tag = !tag
        }

        binding.controlView.setOnClickListener {
            val visible = binding.controlContainer.visibility == View.VISIBLE
            ViewHelper.setVisible(binding.controlContainer, !visible)
            ViewHelper.setVisible(binding.topBarContainer, !visible)
        }

        val screenWidth = DisplayHelper.getScreenWidth()
        val screenHeight = DisplayHelper.getScreenHeight()
        val leftSide = screenWidth / 8
        val rightSide = screenWidth / 8 * 7
        val bottomSide = screenHeight / 3 * 2
        var moved = false
        var downX = 0f
        var downY = 0f
        var lastRealDownX = 0f
        binding.controlView.setOnTouchListener { view, motionEvent ->
            val screenWidth = DisplayHelper.getScreenWidth()
            val screenHeight = DisplayHelper.getScreenHeight()
            val leftSide = screenWidth / 8
            val rightSide = screenWidth / 8 * 7
            val bottomSide = screenHeight / 3 * 2
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = motionEvent.rawX
                    downY = motionEvent.rawY
                    lastRealDownX = downX
                    moved = false
                }
                MotionEvent.ACTION_UP -> {
                    if (!moved) {
//                        toast("?????????")
                        binding.controlView.performClick()
                    } else {
//                        toast("?????????")
                        playerHandler.play()
                    }
                    moved = false
                }
                MotionEvent.ACTION_MOVE -> {
                    val rawX = motionEvent.rawX
                    val rawY = motionEvent.rawY
                    val offsetY = abs(rawY - downY)
                    val offsetX = abs(rawX - downX)
                    val realOffsetX = rawX - lastRealDownX
                    val maxOffset = 50
                    if (offsetX > maxOffset || offsetY > maxOffset) {
                        moved = true
                    }
                    log("rawXY: (${motionEvent.rawX}, ${motionEvent.rawY})")
                    log("XY: (${motionEvent.x}, ${motionEvent.y})")
                    when {
                        rawX < leftSide -> {
                            log("????????????")
                        }
                        rawX > rightSide -> {
                            log("????????????")
                        }
                        rawY > bottomSide -> {
                            log("????????????")

                            if (moved) {
                                playerHandler.pause()
                                val currentPosition =
                                    binding.seekBar.max * realOffsetX / screenWidth.toFloat()
                                val progress = binding.seekBar.progress + currentPosition.toInt()
                                binding.seekBar.progress = progress
                                playerHandler.seekTo(progress)
                                log(" progress = $progress")
                            }
                        }
                        else -> {
                        }
                    }
                    lastRealDownX = rawX

                }
                MotionEvent.ACTION_CANCEL -> {
                    moved = false
                }
            }

            return@setOnTouchListener true
        }
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        playerHandler.stop()
    }

    fun setData(mediaBean: MediaBean) {
        mMediaBean = mediaBean
        playerHandler.initVideoView(binding.videoView, mediaBean, this)
        binding.tvTitle.text = mMediaBean.name
        changeVideoSize()
    }

    override fun onPrepare(duration: Int) {
        binding.seekBar.max = duration
        val text = TimeUtils.formatTime(duration.toLong(), TimeUtils.FORMAT_mm_ss)
        binding.tvDuration.text = " / $text"
    }

    override fun onVideoPlay() {
        binding.ivPlayPause.setImageResource(app.allever.android.lib.widget.R.drawable.icon_album_video_preview_pause)
    }

    override fun onVideoPause() {
        binding.ivPlayPause.setImageResource(app.allever.android.lib.widget.R.drawable.icon_album_video_preview_play)
    }

    override fun onVideoError() {
        binding.ivPlayPause.setImageResource(app.allever.android.lib.widget.R.drawable.icon_album_video_preview_play)
    }

    override fun onVideoPlaying(currentPosition: Int) {
        binding.seekBar.progress = currentPosition
        binding.tvProgress.text =
            TimeUtils.formatTime(currentPosition.toLong(), TimeUtils.FORMAT_mm_ss)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        changeVideoSize()
    }

    //?????????????????????????????????
    private fun changeVideoSize() {
        binding.controlView.post {
            val w: Float = mMediaBean.width.toFloat()
            val h: Float = mMediaBean.height.toFloat()
            val sw: Float = binding.controlView.width.toFloat()
            val sh: Float = binding.controlView.height.toFloat()

            var displayW = 0
            var displayH = 0

            log("video size = $w x $h")
            log("surface size = $sw x $sh")

            if (resources.configuration.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                log("??????")
                if (w > h) {
                    //????????????
                    displayW = sw.toInt()
                    displayH = (h * sw / w).toInt()
                } else {
                    //????????????
                    if (h > sh) {
                        // ????????????
                    } else {
                        //
                        displayW = sw.toInt()
                        displayH = (h * sw / w).toInt()
                    }
                }

            } else {
                log("??????")
                if (w > h) {
                    //????????????
                    if (w > sw) {
                        //????????????
                    } else {
                        //
                        displayH = sh.toInt()
                        displayW = (w * sh / h).toInt()
                    }
                } else {
                    //????????????
                    displayH = sh.toInt()
                    displayW = (w * sh / h).toInt()
                }
            }


            log("surface size = $displayW x $displayH")

            //?????????????????????????????????????????????????????????????????????surfaceView ????????????????????????
            val params = binding.videoView.layoutParams
            params.width = displayW
            params.height = displayH
            binding.videoView.layoutParams = params
        }

    }
}