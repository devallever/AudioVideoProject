package app.allever.android.learning.audiovideo

import app.allever.android.learning.audiovideo.databinding.ActivityMainBinding
import app.allever.android.lib.common.BaseActivity
import app.allever.android.lib.mvvm.base.BaseViewModel
import app.allever.android.lib.mvvm.base.MvvmConfig
import app.allever.android.lib.widget.recycler.RefreshRecyclerView

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun init() {
        initTopBar("音视频", showBackIcon = false)
    }

    override fun getContentMvvmConfig() = MvvmConfig(R.layout.activity_main, BR.mainVM)
}

class MainViewModel: BaseViewModel() {
    override fun init() {
    }
}