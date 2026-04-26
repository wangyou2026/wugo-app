package com.wugo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 围棋主题颜色（修复：所有颜色值补全 alpha 通道 0xFF）
val GoThemeColors = GoColors()

data class GoColors(
    // 棋盘颜色
    val boardWood: Color = Color(0xFF5D4037),
    val boardLine: Color = Color.Black,
    val starPoint: Color = Color.Black,

    // 棋子颜色
    val stoneBlack: Color = Color.Black,
    val stoneWhite: Color = Color.White,
    val stoneWhiteBorder: Color = Color(0xFF5D4037),

    // 界面颜色
    val primary: Color = Color(0xFF3F51B5),
    val secondary: Color = Color(0xFFFF9800),
    val background: Color = Color(0xFFF5F5F5),
    val surface: Color = Color.White,

    // 等级颜色
    val beginner: Color = Color(0xFF4CAF50),
    val intermediate: Color = Color(0xFFFF9800),
    val advanced: Color = Color(0xFFF44336),
    val master: Color = Color(0xFF9C27B0),

    // 状态颜色
    val success: Color = Color(0xFF4CAF50),
    val warning: Color = Color(0xFFFF9800),
    val error: Color = Color(0xFFF44336),
    val info: Color = Color(0xFF2196F3),

    // 文字颜色
    val textPrimary: Color = Color.Black,
    val textSecondary: Color = Color(0xFF757575),
    val textInverse: Color = Color.White
)

// 围棋主题
object GoTheme {
    val colors = GoColors()

    val typography = Typography(
        headlineLarge = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = colors.textPrimary
        ),
        headlineMedium = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            color = colors.textPrimary
        ),
        headlineSmall = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = colors.textPrimary
        ),
        titleLarge = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = colors.textPrimary
        ),
        titleMedium = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = colors.textPrimary
        ),
        titleSmall = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = colors.textPrimary
        ),
        bodyLarge = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = colors.textPrimary
        ),
        bodyMedium = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = colors.textPrimary
        ),
        bodySmall = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = colors.textSecondary
        ),
        labelLarge = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = colors.textPrimary
        ),
        labelMedium = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = colors.textPrimary
        ),
        labelSmall = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            color = colors.textSecondary
        )
    )

    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(8.dp),
        large = RoundedCornerShape(12.dp)
    )
}

// 围棋专用组件
@Composable
fun GoCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = GoTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = GoTheme.colors.surface),
        onClick = onClick
    ) {
        content()
    }
}

@Composable
fun GoButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    variant: ButtonVariant = ButtonVariant.Primary,
    enabled: Boolean = true
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = getButtonColor(variant)),
        shape = GoTheme.shapes.medium,
        enabled = enabled
    ) {
        Text(text)
    }
}

enum class ButtonVariant {
    Primary, Secondary, Success, Warning, Error
}

fun getButtonColor(variant: ButtonVariant): Color {
    return when (variant) {
        ButtonVariant.Primary -> GoTheme.colors.primary
        ButtonVariant.Secondary -> GoTheme.colors.secondary
        ButtonVariant.Success -> GoTheme.colors.success
        ButtonVariant.Warning -> GoTheme.colors.warning
        ButtonVariant.Error -> GoTheme.colors.error
    }
}

@Composable
fun DifficultyBadge(difficulty: Int) {
    val difficultyColor = getDifficultyColor(difficulty)
    val difficultyName = getDifficultyName(difficulty)

    Box(
        modifier = Modifier
            .background(difficultyColor, GoTheme.shapes.small)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = difficultyName,
            style = GoTheme.typography.labelMedium,
            color = Color.White
        )
    }
}

fun getDifficultyColor(difficulty: Int): Color {
    return when (difficulty) {
        in 20..25 -> GoTheme.colors.beginner
        in 15..20 -> GoTheme.colors.intermediate
        in 10..15 -> GoTheme.colors.advanced
        else -> GoTheme.colors.master
    }
}

fun getDifficultyName(difficulty: Int): String {
    return when (difficulty) {
        in 20..25 -> "入门级"
        in 15..20 -> "初级"
        in 10..15 -> "中级"
        in 5..10 -> "高级"
        else -> "大师级"
    }
}

@Composable
fun ProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = GoTheme.colors.success
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(Color.LightGray, RoundedCornerShape(4.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .height(8.dp)
                .background(color, RoundedCornerShape(4.dp))
        )
    }
}
