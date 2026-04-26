package com.wugo

import android.content.Context
import android.util.Log

/**
 * 数据库操作（占位实现）
 * 完整版需集成 Room Database
 */
class DatabaseHelper(context: Context) {

    fun saveProblems(problems: List<Problem>) {
        Log.d("DatabaseHelper", "保存 ${problems.size} 道题目（内存存储）")
    }

    fun loadProblems(): List<Problem> {
        return emptyList()
    }

    fun saveUserProgress(progress: UserProgress) {
        Log.d("DatabaseHelper", "保存用户进度: level=${progress.currentLevel}")
    }

    fun loadUserProgress(): UserProgress? {
        return null
    }
}

/**
 * 简化的数据库初始化帮助类
 */
object AppDatabaseHelper {

    fun loadProblemsFromAssets(context: Context): List<Problem> {
        return SgfParser.loadProblemsFromAssets(context)
    }
}
