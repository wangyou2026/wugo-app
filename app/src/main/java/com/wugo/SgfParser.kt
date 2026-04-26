package com.wugo

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

class SgfParser {

    companion object {

        fun parseSgfContent(content: String): Problem {
            val stones = mutableMapOf<String, Stone>()
            var difficulty = 15
            var problemType = ProblemType.DEAD_ALIVE
            var title = "未知题目"
            var explanation = ""

            for (line in content.split("\n")) {
                when {
                    line.contains("AB[") -> parseBlackStones(line, stones)
                    line.contains("AW[") -> parseWhiteStones(line, stones)
                    line.contains("DI[") -> difficulty = parseDifficulty(line)
                    line.contains("TY[") -> problemType = parseProblemType(line)
                    line.contains("TI[") -> title = parseTitle(line)
                    line.contains("EX[") -> explanation = parseExplanation(line)
                }
            }

            return Problem(
                id = "prob_" + content.hashCode().toString(),
                title = title,
                difficulty = difficulty,
                type = problemType,
                boardSize = 19,
                sgfContent = content,
                solutionSteps = parseSolutionSteps(content),
                explanation = explanation
            )
        }

        private fun parseBlackStones(line: String, stones: MutableMap<String, Stone>) {
            val stonePattern = Regex("AB\\[(..?)\\]")
            stonePattern.findAll(line).forEach { match ->
                val coord = match.groupValues[1]
                if (coord.length >= 2) {
                    val x = coord[0] - 'a'
                    val y = coord[1] - 'a'
                    if (x in 0..18 && y in 0..18)
                        stones["$x,$y"] = Stone(x, y, StoneColor.BLACK)
                }
            }
        }

        private fun parseWhiteStones(line: String, stones: MutableMap<String, Stone>) {
            val stonePattern = Regex("AW\\[(..?)\\]")
            stonePattern.findAll(line).forEach { match ->
                val coord = match.groupValues[1]
                if (coord.length >= 2) {
                    val x = coord[0] - 'a'
                    val y = coord[1] - 'a'
                    if (x in 0..18 && y in 0..18)
                        stones["$x,$y"] = Stone(x, y, StoneColor.WHITE)
                }
            }
        }

        private fun parseDifficulty(line: String): Int {
            return Regex("DI\\[(\\d+)\\]").find(line)?.groupValues?.get(1)?.toIntOrNull() ?: 15
        }

        private fun parseProblemType(line: String): ProblemType {
            val typeStr = Regex("TY\\[([A-Z_]+)\\]").find(line)?.groupValues?.get(1) ?: "DEAD_ALIVE"
            return when (typeStr) {
                "DEAD_ALIVE" -> ProblemType.DEAD_ALIVE
                "TSUMEGO"    -> ProblemType.TSUMEGO
                "YOSE"       -> ProblemType.YOSE
                "JOSEKI"     -> ProblemType.JOSEKI
                "MIDDLE"     -> ProblemType.MIDDLE
                else         -> ProblemType.DEAD_ALIVE
            }
        }

        private fun parseTitle(line: String): String {
            return Regex("TI\\[([^\\]]+)\\]").find(line)?.groupValues?.get(1) ?: "未知题目"
        }

        private fun parseExplanation(line: String): String {
            return Regex("EX\\[([^\\]]+)\\]").find(line)?.groupValues?.get(1) ?: ""
        }

        private fun parseSolutionSteps(content: String): List<SolutionStep> {
            val steps = mutableListOf<SolutionStep>()
            val pattern = Regex("[;](B|W)\\[([a-s]{2})\\]")
            pattern.findAll(content).forEach { match ->
                val color = if (match.groupValues[1] == "B") StoneColor.BLACK else StoneColor.WHITE
                val coord = match.groupValues[2]
                if (coord.length == 2) {
                    val x = coord[0] - 'a'
                    val y = coord[1] - 'a'
                    if (x in 0..18 && y in 0..18)
                        steps.add(SolutionStep(x, y, color))
                }
            }
            return steps
        }

        fun loadProblemsFromAssets(context: Context): List<Problem> {
            val problems = mutableListOf<Problem>()
            try {
                val assets = context.assets.list("sgf_problems") ?: return problems
                assets.filter { it.endsWith(".sgf") }.forEach { filename ->
                    try {
                        context.assets.open("sgf_problems/$filename").use { stream ->
                            val content = BufferedReader(InputStreamReader(stream)).readText()
                            problems.add(parseSgfContent(content))
                        }
                    } catch (e: Exception) {
                        Log.w("SgfParser", "解析 $filename 失败: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e("SgfParser", "加载题库失败: ${e.message}")
            }
            return problems
        }
    }
}

data class SolutionStep(
    val x: Int,
    val y: Int,
    val color: StoneColor
)

enum class ProblemType {
    DEAD_ALIVE, TSUMEGO, YOSE, JOSEKI, MIDDLE
}
