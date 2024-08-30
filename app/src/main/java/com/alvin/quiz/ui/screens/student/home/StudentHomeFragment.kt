package com.alvin.quiz.ui.screens.student.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvin.quiz.R
import com.alvin.quiz.core.service.StorageService
import com.alvin.quiz.data.model.StudentQuizCompletion
import com.alvin.quiz.databinding.FragmentStudentHomeBinding
import com.alvin.quiz.ui.adapter.StudentQuizCompletionAdapter
import com.alvin.quiz.ui.screens.base.BaseFragment
import com.alvin.quiz.ui.screens.student.home.StudentHomeFragmentDirections
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class StudentHomeFragment : BaseFragment<FragmentStudentHomeBinding>() {
    @Inject
    lateinit var storageService: StorageService
    override val viewModel: StudentHomeViewModel by viewModels()
    private lateinit var studentQuizCompletionAdapter: StudentQuizCompletionAdapter
    override fun getLayoutResource() = R.layout.fragment_student_home

    override fun onBindView(view: View) {
        super.onBindView(view)
        studentQuizCompletionAdapter = StudentQuizCompletionAdapter()
        viewModel.loadCompletions()
        viewModel.completions.observe(viewLifecycleOwner) { completions ->
            if (completions.isNotEmpty()) {
                setupRankingViews(completions)
            } else {
                binding?.tvNoRanking?.visibility = View.VISIBLE
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                if (isLoading) {
                    binding?.loadingOverlay?.isVisible = true
                    loading()
                } else {
                    binding?.loadingOverlay?.isVisible = false
                }
            }
        }

        binding?.btnAccessQuiz?.setOnClickListener {
            showAccessDialog()
        }
    }

    private fun showAccessDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.layout_alert_entry_quiz_authentication, null)
        val etAccessId = dialogView.findViewById<EditText>(R.id.etAccessId)

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton(getString(R.string.access)) { _, _ ->
                val accessId = etAccessId.text.toString()
                verifyAccessDetails(accessId)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun verifyAccessDetails(accessId: String) {
        lifecycleScope.launch {
            val studentId = viewModel.getCurrentUserId()
            val quizId = viewModel.verifyQuizAccess(accessId)
            if (quizId != null) {
                val hasCompleted = viewModel.checkResult(quizId, studentId)
                if (!hasCompleted) {
                    val action = StudentHomeFragmentDirections.actionStudentHomeToQuizView(quizId)
                    findNavController().navigate(action)
                } else {
                    Snackbar.make(requireView(), getString(R.string.completed_quiz), Snackbar.LENGTH_LONG).show()
                }
            } else {
                Snackbar.make(requireView(), getString(R.string.invalid_details), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun loading() {
        binding?.loadingOverlay?.isVisible = true
        val tvLoadingText = binding?.tvLoadingText

        lifecycleScope.launch {
            var progress = 0
            while (progress < 100) {
                val randomIncrement = Random.nextInt(1, 15)
                progress += randomIncrement

                if (progress > 100) {
                    progress = 100
                }

                tvLoadingText?.text = getString(R.string.verifying, progress)
                delay(Random.nextLong(50, 250))
            }
            binding?.loadingOverlay?.isVisible = false
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
            Glide.with(this@StudentHomeFragment)
                .load(uri)
                .placeholder(R.drawable.ic_person)
                .centerCrop()
                .into(imageView)
        }
    }
}
