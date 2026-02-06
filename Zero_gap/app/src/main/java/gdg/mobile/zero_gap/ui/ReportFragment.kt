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

import androidx.lifecycle.lifecycleScope
import gdg.mobile.zero_gap.data.network.NetworkClient
import gdg.mobile.zero_gap.data.model.EmotionResponse
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    private var monthlyEmotions: Map<Int, EmotionResponse> = emptyMap()

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
        fetchData()
    }

    private fun fetchData() {
        // Mocking February 2026 range
        val startDate = "2026-02-01"
        val endDate = "2026-02-28"
        
        lifecycleScope.launch {
            try {
                val response = NetworkClient.apiService.getEmotions(startDate, endDate)
                monthlyEmotions = response.emotions.associateBy { dto ->
                    // Extract day from YYYY-MM-DD
                    dto.date.split("-").last().toInt()
                }
                setupCalendar()
            } catch (e: Exception) {
                // Handle error
                setupCalendar() // Still setup with defaults
            }
        }
    }

    private fun setupCalendar() {
        val grid = binding.calendarGrid
        val days = (1..28).toList() // Mockup for February
        
        grid.removeAllViews()
        
        for (day in days) {
            val emotion = monthlyEmotions[day]
            val score = emotion?.score
            
            val cellLayout = android.widget.LinearLayout(requireContext()).apply {
                orientation = android.widget.LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(0, 16, 0, 16)
                
                stateListAnimator = android.animation.AnimatorInflater.loadStateListAnimator(
                    requireContext(), 
                    gdg.mobile.zero_gap.R.animator.click_feedback
                )
                
                setOnClickListener {
                    updateDiaryForDate(day, emotion)
                }
            }

            val textView = TextView(requireContext()).apply {
                text = day.toString()
                gravity = Gravity.CENTER
                textSize = 14f
                setTextColor(Color.BLACK)
            }
            
            cellLayout.addView(textView)

            // Add emotion icon if score exists
            score?.let { s ->
                val iconView = android.widget.ImageView(requireContext()).apply {
                    val iconRes = when (s) {
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

    private fun updateDiaryForDate(day: Int, emotion: EmotionResponse? = null) {
        binding.tvSelectedDate.text = "2026.02.${String.format("%02d", day)}"
        
        if (emotion != null) {
            binding.ivDiaryEmotion.visibility = View.VISIBLE
            val iconRes = when (emotion.score) {
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
            binding.tvDiaryQuote.text = "\"기록은 당신의 성장을 증명합니다.\""
            binding.tvDiaryContent.text = emotion.description
        } else {
            binding.ivDiaryEmotion.visibility = View.GONE
            binding.tvDiaryQuote.text = "\"기록이 없는 날은 새로운 시작을 준비하는 날입니다.\""
            binding.tvDiaryContent.text = "기록이 없습니다. 새로운 감정을 채워보세요."
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
