package com.example.tvshowsapplication.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tvshowsapplication.Adapters.ShowAdapter
import com.example.tvshowsapplication.MainActivity
import com.example.tvshowsapplication.R
import com.example.tvshowsapplication.Utils.Constants.Companion.DELAY_TIME_SEARCH
import com.example.tvshowsapplication.ViewModel.ShowViewModel
import com.example.tvshowsapplication.databinding.FragmentSearchBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class SearchFragment : Fragment(), ShowAdapter.onClickItemListner {


    private lateinit var binding: FragmentSearchBinding
    private lateinit var mSearchShowAdapter: ShowAdapter
    private lateinit var showViewModel: ShowViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showViewModel = (activity as MainActivity).viewModel

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearLayoutManager = LinearLayoutManager(requireContext())
        mSearchShowAdapter = ShowAdapter(this)
        observerSearchShow()
        searchShow()

        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun observerSearchShow() {
        showViewModel.observerSearchShowLiveData().observe(viewLifecycleOwner) { it ->
            mSearchShowAdapter.differ.submitList(it)
            binding.rcvSearchFragment.apply {
                layoutManager = linearLayoutManager
                adapter = mSearchShowAdapter
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
        }
    }

    private fun searchShow() {
        var searchJob: Job? = null
        binding.edtSearch.addTextChangedListener {
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(DELAY_TIME_SEARCH)
                showViewModel.searchShow(it.toString())
            }
        }
    }

    override fun onClickItem(position: Int) {
        val bundle = Bundle().apply {
            putInt("idShow", mSearchShowAdapter.differ.currentList[position].id!!)
            putSerializable("tvShow", mSearchShowAdapter.differ.currentList[position])
        }
        findNavController().navigate(R.id.action_searchFragment_to_deatilShowsFragment, bundle)
    }

}