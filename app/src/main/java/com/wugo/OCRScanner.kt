package com.wugo

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * OCR 拍照识题模块（占位实现）
 * 完整实现需集成 Google ML Kit
 */
class OCRScanner(context: Context) {

    private val _scanningProgress = androidx.compose.runtime.mutableStateOf(false)
    val scanningProgress: androidx.compose.runtime.MutableState<Boolean> = _scanningProgress

    suspend fun scanImage(bitmap: Bitmap): OCRResult {
        return withContext(Dispatchers.IO) {
            _scanningProgress.value = true
            // TODO: 接入 ML Kit Text Recognition
            try {
                OCRResult(
                    success = false,
                    sgfContent = "",
                    message = "OCR 功能待实现",
                    boardDetection = BoardDetection(
                        isBoardDetected = false,
                        gridSize = 0,
                        confidence = 0f
                    )
                )
            } finally {
                _scanningProgress.value = false
            }
        }
    }

    suspend fun saveToProblemRepository(sgfContent: String, context: Context) {
        Log.d("OCRScanner", "保存 SGF: $sgfContent")
    }
}

data class OCRResult(
    val success: Boolean,
    val sgfContent: String,
    val message: String,
    val boardDetection: BoardDetection
)

data class BoardDetection(
    val isBoardDetected: Boolean,
    val gridSize: Int,
    val confidence: Float
)
