package gdg.mobile.zero_gap.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import gdg.mobile.zero_gap.databinding.FragmentReportBinding

import android.graphics.Color
import android.view.Gravity
import android.widget.TextView

class ReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCalendar()
    }

    private fun setupCalendar() {
        val grid = binding.calendarGrid
        val days = (1..28).toList() // Mockup for February
        
        // Mock data: Day to Emotion Score (1-5)
        val mockScores = mapOf(
            7 to 5,
            10 to 3,
            14 to 1,
            20 to 4,
            25 to 2
        )
        
        grid.removeAllViews()
        
        for (day in days) {
            val cellLayout = android.widget.LinearLayout(requireContext()).apply {
                orientation = android.widget.LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(0, 16, 0, 16)
                
                // Add click feedback
                stateListAnimator = android.animation.AnimatorInflater.loadStateListAnimator(
                    requireContext(), 
                    gdg.mobile.zero_gap.R.animator.click_feedback
                )
                
                setOnClickListener {
                    updateDiaryForDate(day, mockScores[day])
                }
            }

            val textView = TextView(requireContext()).apply {
                text = day.toString()
                gravity = Gravity.CENTER
                textSize = 14f
                setTextColor(if (day == 7) Color.WHITE else Color.BLACK)
                if (day == 7) {
                    setBackgroundResource(gdg.mobile.zero_gap.R.drawable.bg_stat_item)
                    backgroundTintList = android.content.res.ColorStateList.valueOf(
                        requireContext().getColor(gdg.mobile.zero_gap.R.color.indigo_600)
                    )
                }
            }
            
            cellLayout.addView(textView)

            // Add emotion icon if score exists
            mockScores[day]?.let { score ->
                val iconView = android.widget.ImageView(requireContext()).apply {
                    val iconRes = when (score) {
                        1 -> gdg.mobile.zero_gap.R.drawable.ic_emotion_1
                        2 -> gdg.mobile.zero_gap.R.drawable.ic_emotion_2
                        3 -> gdg.mobile.zero_gap.R.drawable.ic_emotion_3
                        4 -> gdg.mobile.zero_gap.R.drawable.ic_emotion_4
                        5 -> gdg.mobile.zero_gap.R.drawable.ic_emotion_5
                        else -> gdg.mobile.zero_gap.R.drawable.ic_emotion_3
                    }
                    setImageResource(iconRes)
                    val size = (24 * resources.displayMetrics.density).toInt()
                    layoutParams = android.widget.LinearLayout.LayoutParams(size, size).apply {
                        topMargin = (4 * resources.displayMetrics.density).toInt()
                    }
                    // Apply tint
                    imageTintList = android.content.res.ColorStateList.valueOf(
                        requireContext().getColor(gdg.mobile.zero_gap.R.color.indigo_500)
                    )
                }
                cellLayout.addView(iconView)
            }
            
            val params = android.widget.GridLayout.LayoutParams().apply {
                width = 0
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                columnSpec = android.widget.GridLayout.spec(android.widget.GridLayout.UNDEFINED, 1f)
            }
            grid.addView(cellLayout, params)
        }
    }

    private fun updateDiaryForDate(day: Int, score: Int? = null) {
        binding.tvSelectedDate.text = "2026.02.${String.format("%02d", day)}"
        
        // Update Icon
        if (score != null) {
            binding.ivDiaryEmotion.visibility = View.VISIBLE
            val iconRes = when (score) {
                1 -> gdg.mobile.zero_gap.R.drawable.ic_emotion_1
                2 -> gdg.mobile.zero_gap.R.drawable.ic_emotion_2
                3 -> gdg.mobile.zero_gap.R.drawable.ic_emotion_3
                4 -> gdg.mobile.zero_gap.R.drawable.ic_emotion_4
                5 -> gdg.mobile.zero_gap.R.drawable.ic_emotion_5
                else -> gdg.mobile.zero_gap.R.drawable.ic_emotion_3
            }
            binding.ivDiaryEmotion.setImageResource(iconRes)
            binding.ivDiaryEmotion.imageTintList = android.content.res.ColorStateList.valueOf(
                requireContext().getColor(gdg.mobile.zero_gap.R.color.indigo_600)
            )
        } else {
            binding.ivDiaryEmotion.visibility = View.GONE
        }

        if (day == 7) {
            binding.tvDiaryQuote.text = "\"조금 늦어도 괜찮아요. 당신의 계절은 반드시 올 거예요.\""
            binding.tvDiaryContent.text = "오늘은 코테 문제를 풀었다. 생각보다 잘 풀려서 기분이 좋았다. 내일도 오늘처럼만 했으면 좋겠다."
        } else {
            binding.tvDiaryQuote.text = if (score != null) "\"오늘 하루도 수고 많으셨습니다.\"" else "\"도전한 것 자체가 큰 발걸음입니다.\""
            binding.tvDiaryContent.text = if (score != null) "기록된 일기가 여기에 표시됩니다. 감정 점수: ${score}점" else "기록이 없습니다. 새로운 감정을 채워보세요."
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
