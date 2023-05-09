package com.ebatashota.coroutineasync.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ebatashota.coroutineasync.databinding.FragmentButtonAndLogBinding
import com.ebatashota.coroutineasync.databinding.ViewButtonBinding
import com.ebatashota.coroutineasync.viewmodel.ErrorSampleViewModel

class ErrorSampleFragment : Fragment() {

    private val viewModel: ErrorSampleViewModel by viewModels()

    private var _binding: FragmentButtonAndLogBinding? = null
    private val binding: FragmentButtonAndLogBinding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentButtonAndLogBinding.inflate(inflater, container, false)

        initButtons(inflater)
        viewModel.logTextLiveData.observe(viewLifecycleOwner) {
            binding.logTextView.text = it
        }
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initButtons(inflater: LayoutInflater) {
        viewModel.types.forEach { myTask ->
            addButton(inflater, label = myTask.typeName, onClick = myTask::run)
        }
    }

    private fun addButton(
        inflater: LayoutInflater,
        label: String,
        onClick: () -> Unit
    ) {
        val buttonViewBinding = ViewButtonBinding.inflate(inflater, binding.layoutButtonsHolder, false)
        buttonViewBinding.button.text = label
        buttonViewBinding.button.setOnClickListener {
            onClick.invoke()
        }
        binding.layoutButtonsHolder.addView(buttonViewBinding.root)
    }
}