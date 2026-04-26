# 围棋做题App Android项目结构

## 项目概述
项目名称：Wugo（忘忧围棋）
版本：MVP 1.0
定位：专业级单机围棋做题App

## 项目结构
```
WugoApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com.wugo/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── GoBoardView.kt
│   │   │   │   ├── ProblemRepository.kt
│   │   │   │   ├── DatabaseHelper.kt
│   │   │   │   ├── SgfParser.kt
│   │   │   │   └── KataGoEngine.kt
│   │   │   ├── resources/
│   │   │   │   ├── sgf_problems/
│   │   │   │   ├── assets/
│   │   │   │   └── ...
│   │   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
├── libs/
│   ├── kata-go/
│   └── ...
├── gradle/
└── README.md
```

## 核心功能模块
1. **棋盘渲染引擎** - Canvas绘制棋盘与棋子
2. **题库管理系统** - SGF解析与分类
3. **用户进度系统** - Room数据库存储
4. **关卡系统** - 难度分级与解锁
5. **本地AI引擎** - KataGo轻量化集成
6. **拍照识题** - ML Kit OCR识别棋盘

## 技术选型
- 开发语言：Kotlin
- UI框架：Jetpack Compose
- 数据库：Room
- AI引擎：KataGo（Android NNAPI优化版本）
- OCR：ML Kit On-device Text Recognition
- 棋盘渲染：Canvas + 自定义View

## 开发计划
### 第1周：项目初始化与棋盘基础
- 创建Android项目结构
- 实现基础棋盘Canvas渲染
- 棋子点击交互逻辑

### 第2-3周：题库系统
- SGF解析库集成
- 基础题库加载（5000题）
- 题目分类与难度标注

### 第4周：闯关模式
- 关卡系统设计
- 解锁逻辑实现
- 进度追踪

### 第5周：数据库与进度
- Room数据库设置
- 用户进度存储
- 做题历史记录

### 第6周：UI界面完善
- Compose界面设计
- 导航结构实现
- 视觉效果优化

### 第7周：测试与优化
- 功能测试
- 性能优化
- bug修复

### 第8周：发布准备
- APK打包
- 发布到测试平台
- 用户反馈收集