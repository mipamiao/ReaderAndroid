package com.mipa.readerandroid.view.compose.base

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow

@Composable
fun AnimatedVisibilityWithCallback(
    name: String = "anonymous",
    visible: Boolean,
    onEnterStart: () -> Unit = {},
    onEnterEnd: () -> Unit= {},
    onExitStart: () -> Unit= {},
    onExitEnd: () -> Unit= {},
    enter: EnterTransition = fadeIn() + expandIn(),
    exit: ExitTransition = shrinkOut() + fadeOut(),
    content: @Composable () -> Unit
) {
    val TAG = "AnimatedVisibilityWithCallback"


    val visibleState = remember { MutableTransitionState(false) } // 初始不可见
    visibleState.targetState = visible

    var initialized by remember { mutableStateOf(false) }


    LaunchedEffect(visibleState) {
        snapshotFlow { visibleState.currentState to visibleState.targetState }
            .collect { (current, target) ->
                if (!initialized) {
                    initialized = true
                    return@collect // 第一次初始化不触发任何回调
                }
                when {
                    !current && target -> {
                        Log.d(TAG, "AnimatedVisibilityWithCallback: $name  onEnterStart")
                        onEnterStart()
                    }

                    current && target -> {
                        Log.d(TAG, "AnimatedVisibilityWithCallback: $name  onEnterEnd")
                        onEnterEnd()
                    }

                    current && !target -> {
                        Log.d(TAG, "AnimatedVisibilityWithCallback: $name  onExitStart")
                        onExitStart()
                    }

                    else -> {
                        Log.d(TAG, "AnimatedVisibilityWithCallback: $name  onExitEnd")
                        onExitEnd()
                    }
                }
            }
    }
    AnimatedVisibility(
        visibleState = visibleState,
        enter = enter,
        exit = exit
    ) {
        content()
    }
}