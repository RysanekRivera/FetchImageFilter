package com.rysanek.fetchimagefilter.presentation.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.rysanek.fetchimagefilter.R
import com.rysanek.fetchimagefilter.databinding.FragmentFilterEditBinding
import com.rysanek.fetchimagefilter.domain.utils.UploadState
import com.rysanek.fetchimagefilter.presentation.viewmodels.FilterImagesViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.glide.transformations.*
import jp.wasabeef.glide.transformations.gpu.*

@AndroidEntryPoint
class FilterEditFragment: Fragment() {
    
    private lateinit var binding: FragmentFilterEditBinding
    private val filterImagesViewModel: FilterImagesViewModel by viewModels()
    private lateinit var imageUri: String
    private lateinit var imageUrl: String
    
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterEditBinding.inflate(inflater, container, false)
        
        imageUri = arguments?.getString("imageUri") ?: ""
        imageUrl = arguments?.getString("imageUrl") ?: ""
        
        filterImagesViewModel.loadImagesToView(imageUri, binding.ivOriginal)
        
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.fabReset.setOnClickListener {
            filterImagesViewModel.loadImagesToView(imageUri, binding.ivOriginal)
        }
        
        binding.fabUpload.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setIcon(R.drawable.ic_upload)
                .setMessage("Ready to upload your image?")
                .setPositiveButton("yes"){ _, _->
                    filterImagesViewModel.saveAndUploadImage(binding.ivOriginal.drawable, imageUrl)
                }
                .setNegativeButton("cancel"){_, _ ->}
                .create()
                .show()
        }
        
        filterImagesViewModel.uploadState.observe(viewLifecycleOwner){ uploadStates ->
            handleUploadStates(uploadStates)
        }
    
        loadFilterImagesAndSetOnClickListener()
    }
    
    private fun loadFilterImagesAndSetOnClickListener() {
        with(binding) {
            listOf(
                ivBlur to BlurTransformation(),
                ivBrightness to BrightnessFilterTransformation(.3f),
                ivContrast to ContrastFilterTransformation(.3f),
                ivGrayscale to GrayscaleTransformation(),
                ivInvert to InvertFilterTransformation(),
                ivKuwahara to KuwaharaFilterTransformation(),
                ivPixelation to PixelationFilterTransformation(),
                ivSepia to SepiaFilterTransformation(),
                ivSketch to SketchFilterTransformation(),
                ivToon to ToonFilterTransformation(),
                ivVignette to VignetteFilterTransformation()
            ).forEach {
            
                filterImagesViewModel.filterAndLoadImagesToView(imageUri, it.first, it.second)
            
                val transformation = it.second
            
                it.first.setOnClickListener {
                    filterImagesViewModel.filterAndLoadImagesToView(imageUri, binding.ivOriginal, transformation)
                }
            }
        }
    }
    
    private fun handleUploadStates(state: UploadState){
        when (state){
            is UploadState.Idle -> { /*NO-OP*/ }
            is UploadState.Uploading -> { /*NO-OP*/ }
            is UploadState.Error -> {
                Snackbar.make(requireContext(), requireView(), UploadState.Error.message ?: "", Snackbar.LENGTH_SHORT).show()
            }
            is UploadState.Finished -> {
                Snackbar.make(requireContext(), requireView(),UploadState.Finished.message ?: "", Snackbar.LENGTH_SHORT).show()
    
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }
}