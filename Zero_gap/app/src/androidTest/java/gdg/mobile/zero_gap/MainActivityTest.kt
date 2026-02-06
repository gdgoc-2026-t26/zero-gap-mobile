package gdg.mobile.zero_gap

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainActivity_isDisplayed() {
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }

    @Test
    fun topAppBar_elementsAreDisplayed() {
        onView(withId(R.id.topAppBar)).check(matches(isDisplayed()))
        onView(withId(R.id.btnNotifications)).check(matches(isDisplayed()))
        onView(withId(R.id.imgProfile)).check(matches(isDisplayed()))
    }

    @Test
    fun bottomNavigation_isDisplayed() {
        onView(withId(R.id.bottomNavigation)).check(matches(isDisplayed()))
    }

    @Test
    fun achievementCard_isDisplayed() {
        onView(withId(R.id.cardAchievementSummary)).check(matches(isDisplayed()))
        onView(withId(R.id.tvAchievementTitle)).check(matches(isDisplayed()))
    }

    @Test
    fun reengagementAlert_isDisplayed() {
        onView(withId(R.id.alertReengagement)).check(matches(isDisplayed()))
        onView(withId(R.id.tvAlertTitle)).check(matches(isDisplayed()))
    }

    @Test
    fun challengeList_isDisplayed() {
        onView(withId(R.id.tvChallengeHeader)).check(matches(isDisplayed()))
        onView(withId(R.id.tvChallenge1)).check(matches(isDisplayed()))
        onView(withId(R.id.tvChallenge2)).check(matches(isDisplayed()))
    }

    @Test
    fun emotionalReport_isDisplayed() {
        onView(withId(R.id.cardEmotionalReport)).check(matches(isDisplayed()))
        onView(withId(R.id.tvReportTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.btnRecordEmotion)).check(matches(isDisplayed()))
    }
}
