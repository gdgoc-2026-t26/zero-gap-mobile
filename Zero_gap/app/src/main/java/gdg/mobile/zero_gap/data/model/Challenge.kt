package gdg.mobile.zero_gap.data.model

data class Challenge(
    val id: String,
    val title: String,
    val description: String,
    val iconResId: Int, // Placeholder for icon resource
    val category: ChallengeCategory,
    val defaultDurations: List<Int> = listOf(5, 10, 30, 60) // in minutes
)

enum class ChallengeCategory {
    MINDSET, ACTION, EMOTION
}
