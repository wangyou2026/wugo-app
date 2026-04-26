package com.wugo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ProblemDetailScreen(
    navController: NavController,
    problemId: String,
    repository: ProblemRepository
) {
    LaunchedEffect(problemId) {
        repository.setCurrentProblem(problemId)
    }

    val problem by repository.currentProblem.collectAsState()
    var userStones by remember { mutableStateOf<Map<String, Stone>>(emptyMap()) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (problem == null) {
            Text("题目加载中...")
            Button(onClick = { navController.popBackStack() }) { Text("返回") }
        } else {
            val p = problem!!
            Text(p.title, style = MaterialTheme.typography.headlineMedium)

            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                DifficultyBadge(difficulty = p.difficulty)
                Spacer(modifier = Modifier.width(8.dp))
                Text("类型: ${p.type.name}", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            val initStones = remember(p.id) {
                p.solutionSteps.associate { step ->
                    "${step.x},${step.y}" to Stone(step.x, step.y, step.color)
                }
            }

            GoBoard(
                boardSize = p.boardSize,
                stones = initStones + userStones,
                onStonePlaced = { x, y, color ->
                    val key = "$x,$y"
                    if (!initStones.containsKey(key)) {
                        userStones = userStones + (key to Stone(x, y, color))
                    }
                },
                interactive = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (p.explanation.isNotEmpty()) {
                Text("解题思路:", style = MaterialTheme.typography.titleMedium)
                Text(p.explanation, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { userStones = emptyMap() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9E9E9E))
                ) { Text("重置") }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) { Text("返回") }
            }
        }
    }
}
