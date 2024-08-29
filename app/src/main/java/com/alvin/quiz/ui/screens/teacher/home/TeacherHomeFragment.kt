package com.alvin.quiz.ui.screens.teacher.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvin.quiz.R
import com.alvin.quiz.core.service.StorageService
import com.alvin.quiz.data.model.Quiz
import com.alvin.quiz.data.model.StudentQuizCompletion
import com.alvin.quiz.databinding.FragmentRegisterBinding
import com.alvin.quiz.databinding.FragmentTeacherHomeBinding
import com.alvin.quiz.ui.adapter.QuizAdapter
import com.alvin.quiz.ui.adapter.QuizStatusAdapter
import com.alvin.quiz.ui.adapter.StudentQuizCompletionAdapter
import com.alvin.quiz.ui.screens.base.BaseFragment
import com.alvin.quiz.ui.screens.register.RegisterViewModel
import com.alvin.quiz.ui.screens.student.home.StudentHomeViewModel
import com.alvin.quiz.ui.screens.teacher.dashboard.TeacherDashboardFragmentDirections
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherHomeFragment : BaseFragment<FragmentTeacherHomeBinding>() {
    override val viewModel: TeacherHomeViewModel by viewModels()
    lateinit var storageService: StorageService
    private lateinit var studentQuizCompletionAdapter: StudentQuizCompletionAdapter
    override fun getLayoutResource() = R.layout.fragment_teacher_home
    private lateinit var attendQuizAdapter: QuizStatusAdapter
    private lateinit var noAttendQuizAdapter: QuizStatusAdapter

    override fun onBindView(view: View) {
        super.onBindView(view)
        setupAttendQuizAdapter()
        setupNoAttendQuizAdapter()

    }


    private fun setupAttendQuizAdapter() {
        attendQuizAdapter = QuizStatusAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        binding?.rvQuizHasAttend?.adapter = attendQuizAdapter
        binding?.rvQuizHasAttend?.layoutManager = layoutManager
        attendQuizAdapter.listener = object : QuizStatusAdapter.Listener {
            override fun onClick(quiz: Quiz) {
                quiz.quizId.let {
                    findNavController().navigate(
                        TeacherHomeFragmentDirections.actionTeacherHomeToCheckQuizView(it)
                    )
                }
            }
        }
    }
    private fun setupNoAttendQuizAdapter() {
        noAttendQuizAdapter = QuizStatusAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        binding?.rvQuizNoAttend?.adapter = attendQuizAdapter
        binding?.rvQuizNoAttend?.layoutManager = layoutManager
        noAttendQuizAdapter.listener = object : QuizStatusAdapter.Listener {
            override fun onClick(quiz: Quiz) {
                quiz.quizId.let {
                    findNavController().navigate(
                        TeacherHomeFragmentDirections.actionTeacherHomeToCheckQuizView(it)
                    )
                }
            }
        }
    }

    private fun setupRankingViews(completions: List<StudentQuizCompletion>) {
        Log.d("StudentHomeFragment", "Completions received: ${completions.size}")

        val sortedCompletions = completions.sortedByDescending { it.totalScore }
        val top3 = sortedCompletions.take(3)
        val fourToTen = sortedCompletions.drop(3)

        Log.d("StudentHomeFragment", "Top 3: $top3")
        Log.d("StudentHomeFragment", "Remaining: $fourToTen")

        if (top3.isNotEmpty()) {
            binding?.top3Container?.visibility = View.VISIBLE
            binding?.tvNoRanking?.visibility = View.GONE

            when (top3.size) {
                1 -> {
                    setupFirstPlace(top3[0])
                    hideSecondAndThirdPlace()
                }
                2 -> {
                    setupFirstPlace(top3[0])
                    setupSecondPlace(top3[1])
                    hideThirdPlace()
                }
                else -> {
                    setupFirstPlace(top3[0])
                    setupSecondPlace(top3[1])
                    setupThirdPlace(top3[2])
                }
            }
        } else {
            binding?.top3Container?.visibility = View.GONE
            binding?.tvNoRanking?.visibility = View.VISIBLE
        }

        if (fourToTen.isNotEmpty()) {
            binding?.rvRankings?.visibility = View.VISIBLE
            studentQuizCompletionAdapter.submitList(fourToTen)
            binding?.rvRankings?.adapter = studentQuizCompletionAdapter
            binding?.rvRankings?.layoutManager = LinearLayoutManager(requireContext())
        } else {
            binding?.rvRankings?.visibility = View.GONE
        }
    }

    private fun setupFirstPlace(completion: StudentQuizCompletion) {
        binding?.ivFirstPlace?.visibility = View.VISIBLE
        binding?.tvFirstPlaceName?.visibility = View.VISIBLE
        binding?.tvFirstPlaceScore?.visibility = View.VISIBLE
        binding?.tvFirstPlaceName?.text = "${completion.firstName} ${completion.lastName}"
        binding?.tvFirstPlaceScore?.text = completion.totalScore.toString()
        showProfileImage(binding?.ivFirstPlace!!, completion.profilePicture)
    }

    private fun setupSecondPlace(completion: StudentQuizCompletion) {
        binding?.ivSecondPlace?.visibility = View.VISIBLE
        binding?.tvSecondPlaceName?.visibility = View.VISIBLE
        binding?.tvSecondPlaceScore?.visibility = View.VISIBLE
        binding?.tvSecondPlaceName?.text = "${completion.firstName} ${completion.lastName}"
        binding?.tvSecondPlaceScore?.text = completion.totalScore.toString()
        showProfileImage(binding?.ivSecondPlace!!, completion.profilePicture)
    }

    private fun setupThirdPlace(completion: StudentQuizCompletion) {
        binding?.ivThirdPlace?.visibility = View.VISIBLE
        binding?.tvThirdPlaceName?.visibility = View.VISIBLE
        binding?.tvThirdPlaceScore?.visibility = View.VISIBLE
        binding?.tvThirdPlaceName?.text = "${completion.firstName} ${completion.lastName}"
        binding?.tvThirdPlaceScore?.text = completion.totalScore.toString()
        showProfileImage(binding?.ivThirdPlace!!, completion.profilePicture)
    }

    private fun hideSecondAndThirdPlace() {
        binding?.ivSecondPlace?.visibility = View.GONE
        binding?.tvSecondPlaceName?.visibility = View.GONE
        binding?.tvSecondPlaceScore?.visibility = View.GONE
        binding?.ivThirdPlace?.visibility = View.GONE
        binding?.tvThirdPlaceName?.visibility = View.GONE
        binding?.tvThirdPlaceScore?.visibility = View.GONE
    }

    private fun hideThirdPlace() {
        binding?.ivThirdPlace?.visibility = View.GONE
        binding?.tvThirdPlaceName?.visibility = View.GONE
        binding?.tvThirdPlaceScore?.visibility = View.GONE
    }

    private fun showProfileImage(imageView: ImageView, imageName: String?) {
        if (imageName.isNullOrEmpty()) return
        storageService.getImageUri(imageName) { uri ->
            Glide.with(this@TeacherHomeFragment)
                .load(uri)
                .placeholder(R.drawable.ic_person)
                .centerCrop()
                .into(imageView)
        }
    }
}