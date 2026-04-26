package com.wugo

import android.app.Application

/**
 * 忘忧围棋应用入口
 */
class WugoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 应用初始化（数据加载通过 ViewModel 延迟加载）
    }
}
