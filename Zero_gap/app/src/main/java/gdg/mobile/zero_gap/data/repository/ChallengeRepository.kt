package gdg.mobile.zero_gap.data.repository

import gdg.mobile.zero_gap.R
import gdg.mobile.zero_gap.data.model.Challenge
import gdg.mobile.zero_gap.data.model.ChallengeCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ChallengeRepository {

    private val mockupChallenges = listOf(
        Challenge(
            id = "1",
            title = "코테 1문제 풀기",
            description = "뇌 풀기용 쉬운 문제 한 개만 풀어보세요.",
            iconResId = R.drawable.ic_bolt,
            category = ChallengeCategory.ACTION
        ),
        Challenge(
            id = "2",
            title = "오늘의 한 줄 기록",
            description = "지금 느끼는 감정을 딱 한 줄만 적어보세요.",
            iconResId = R.drawable.ic_check_circle,
            category = ChallengeCategory.EMOTION
        ),
        Challenge(
            id = "3",
            title = "5분 명상",
            description = "잠시 눈을 감고 호흡에 집중해보세요.",
            iconResId = R.drawable.ic_bolt,
            category = ChallengeCategory.MINDSET
        )
    )

    fun getChallenges(): Flow<List<Challenge>> = flowOf(mockupChallenges)

    fun getChallengeById(id: String): Challenge? {
        return mockupChallenges.find { it.id == id }
    }
}
