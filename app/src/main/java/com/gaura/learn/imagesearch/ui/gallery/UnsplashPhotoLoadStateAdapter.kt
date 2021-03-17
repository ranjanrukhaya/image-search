package com.gaura.learn.imagesearch.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gaura.learn.imagesearch.R
import kotlinx.android.synthetic.main.unsplash_photo_load_state_footer.view.button_retry
import kotlinx.android.synthetic.main.unsplash_photo_load_state_footer.view.progress_bar
import kotlinx.android.synthetic.main.unsplash_photo_load_state_footer.view.text_view_error

class UnsplashPhotoLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<UnsplashPhotoLoadStateAdapter.LoadStateViewHolder>() {


    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.unsplash_photo_load_state_footer, parent, false)
        return LoadStateViewHolder(view)
    }

    inner class LoadStateViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        init {
            view.button_retry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            view.progress_bar.isVisible = loadState is LoadState.Loading
            view.button_retry.isVisible = loadState !is LoadState.Loading
            view.text_view_error.isVisible = loadState !is LoadState.Loading
        }
    }
}