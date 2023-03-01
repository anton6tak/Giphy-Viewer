package com.example.giphyviewer.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.giphyviewer.databinding.GiphyListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GiphyListFragment : Fragment() {
    private var _binding: GiphyListFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<GiphyListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GiphyListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            viewModel.test()
        }

        viewModel.title.observe(viewLifecycleOwner) {
            binding.message.text = it
            print(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}