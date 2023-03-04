package com.example.giphyviewer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giphyviewer.GifsListAdapter
import com.example.giphyviewer.R
import com.example.giphyviewer.databinding.GiphyListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.icerock.moko.mvvm.ResourceState
import dev.icerock.moko.mvvm.utils.bindNotNull
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
            this.layoutManager = LinearLayoutManager(requireContext())
            this.adapter = gifsListAdapter
            bindReachEnd(this, gifsListAdapter)
        }

        bindToViewModel(gifsListAdapter)
    }

    private fun bindToViewModel(adapter: GifsListAdapter) {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.onRefresh {
                binding.swipeRefresh.isRefreshing = false
            }
        }

        viewModel.state.bindNotNull(this) { state ->
            bindState(state, adapter)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.action.collect {
                handleAction(it)
            }
        }
    }

    private fun bindState(
        state: ResourceState<List<GiphyListViewModel.UnitItem>, Exception>,
        gifsListAdapter: GifsListAdapter
    ) {
        binding.recyclerView.visibility = if (state.isSuccess()) View.VISIBLE else View.GONE

        binding.circularProgressIndicator.visibility =
            if (state.isLoading()) View.VISIBLE else View.GONE

        gifsListAdapter.items = state.dataValue() ?: emptyList()

        binding.recyclerView.post(Runnable { gifsListAdapter.notifyDataSetChanged() })

        binding.retryButton.visibility =
            if (state.isFailed() || state.isEmpty()) View.VISIBLE else View.GONE

        binding.retryButton.text = when (state) {
            is ResourceState.Empty -> getString(R.string.refresh)
            is ResourceState.Failed -> getString(R.string.retry)
            else -> ""
        }

        val clickListener: View.OnClickListener? = when (state) {
            is ResourceState.Empty -> View.OnClickListener { viewModel.onRefreshPressed() }
            is ResourceState.Failed -> View.OnClickListener { viewModel.onRetryPressed() }
            else -> null
        }

        binding.retryButton.setOnClickListener(clickListener)
    }

    private fun bindReachEnd(
        recyclerView: RecyclerView,
        adapter: GifsListAdapter
    ) {
        recyclerView.addOnChildAttachStateChangeListener(object :
            RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) = Unit

            override fun onChildViewAttachedToWindow(view: View) {
                val count = adapter.itemCount
                val position = recyclerView.getChildAdapterPosition(view)
                if (position != count - 1) return

                viewModel.onReachEndOfList()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleAction(action: GiphyListViewModel.Action) {
        when (action) {
            is GiphyListViewModel.Action.ShowError -> Toast.makeText(
                context,
                action.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}