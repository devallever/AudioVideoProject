package app.allever.android.learning.audiovideo

import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.function.datastore.DataStore
import app.allever.android.lib.core.function.imageloader.ImageLoader
import app.allever.android.lib.core.function.imageloader.coil.CoilLoader
import app.allever.android.lib.core.function.permission.DefaultPermissionEngine
import app.allever.android.lib.core.function.permission.PermissionHelper
import app.allever.android.lib.imageloader.glide.GlideLoader
import app.allever.android.lib.widget.Widget

class MyApp: App() {
    override fun init() {
        ImageLoader.init(this, GlideLoader, ImageLoader.Builder.create())
        PermissionHelper.init(DefaultPermissionEngine)
        Widget.init(this)
    }
}