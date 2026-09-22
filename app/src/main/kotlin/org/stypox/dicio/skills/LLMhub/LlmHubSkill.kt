package org.stypox.dicio.skills.llmhub

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import org.dicio.skill.context.SkillContext
import org.dicio.skill.skill.FloatScore
import org.dicio.skill.skill.Score
import org.dicio.skill.skill.Skill
import org.dicio.skill.skill.SkillInfo
import org.dicio.skill.skill.SkillOutput
import org.dicio.skill.skill.Specificity
import org.stypox.dicio.io.graphical.Headline

// ================= SkillInfo =================
object LlmHubInfo : SkillInfo("llmhub_vibevoice") {
    override fun name(context: Context) = "LLM Hub"
    override fun sentenceExample(context: Context) = "Open LLM Hub"

    @Composable
    override fun icon() = rememberVectorPainter(Icons.Filled.Mic)

    override fun build(ctx: SkillContext): Skill<*>? {
        return LlmHubSkill(LlmHubInfo)
    }
}

// ================= Skill =================
class LlmHubSkill(correspondingSkillInfo: SkillInfo) :
    Skill<Boolean>(correspondingSkillInfo, Specificity.HIGH) {

    // هر عبارتی که می‌خوای باهاش فعال بشه رو اینجا اضافه کن
    private val triggerPhrases = listOf(
        "open llm hub",
        "start llm hub",
        "open vibevoice",
        "باز کن ال ال ام هاب",
        "ال ال ام هاب رو باز کن",
        "دستیار رو باز کن",
    )

    override fun score(ctx: SkillContext, input: String): Pair<Score, Boolean> {
        val normalized = input.trim().lowercase()
        val matched = triggerPhrases.any { normalized.contains(it.lowercase()) }
        return Pair(
            if (matched) FloatScore(1.0f) else FloatScore(0.0f),
            matched
        )
    }

    override suspend fun generateOutput(ctx: SkillContext, inputData: Boolean): SkillOutput {
        if (inputData) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("llmhub://vibevoice"))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            ctx.android.startActivity(intent)
        }
        return LlmHubOutput(inputData)
    }
}

// ================= SkillOutput =================
class LlmHubOutput(private val success: Boolean) : SkillOutput {
    override fun getSpeechOutput(ctx: SkillContext): String {
        return if (success) "در حال باز کردن LLM Hub" else "متوجه نشدم"
    }

    @Composable
    override fun GraphicalOutput(ctx: SkillContext) {
        Headline(text = getSpeechOutput(ctx))
    }
}