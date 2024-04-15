package com.example.irgilatihan07.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.irgilatihan07.databinding.ItemReviewBinding

class ListAdapter(private val listReview: ArrayList<String>): RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemReviewBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
       val review = listReview[position]
       holder.binding.tvItem.text = review
    }

    override fun getItemCount(): Int = listReview.size

}