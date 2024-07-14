package com.example.tvshowsapplication.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tvshowsapplication.Adapters.BottomSheetShowAdapter
import com.example.tvshowsapplication.Adapters.SliderImageView
import com.example.tvshowsapplication.MainActivity
import com.example.tvshowsapplication.R
import com.example.tvshowsapplication.ViewModel.ShowViewModel
import com.example.tvshowsapplication.databinding.FragmentDeatilShowBinding
import com.example.tvshowsapplication.databinding.FragmentShowBottomSheetBinding
import com.example.tvshowsapplication.models.TvShow
import com.example.tvshowsapplication.models.TvShowDetail
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Locale

class DetailShowFragment : Fragment() {

    private lateinit var binding: FragmentDeatilShowBinding
    private var tvShowDetail: TvShowDetail? = null
    private var listImage = listOf<String>()
    private lateinit var mSliderImageView: SliderImageView
    private lateinit var showViewModel: ShowViewModel
    private val args: DetailShowFragmentArgs by navArgs()

    private var tvShow: TvShow? = null

    private lateinit var episodeBottomSheetDialog: BottomSheetDialog
    private lateinit var layoutFragmentShowBottomSheetBinding: FragmentShowBottomSheetBinding
    private lateinit var mBottomSheetShowAdapter: BottomSheetShowAdapter

    companion object {
        @JvmStatic
        @BindingAdapter("android:imageURL")
        fun setImageURL(view: ImageView, url: String) {
            Glide.with(view.context).load(url).into(view)
        }

        @JvmStatic
        @BindingAdapter("android:imageCircleURL")
        fun setImageCircle(view: CircleImageView, url: String?) {
            Glide.with(view.context).load(url).into(view)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showViewModel = (activity as MainActivity).viewModel
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeatilShowBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSliderImageView = SliderImageView(listImage)
        val idShow = args.idShow
        tvShow = args.tvShow
        showViewModel.getDetailShow(idShow)
        observerTvShowDetails()

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


    }


    @SuppressLint("SetTextI18n")
    private fun observerTvShowDetails() {
        binding.isLoading = true
        showViewModel.observerDetailShowLiveData().observe(viewLifecycleOwner) { it ->
            binding.isLoading = false
            this.tvShowDetail = it

            if (tvShowDetail != null) {
                this.listImage = tvShowDetail!!.pictures
                loadImageSlider(listImage)
                tvShowDetail?.image_path?.let {
                    val scope = CoroutineScope(Job() + Dispatchers.Main)
                    scope.launch {
                        binding.tvShowImageURL = it
                    }
                    binding.imageTvShow.visibility = View.VISIBLE
                }

                binding.tvNameShow = tvShowDetail!!.name
                binding.tvNetworkShow = tvShowDetail!!.network
                binding.tvStatusShow = tvShowDetail!!.status
                binding.tvStartedOn = tvShowDetail!!.start_date
                binding.setTvDescription(
                    HtmlCompat.fromHtml(
                        tvShowDetail!!.description, HtmlCompat.FROM_HTML_MODE_LEGACY
                    ).toString()
                )


                binding.tvReadMore.setOnClickListener {
                    if (binding.tvReadMore.text == "Read More") {
                        binding.tvDescription.maxLines = Integer.MAX_VALUE
                        binding.tvDescription.ellipsize = null
                        binding.tvReadMore.text = "Read Less"
                    } else {
                        binding.tvDescription.maxLines = 4
                        binding.tvDescription.ellipsize = TextUtils.TruncateAt.END
                        binding.tvReadMore.text = "Read More"
                    }
                }

                binding.tvRate =
                    String.format(Locale.getDefault(), "%.2f", tvShowDetail!!.rating.toDouble())

                if (tvShowDetail!!.genres != null) {
                    binding.tvGenre = tvShowDetail!!.genres[0]
                } else {
                    binding.tvGenre = "N/A"
                }

                binding.tvMinute = tvShowDetail!!.runtime.toString()
            }

            binding.btnWebsite.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(tvShowDetail!!.url)
                startActivity(intent)
            }

            binding.btnEpisodes.setOnClickListener {
                episodeBottomSheetDialog = BottomSheetDialog(requireContext())
                layoutFragmentShowBottomSheetBinding =
                    FragmentShowBottomSheetBinding.inflate(layoutInflater)


                mBottomSheetShowAdapter = BottomSheetShowAdapter()
                mBottomSheetShowAdapter.differ.submitList(tvShowDetail!!.episodes)

                layoutFragmentShowBottomSheetBinding.rcvEpisode.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = mBottomSheetShowAdapter
                }

                layoutFragmentShowBottomSheetBinding.tvName = tvShowDetail!!.name
                layoutFragmentShowBottomSheetBinding.imgButtonClose.setOnClickListener {
                    episodeBottomSheetDialog.dismiss()
                }

                episodeBottomSheetDialog.setContentView(layoutFragmentShowBottomSheetBinding.root)

                val frameLayout =
                    episodeBottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

                frameLayout?.let {
                    val bottomSheetBehavior: BottomSheetBehavior<View> =
                        BottomSheetBehavior.from(frameLayout)
                    bottomSheetBehavior.peekHeight =
                        Resources.getSystem().displayMetrics.heightPixels
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }


                episodeBottomSheetDialog.show()
            }
            binding.btnFavorite.setOnClickListener {
                tvShow?.let { it1 -> upsertEpisode(it1) }
            }
        }

    }

    private fun loadImageSlider(listImage: List<String>) {
        binding.viewPager2Image.apply {
            mSliderImageView = SliderImageView(listImage)
            offscreenPageLimit = 1
            adapter = mSliderImageView
            visibility = View.VISIBLE
        }
        binding.viewFadingEdge.visibility = View.VISIBLE
        binding.circleIndicator.visibility = View.VISIBLE
        binding.circleIndicator.setViewPager(binding.viewPager2Image)
        mSliderImageView.registerAdapterDataObserver(binding.circleIndicator.adapterDataObserver)
    }

    private fun upsertEpisode(tvShow: TvShow) {
        tvShowDetail.let {
            showViewModel.saveTvShow(tvShow)
            Toast.makeText(requireContext(), "Tv Show Saved", Toast.LENGTH_SHORT).show()
            binding.btnFavorite.setImageResource(R.drawable.baseline_check_24)
        }
    }

}