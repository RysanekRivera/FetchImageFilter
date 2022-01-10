package com.rysanek.fetchimagefilter.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.rysanek.fetchimagefilter.R
import com.rysanek.fetchimagefilter.data.local.entities.PictureEntity
import com.rysanek.fetchimagefilter.databinding.SinglePictureItemBinding
import com.rysanek.fetchimagefilter.domain.utils.PicturesListDiffUtil
import com.rysanek.fetchimagefilter.presentation.viewmodels.LoadImagesViewModel

class PicturesRecyclerViewAdapter(
    private val loadImagesViewModel: LoadImagesViewModel
): RecyclerView.Adapter<PicturesRecyclerViewAdapter.ItemsRecyclerViewHolder>() {
    
    private var currentArticleList = mutableListOf<PictureEntity>()
    
    class ItemsRecyclerViewHolder(private val binding: SinglePictureItemBinding) : RecyclerView.ViewHolder(binding.root) {
        
        companion object {
            
            fun from(parent: ViewGroup): ItemsRecyclerViewHolder {
                
                val inflater = LayoutInflater.from(parent.context)
                
                val singleLayout = SinglePictureItemBinding.inflate(inflater, parent, false)
                
                return ItemsRecyclerViewHolder(singleLayout)
            }
        }
        
        fun bind(entity: PictureEntity, viewModel: LoadImagesViewModel) {
            
            viewModel.loadImagesToView(entity.url, binding.ivPicture)
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsRecyclerViewHolder =
        ItemsRecyclerViewHolder.from(parent)
    
    
    override fun onBindViewHolder(holder: ItemsRecyclerViewHolder, position: Int) {
        val item = currentArticleList[position]
        holder.bind(item, loadImagesViewModel)
        holder.itemView.setOnClickListener {
            val options = bundleOf("imageUri" to item.imageUri, "imageUrl" to item.url)
            holder.itemView.findNavController().navigate(R.id.action_picturesListFragment_to_filterEditFragment, options)
        }
    }
    
    override fun getItemCount() = currentArticleList.size
    
    fun setData(list: List<PictureEntity>) {
        PicturesListDiffUtil(currentArticleList, list).calculateDiff(this)
        currentArticleList.clear()
        currentArticleList = list as MutableList
    }
}