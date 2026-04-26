package com.wugo

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Problem(
    val id: String,
    val title: String,
    val difficulty: Int,
    val type: ProblemType,
    val boardSize: Int,
    val sgfContent: String,
    val solutionSteps: List<SolutionStep>,
    val explanation: String,
    val category: String = ""
)

class ProblemRepository : ViewModel() {

    private val _problems = MutableStateFlow<List<Problem>>(emptyList())
    val problems: StateFlow<List<Problem>> = _problems.asStateFlow()

    private val _currentProblem = MutableStateFlow<Problem?>(null)
    val currentProblem: StateFlow<Problem?> = _currentProblem.asStateFlow()

    private val _userProgress = MutableStateFlow<UserProgress?>(null)
    val userProgress: StateFlow<UserProgress?> = _userProgress.asStateFlow()

    fun loadProblems(context: Context) {
        viewModelScope.launch {
            val loadedProblems = SgfParser.loadProblemsFromAssets(context)
            _problems.value = categorizeProblems(loadedProblems)
        }
    }

    private fun categorizeProblems(list: List<Problem>): List<Problem> {
        return list.map { problem ->
            problem.copy(category = when (problem.difficulty) {
                in 20..25 -> "入门级"
                in 15..19 -> "初级"
                in 10..14 -> "中级"
                in 5..9  -> "高级"
                else -> "大师级"
            })
        }
    }

    fun setCurrentProblem(problemId: String) {
        _currentProblem.value = _problems.value.find { it.id == problemId }
    }

    fun getRandomProblem(difficulty: Int? = null): Problem? {
        val filtered = if (difficulty != null)
            _problems.value.filter { it.difficulty == difficulty }
        else
            _problems.value
        return filtered.randomOrNull()
    }

    /** 返回今日推荐题目列表（非 Flow，直接取当前值） */
    fun getDailyProblems(): List<Problem> {
        val userLevel = _userProgress.value?.currentLevel ?: 25
        val difficultyRange = when {
            userLevel >= 20 -> 15..20
            userLevel >= 15 -> 10..15
            userLevel >= 10 -> 5..10
            else -> 1..5
        }
        return _problems.value
            .filter { it.difficulty in difficultyRange }
            .shuffled()
            .take(10)
    }

    fun updateUserProgress(isCorrect: Boolean, problemId: String) {
        val current = _userProgress.value ?: UserProgress()
        val totalSolved = current.totalProblemsSolved + 1
        val previousCorrect = current.correctRate * current.totalProblemsSolved
        val newCorrect = if (isCorrect) previousCorrect + 1 else previousCorrect
        val newRate = newCorrect / totalSolved

        val stats = current.problemStatistics.toMutableMap()
        val existing = stats[problemId] ?: ProblemStat()
        stats[problemId] = existing.copy(
            attemptedCount = existing.attemptedCount + 1,
            correctCount = if (isCorrect) existing.correctCount + 1 else existing.correctCount
        )

        val newProgress = current.copy(
            totalProblemsSolved = totalSolved,
            correctRate = newRate,
            problemStatistics = stats
        )
        val newLevel = calculateLevel(newRate, totalSolved)
        _userProgress.value = newProgress.copy(currentLevel = newLevel)
    }

    private fun calculateLevel(correctRate: Float, totalSolved: Int): Int {
        val levelsGained = totalSolved / 50
        val rateBonus = if (correctRate >= 0.7f) 1 else 0
        return maxOf(1, 25 - levelsGained - rateBonus)
    }
}

data class UserProgress(
    val userId: String = "default",
    val totalProblemsSolved: Int = 0,
    val correctRate: Float = 0f,
    val currentLevel: Int = 25,
    val dailyStreak: Int = 0,
    val lastPracticeDate: Long = System.currentTimeMillis(),
    val problemStatistics: Map<String, ProblemStat> = emptyMap()
)

data class ProblemStat(
    val attemptedCount: Int = 0,
    val correctCount: Int = 0
)
