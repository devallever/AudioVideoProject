package app.allever.android.learning.audiovideo

import app.allever.android.learning.audiovideo.audio.AudioRecordActivity
import app.allever.android.learning.audiovideo.databinding.ActivityMainBinding
import app.allever.android.learning.audiovideo.videoviewplayer.VideoViewPlayerActivity
import app.allever.android.lib.common.BaseActivity
import app.allever.android.lib.core.helper.ActivityHelper
import app.allever.android.lib.mvvm.base.BaseViewModel
import app.allever.android.lib.mvvm.base.MvvmConfig
import app.allever.android.lib.widget.ripple.RippleHelper

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun init() {
        initTopBar("音视频", showBackIcon = false)
        RippleHelper.addRippleView(binding.btnVideoViewPlayer)
        RippleHelper.addRippleView(binding.btnTextureViewPlayer)
        RippleHelper.addRippleView(binding.btnSurfaceViewPlayer)
        RippleHelper.addRippleView(binding.btnAudioRecord)

        binding.btnVideoViewPlayer.setOnClickListener {
            ActivityHelper.startActivity(SelectMediaActivity::class.java) {
                putExtra("TYPE", 0)
            }
        }

        binding.btnTextureViewPlayer.setOnClickListener {
            ActivityHelper.startActivity(SelectMediaActivity::class.java) {
                putExtra("TYPE", 1)
            }
        }

        binding.btnSurfaceViewPlayer.setOnClickListener {
            ActivityHelper.startActivity(SelectMediaActivity::class.java) {
                putExtra("TYPE", 2)
            }
        }

        binding.btnAudioRecord.setOnClickListener {
            ActivityHelper.startActivity(AudioRecordActivity::class.java) {
//                putExtra("TYPE", 2)
            }
        }

    }

    override fun getContentMvvmConfig() = MvvmConfig(R.layout.activity_main, BR.mainVM)
}

class MainViewModel: BaseViewModel() {
    override fun init() {
    }
}