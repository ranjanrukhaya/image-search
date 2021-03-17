package com.gaura.learn.imagesearch.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.gaura.learn.imagesearch.R
import com.gaura.learn.imagesearch.data.UnsplashPhoto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_gallery.*


@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery), UnsplashPhotoAdapter.OnItemClickListener {

    private val viewModel by viewModels<GalleryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photoAdapter = UnsplashPhotoAdapter(this)

        recycler_view.apply {
            itemAnimator = null
            adapter = photoAdapter.withLoadStateHeaderAndFooter(
                header = UnsplashPhotoLoadStateAdapter { photoAdapter.retry() },
                footer = UnsplashPhotoLoadStateAdapter { photoAdapter.retry() }
            )
            setHasFixedSize(true)
            button_retry.setOnClickListener { photoAdapter.retry() }
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            photoAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        photoAdapter.addLoadStateListener { loadState ->
            progress_bar.isVisible = loadState.source.refresh is LoadState.Loading
            recycler_view.isVisible = loadState.source.refresh is LoadState.NotLoading
            button_retry.isVisible = loadState.source.refresh is LoadState.Error
            text_view_error.isVisible = loadState.source.refresh is LoadState.Error

            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.refresh.endOfPaginationReached &&
                photoAdapter.itemCount < 1
            ) {
                recycler_view.isVisible = false
                text_view_empty.isVisible = true
            } else {
                text_view_empty.isVisible = false
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_gallery, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    recycler_view.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    override fun onItemClick(photo: UnsplashPhoto) {
        val action = GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(photo)
        findNavController().navigate(action)
    }
}