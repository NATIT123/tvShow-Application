package com.example.tvshowsapplication.Fragments

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tvshowsapplication.Adapters.FavoriteShowAdapter
import com.example.tvshowsapplication.MainActivity
import com.example.tvshowsapplication.R
import com.example.tvshowsapplication.ViewModel.ShowViewModel
import com.example.tvshowsapplication.databinding.FragmentFavoriteBinding
import com.example.tvshowsapplication.models.TvShow

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteFragment : Fragment(), FavoriteShowAdapter.onClickItemListner {


    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var showViewModel: ShowViewModel
    private lateinit var mFavoriteShowAdapter: FavoriteShowAdapter
    private var listFavoriteShow = listOf<TvShow>()
    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showViewModel = (activity as MainActivity).viewModel

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearLayoutManager = LinearLayoutManager(requireContext())
        mFavoriteShowAdapter = FavoriteShowAdapter(this)
        observerFavoriteShow()

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }


    private fun observerFavoriteShow() {
        showViewModel.observerFavoriteShowLiveData().observe(viewLifecycleOwner) { it ->
            this.listFavoriteShow = it
            mFavoriteShowAdapter.differ.submitList(this.listFavoriteShow)
            binding.rcvFavorite.apply {
                layoutManager = linearLayoutManager
                adapter = mFavoriteShowAdapter
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
        }
    }

    override fun onClickItem(position: Int) {
        val bundle = Bundle().apply {
            putInt("idShow", listFavoriteShow[position].id!!)
            putSerializable("tvShow", listFavoriteShow[position])
        }
        findNavController().navigate(R.id.action_favoriteFragment_to_deatilShowsFragment, bundle)
    }

    override fun deleteItem(position: Int) {
        val dialog = AlertDialog.Builder(requireContext())
        val show = mFavoriteShowAdapter.differ.currentList[position]
        dialog.apply {
            setTitle("Confirm Delete Show")
            setMessage("Do you want to delete show ${show.name}")
            setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                showViewModel.deleteTvShow(show)
                Toast.makeText(requireContext(), "Delete TvShow Successfully", Toast.LENGTH_SHORT)
                    .show()
            })
            setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
            })
        }
        dialog.show()
    }


}