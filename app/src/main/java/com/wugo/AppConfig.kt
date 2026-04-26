package com.wugo

/**
 * 应用配置类
 */
object AppConfig {
    
    // 应用配置
    const val APP_NAME = "忘忧围棋"
    const val APP_VERSION = "1.0.0"
    const val BUILD_DATE = "2024-01-01"
    
    // 题库配置
    const val INITIAL_PROBLEMS = 5000 // 初始题库数量
    const val MAX_PROBLEMS = 150000 // 最大题库数量
    
    // AI配置
    const val AI_ENABLED = true // AI功能启用
    const val AI_MAX_DEPTH = 10 // AI分析深度
    
    // OCR配置
    const val OCR_ENABLED = true // OCR功能启用
    const val OCR_CONFIDENCE_THRESHOLD = 0.7 // OCR置信度阈值
    
    // 用户配置
    const val DAILY_TARGET = 10 // 每日目标题目数
    const val MAX_LEVEL = 25 // 最高级别
    
    // 棋盘配置
    const val DEFAULT_BOARD_SIZE = 19 // 默认棋盘大小
    const val BOARD_COLOR = "#5D4037" // 棋盘木纹色
    const val BOARD_LINE_COLOR = "#000000" // 棋盘线颜色
    
    // 棋子配置
    const val BLACK_STONE_COLOR = "#000000" // 黑棋颜色
    const val WHITE_STONE_COLOR = "#FFFFFF" // 白棋颜色
    const val STONE_RADIUS = 0.45f // 棋子半径（相对于网格）
    
    // 数据库配置
    const val DATABASE_NAME = "wugo-database"
    const val DATABASE_VERSION = 1
    
    // 主题配置
    const val THEME_MODE = "light" // 主题模式
    const val PRIMARY_COLOR = "#3F51B5" // 主色
    const val SECONDARY_COLOR = "#FF9800" // 副色
    
    // 音效配置
    const val SOUND_ENABLED = true // 音效启用
    const val VOLUME = 0.5f // 音量
    
    /**
     * 获取难度等级名称
     */
    fun getLevelName(level: Int): String {
        return when (level) {
            in 20..25 -> "入门级"
            in 15..20 -> "初级"
            in 10..15 -> "中级"
            in 5..10 -> "高级"
            else -> "大师级"
        }
    }
    
    /**
     * 获取难度等级颜色
     */
    fun getLevelColor(level: Int): String {
        return when (level) {
            in 20..25 -> "#4CAF50" // 初级绿色
            in 15..20 -> "#FF9800" // 中级橙色
            in 10..15 -> "#F44336" // 高级红色
            else -> "#9C27B0" // 大师紫色
        }
    }
    
    /**
     * 获取棋盘坐标转换
     */
    fun convertCoordinates(x: Int, y: Int): String {
        return "${('A' + x)}${y + 1}"
    }
    
    /**
     * 获取棋盘坐标反转
     */
    fun reverseCoordinates(coordinate: String): Pair<Int, Int> {
        val x = coordinate[0] - 'A'
        val y = coordinate.substring(1).toInt() - 1
        return Pair(x, y)
    }
}