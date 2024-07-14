package com.example.tvshowsapplication.Fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.newsapplication.adapters.PaginationScrollListener
import com.example.tvshowsapplication.Adapters.ShowAdapter
import com.example.tvshowsapplication.MainActivity
import com.example.tvshowsapplication.R
import com.example.tvshowsapplication.models.TvShow
import com.example.tvshowsapplication.ViewModel.ShowViewModel
import com.example.tvshowsapplication.databinding.FragmentShowsBinding
import de.hdodenhof.circleimageview.CircleImageView


class ShowsFragment : Fragment(), ShowAdapter.onClickItemListner {

    private lateinit var binding: FragmentShowsBinding
    private lateinit var showViewModel: ShowViewModel
    private lateinit var mShowAdapter: ShowAdapter
    private var listShowPopular = mutableListOf<TvShow>()
    private var currentPage = 1

    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var totalPage = 7;


    companion object {
        @JvmStatic
        @BindingAdapter("android:imageURL")
        fun setImageURL(view: CircleImageView, url: String) {
            Glide.with(view.context).load(url).into(view)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showViewModel = (activity as MainActivity).viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(activity)
        mShowAdapter = ShowAdapter(this)
        prepareRecyclerView(linearLayoutManager)

        binding.rcvShows.addOnScrollListener(object :
            PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {
                isLoading = true

                binding.isLoadingMore = true

                currentPage += 1
                loadNextPage()
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

        })
    }

    private fun loadNextPage() {
        Handler(Looper.getMainLooper()).postDelayed({
            showViewModel.getPopularShow(currentPage)
            isLoading = false
            binding.isLoadingMore = false
            if (currentPage == totalPage) {
                isLastPage = true
            }
        }, 2000)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowsBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun prepareRecyclerView(linearLayoutManager: LinearLayoutManager) {
        binding.rcvShows.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mShowAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        showViewModel.getPopularShow(currentPage)
        observerShowPopular()
    }

    private fun observerShowPopular() {
        binding.isLoading = true
        showViewModel.observerShowPopularLiveData().observe(viewLifecycleOwner) { it ->
            binding.isLoading = false
            this.listShowPopular.addAll(it)
            mShowAdapter.differ.submitList(this.listShowPopular)
            mShowAdapter.notifyDataSetChanged()

        }
    }

    override fun onClickItem(position: Int) {
        val bundle = Bundle().apply {
            putInt("idShow", listShowPopular[position].id!!)
            putSerializable("tvShow", listShowPopular[position])
        }
        findNavController().navigate(R.id.action_showsFragment_to_deatilShowsFragment, bundle)
    }


}