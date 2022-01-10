package com.rysanek.fetchimagefilter.presentation.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rysanek.fetchimagefilter.databinding.FragmentPicturesListBinding
import com.rysanek.fetchimagefilter.domain.utils.DownloadState
import com.rysanek.fetchimagefilter.domain.utils.gone
import com.rysanek.fetchimagefilter.domain.utils.show
import com.rysanek.fetchimagefilter.presentation.adapters.PicturesRecyclerViewAdapter
import com.rysanek.fetchimagefilter.presentation.viewmodels.FetchImagesViewModel
import com.rysanek.fetchimagefilter.presentation.viewmodels.LoadFromDbViewModel
import com.rysanek.fetchimagefilter.presentation.viewmodels.LoadImagesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PicturesListFragment: Fragment() {
    
    private lateinit var binding: FragmentPicturesListBinding
    private val fetchImagesViewModel: FetchImagesViewModel by viewModels()
    private val loadImagesViewModel: LoadImagesViewModel by viewModels()
    private val loadFromDbViewModel: LoadFromDbViewModel by viewModels()
    private lateinit var rvAdapter: PicturesRecyclerViewAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        
        binding = FragmentPicturesListBinding.inflate(inflater, container, false)
        
        setupRecyclerview()
        
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        fetchImagesViewModel.downloadState.observe(viewLifecycleOwner) { downloadState ->
            handleDownloadState(downloadState)
        }
        
        loadFromDbViewModel.loadPicturesListFromDb().observe(viewLifecycleOwner) { picturesList ->
            rvAdapter.setData(picturesList)
        }
    }
    
    private fun setupRecyclerview() {
        rvAdapter = PicturesRecyclerViewAdapter(loadImagesViewModel)
        
        binding.rvPictures.apply {
            adapter = rvAdapter.apply {
                stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }

            val spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 1 else 2
            
            layoutManager = GridLayoutManager(requireContext(), spanCount)
        }
    }
    
    /**
     * Handles the UI during the different states of updating data from the server.
     * @param state [DownloadState].
     * */
    private fun handleDownloadState(state: DownloadState?) {
        when (state) {
            is DownloadState.Idle -> { binding.progressBar.gone() }
            is DownloadState.Downloading -> { binding.progressBar.show() }
            is DownloadState.Finished -> { binding.progressBar.gone() }
            is DownloadState.Error -> { binding.progressBar.gone() }
            else -> { /* NO-OP */ }
        }
    }
}