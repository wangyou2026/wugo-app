package com.wugo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow

// ─── 首页 ─────────────────────────────────────────────────────────────────────
@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "忘忧围棋",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF5D4037)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "单机围棋死活题库",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFF3F51B5)
        )

        Spacer(modifier = Modifier.height(32.dp))

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(0.8f),
            onClick = { navController.navigate("practice") }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("每日练习", style = MaterialTheme.typography.titleMedium)
                Text("每日推荐10题，巩固棋力", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(0.8f),
            onClick = { navController.navigate("levels") }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("闯关模式", style = MaterialTheme.typography.titleMedium)
                Text("从入门到大师，逐步提升", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(0.8f),
            onClick = { navController.navigate("stats") }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("数据统计", style = MaterialTheme.typography.titleMedium)
                Text("查看你的棋力进步曲线", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("settings") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
        ) {
            Text("设置")
        }
    }
}

// ─── 每日练习 ──────────────────────────────────────────────────────────────────
@Composable
fun PracticeScreen(navController: NavController, repository: ProblemRepository) {
    val problems by repository.problems.collectAsState()
    val dailyProblems = remember(problems) { repository.getDailyProblems() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("每日练习", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (dailyProblems.isEmpty()) {
            Text("暂无题目，请先加载题库")
            Button(onClick = { navController.navigate("levels") }) {
                Text("前往题库")
            }
        } else {
            LazyColumn {
                items(dailyProblems) { problem ->
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        onClick = { navController.navigate("problem/${problem.id}") }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(problem.title, style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.weight(1f))
                                Text("难度: ${problem.difficulty}", color = Color.Gray)
                            }
                            Text(problem.type.name, style = MaterialTheme.typography.bodyMedium)
                            if (problem.explanation.isNotEmpty()) {
                                Text(problem.explanation, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─── 题库浏览 ──────────────────────────────────────────────────────────────────
@Composable
fun LevelsScreen(navController: NavController, repository: ProblemRepository) {
    val problems by repository.problems.collectAsState()
    val categories = remember(problems) { problems.groupBy { it.category } }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("题库分类", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (problems.isEmpty()) {
            Text("题库为空，请检查 assets/sgf_problems/ 目录")
        } else {
            LazyColumn {
                items(categories.keys.toList()) { category ->
                    val categoryProblems = categories[category] ?: emptyList()
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        onClick = {
                            // 进入分类，随机选第一题
                            categoryProblems.firstOrNull()?.let {
                                navController.navigate("problem/${it.id}")
                            }
                        }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(category, style = MaterialTheme.typography.titleMedium)
                            Text("${categoryProblems.size} 题", style = MaterialTheme.typography.bodyMedium)
                            if (categoryProblems.isNotEmpty()) {
                                val minD = categoryProblems.minOf { it.difficulty }
                                val maxD = categoryProblems.maxOf { it.difficulty }
                                Text("难度范围: $minD - $maxD",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF3F51B5))
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─── 统计 ──────────────────────────────────────────────────────────────────────
@Composable
fun StatsScreen(navController: NavController, repository: ProblemRepository) {
    val userProgress by repository.userProgress.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("棋力统计", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (userProgress == null) {
            Text("暂无统计数据，开始做题吧！")
        } else {
            val progress = userProgress!!
            val levelName = if (progress.currentLevel in learningLevels.indices)
                learningLevels[progress.currentLevel] else "未知"

            Row(modifier = Modifier.fillMaxWidth()) {
                Text("当前等级: ", style = MaterialTheme.typography.titleMedium)
                Text(levelName, style = MaterialTheme.typography.titleMedium, color = Color(0xFF4CAF50))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("做题统计", style = MaterialTheme.typography.titleMedium)
                    Text("总做题数: ${progress.totalProblemsSolved}")
                    Text("正确率: ${(progress.correctRate * 100).toInt()}%")
                    Text("连续打卡: ${progress.dailyStreak} 天")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate("practice") }) {
                Text("继续做题")
            }
        }
    }
}

// ─── 做题页面 ─────────────────────────────────────────────────────────────────
@Composable
fun ProblemScreen(
    navController: NavController,
    problemId: String,
    repository: ProblemRepository
) {
    // 触发加载当前题目
    LaunchedEffect(problemId) {
        repository.setCurrentProblem(problemId)
    }

    val problem by repository.currentProblem.collectAsState()
    var userStones by remember { mutableStateOf<Map<String, Stone>>(emptyMap()) }
    var resultMessage by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (problem == null) {
            Text("题目加载中...")
            Button(onClick = { navController.popBackStack() }) {
                Text("返回")
            }
        } else {
            val p = problem!!

            Text(p.title, style = MaterialTheme.typography.headlineMedium)

            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                DifficultyBadge(difficulty = p.difficulty)
                Spacer(modifier = Modifier.width(8.dp))
                Text("类型: ${p.type.name}", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 棋盘
            val initStones = remember(p.id) {
                p.solutionSteps.associate { step ->
                    "${step.x},${step.y}" to Stone(step.x, step.y, step.color)
                }
            }
            val displayStones = remember(initStones, userStones) {
                initStones + userStones
            }

            GoBoard(
                boardSize = p.boardSize,
                stones = displayStones,
                onStonePlaced = { x, y, color ->
                    val key = "$x,$y"
                    if (!initStones.containsKey(key)) {
                        userStones = userStones + (key to Stone(x, y, color))
                    }
                },
                interactive = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (resultMessage.isNotEmpty()) {
                Text(
                    text = resultMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (resultMessage.startsWith("✓")) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            if (p.explanation.isNotEmpty()) {
                Text("解题思路:", style = MaterialTheme.typography.titleMedium)
                Text(p.explanation, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // 按钮行
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        userStones = emptyMap()
                        resultMessage = ""
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9E9E9E))
                ) {
                    Text("重置")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        resultMessage = "✓ 继续加油！"
                        repository.updateUserProgress(true, p.id)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("答对了")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
                ) {
                    Text("返回")
                }
            }
        }
    }
}

val learningLevels = listOf(
    "25级", "24级", "23级", "22级", "21级",
    "20级", "19级", "18级", "17级", "16级",
    "15级", "14级", "13级", "12级", "11级",
    "10级", "9级", "8级", "7级", "6级",
    "5级", "4级", "3级", "2级", "1级",
    "初段", "二段", "三段", "四段", "五段",
    "六段", "七段", "八段", "九段"
)
