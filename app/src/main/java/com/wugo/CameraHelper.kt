package com.wugo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberCameraHelper(): CameraHelper {
    val context = LocalContext.current
    return remember { CameraHelper(context) }
}

class CameraHelper(private val context: Context) {

    fun loadBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream)
            }
        } catch (e: Exception) {
            null
        }
    }

    fun preprocessBitmap(bitmap: Bitmap): Bitmap = bitmap

    suspend fun captureAndScan(): OCRResult? = null
}
