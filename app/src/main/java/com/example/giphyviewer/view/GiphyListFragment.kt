package com.example.giphyviewer.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giphyviewer.GifsListAdapter
import com.example.giphyviewer.databinding.GiphyListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gifsListAdapter = GifsListAdapter(requireContext())
        with(binding.recyclerView) {
            this.adapter = gifsListAdapter
            this.layoutManager = LinearLayoutManager(requireContext())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect {
                handleState(it, gifsListAdapter)
            }
        }

    }

    fun handleState(
        state: GiphyListViewModel.State,
        adapter: GifsListAdapter
    ) {
        binding.recyclerView.visibility =
            if (state is GiphyListViewModel.State.Success) View.VISIBLE else View.GONE

        binding.circularProgressIndicator.visibility =
            if (state == GiphyListViewModel.State.Loading) View.VISIBLE else View.GONE

        adapter.items = if (state is GiphyListViewModel.State.Success) {
            state.data.take(3)
        } else {
            emptyList()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}