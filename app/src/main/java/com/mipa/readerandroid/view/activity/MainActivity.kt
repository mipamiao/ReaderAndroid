package com.mipa.readerandroid.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.mipa.model.UserProfileViewModel
import com.mipa.readerandroid.base.AbsActivity
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.service.UserService
import com.mipa.readerandroid.ui.theme.ReaderAndroidTheme
import com.mipa.readerandroid.view.compose.MainNavigation
import com.mipa.readerandroid.view.composedata.MePageCD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity :AbsActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReaderAndroidTheme {
                MainNavigation()
            }
        }
        val status = getStatusBarHeight()
        Log.e("TAG", "onCreate: $status", )

        tryLoadUserProfile()
    }

    private fun tryLoadUserProfile() {
        lifecycleScope.launch {
            var res = withContext(Dispatchers.IO) {
                UserService.loadUserProfile()
            }
            res?.let {
                CDMap.get<MePageCD>().updateUserProfile(res)
            }
        }
    }

}


fun Context.getStatusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else {
        0
    }
}



