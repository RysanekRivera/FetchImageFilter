package com.rysanek.fetchimagefilter.domain.utils

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rysanek.fetchimagefilter.data.local.entities.PictureEntity

class PicturesListDiffUtil(private val oldList: List<PictureEntity>, private val newList: List<PictureEntity>): DiffUtil.Callback() {
    
    override fun getOldListSize() = oldList.size
    
    override fun getNewListSize() = newList.size
    
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
    
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList [newItemPosition]
    }
    
    /**
     * Handles calculating the difference between two lists and updating the UI accordingly.
     * @param adapter The [RecyclerView.Adapter] to which the changes will dispatch to.
     */
    fun <T: RecyclerView.ViewHolder> calculateDiff(adapter: RecyclerView.Adapter<T>) =
        DiffUtil.calculateDiff(this).dispatchUpdatesTo(adapter)
}