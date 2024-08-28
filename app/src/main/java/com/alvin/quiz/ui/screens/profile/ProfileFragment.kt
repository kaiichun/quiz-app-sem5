package com.alvin.quiz.ui.screens.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.alvin.quiz.R
import com.alvin.quiz.core.service.StorageService
import com.alvin.quiz.databinding.FragmentProfileBinding
import com.alvin.quiz.ui.screens.base.BaseFragment
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override val viewModel: ProfileViewModel by viewModels()
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private var imageName: String? = null
    @Inject
    lateinit var storageService: StorageService
    override fun getLayoutResource() = R.layout.fragment_profile
    override fun onBindView(view: View) {
        super.onBindView(view)
        imagePickerLauncher =registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {
            it?.let {uri ->
                val newName = if(imageName == null || imageName == "null") null else imageName
                binding?.ivProfileImage?.setImageURI(uri)
                storageService.uploadImage(uri = it, newName) {name ->
                    if(name != null) {
                        viewModel.updateUserProfile(name)
                        imageName = name
                    }
                }
            }
        }

        binding?.ivProfileUpload?.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding?.loadingOverlay?.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.finish.collect {
                Snackbar.make(view, "Profile Image update successfully", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.darkGreen))
                    .show()
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        viewModel.user.observe(viewLifecycleOwner) {
            imageName = it.profilePicture
            binding?.run {
                tvCurrentUserName.text = "${it.firstName} ${it.lastName}"
                tvCurrentEmail.text = it.email
                showProfileImage(ivProfileImage, imageName)
            }
        }
    }

    private fun showProfileImage(imageView: ImageView, name: String?) {
        if (name.isNullOrEmpty()) return
        storageService.getImageUri(name) {
            Glide.with(imageView)
                .load(it)
                .placeholder(R.drawable.ic_person)
                .centerCrop()
                .into(imageView)
        }
    }

}