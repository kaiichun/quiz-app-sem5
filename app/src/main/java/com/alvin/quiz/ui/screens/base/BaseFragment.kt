package com.alvin.quiz.ui.screens.base

import com.alvin.quiz.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

abstract class BaseFragment<T: ViewBinding>: Fragment() {
    protected abstract val viewModel: BaseViewModel
    protected var binding: T? = null
    protected abstract fun getLayoutResource(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutResource(), container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBindView(view)
        onBindData(view)
    }

    protected open fun onBindView(view: View) {
        binding = DataBindingUtil.bind(view)
    }

    protected open fun onBindData(view: View) {
        lifecycleScope.launch {
            viewModel.error.collect {
                showSnackBar(view, it, true)
            }
        }
    }

    protected fun showSnackBar(view: View, message: String, isError: Boolean = false) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        if (isError) {
            snackBar.setBackgroundTint(
                ContextCompat.getColor(requireContext(), R.color.red)
            )
        }
        snackBar.show()
    }
}