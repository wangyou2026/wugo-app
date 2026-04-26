package com.wugo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SettingsScreen(navController: NavController) {
    var aiEnabled by remember { mutableStateOf(true) }
    var ocrEnabled by remember { mutableStateOf(true) }
    var themeMode by remember { mutableStateOf("light") }
    var language by remember { mutableStateOf("zh") }
    var dailyGoal by remember { mutableStateOf(10) }
    var soundEnabled by remember { mutableStateOf(true) }
    
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("设置", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // AI分析设置
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("AI棋局分析", modifier = Modifier.weight(1f))
            Switch(
                checked = aiEnabled,
                onCheckedChange = { aiEnabled = it }
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // OCR拍照设置
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("OCR拍照识题", modifier = Modifier.weight(1f))
            Switch(
                checked = ocrEnabled,
                onCheckedChange = { ocrEnabled = it }
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 主题模式
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("主题模式", modifier = Modifier.weight(1f))
            Text(themeMode, color = MaterialTheme.colorScheme.primary)
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 语言设置
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("语言", modifier = Modifier.weight(1f))
            Text(language, color = MaterialTheme.colorScheme.primary)
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 每日目标
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("每日目标题目数", modifier = Modifier.weight(1f))
            Text("$dailyGoal题", color = MaterialTheme.colorScheme.primary)
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 声音设置
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("落子声音", modifier = Modifier.weight(1f))
            Switch(
                checked = soundEnabled,
                onCheckedChange = { soundEnabled = it }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 清空数据
        Button(
            onClick = {
                // TODO: 清空用户进度数据
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("清空学习进度")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 导出数据
        Button(
            onClick = {
                // TODO: 导出学习数据
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("导出学习数据")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 关于我们
        Button(
            onClick = {
                // TODO: 显示关于信息
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("关于忘忧围棋")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 返回主页
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("返回主页")
        }
    }
}