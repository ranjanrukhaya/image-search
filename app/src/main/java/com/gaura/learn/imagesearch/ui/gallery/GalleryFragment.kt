package com.gaura.learn.imagesearch.ui.gallery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gaura.learn.imagesearch.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_gallery.*

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private val viewModel by viewModels<GalleryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photoAdapter = UnsplashPhotoAdapter()

        recycler_view.apply {
            adapter = photoAdapter
            setHasFixedSize(true)
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            photoAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }
}