package com.example.toilet_seoul

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.example.toilet_seoul.databinding.ToiletRowBinding

class ToiletAdapter(private val toiletList: List<Toilet>) : RecyclerView.Adapter<ToiletAdapter.ToiletHolder>() {

    private val DiffAsync = AsyncListDiffer(this, DiffCallbackAsync())

    inner class DiffCallbackAsync : DiffUtil.ItemCallback<Toilet>() {
        override fun areItemsTheSame(oldItem: Toilet, newItem: Toilet) =
            oldItem.toiletNm == newItem.toiletNm

        override fun areContentsTheSame(oldItem: Toilet, newItem: Toilet) =
            oldItem == newItem
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToiletHolder {
        val itemBinding = ToiletRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToiletHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ToiletHolder, position: Int) {
        val toilet: Toilet = toiletList[position]
        holder.bind(toilet)
    }

    override fun getItemCount(): Int = toiletList.size

    fun submitList(newList : ArrayList<Toilet>){
        DiffAsync.submitList(newList)
    }

    class ToiletHolder(private val itemBinding: ToiletRowBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(toilet: Toilet) {
            val distance = QueryActivity().getDistance(toilet.latitude!!, toilet.longitude!!)

            itemBinding.tvName.text = toilet.toiletNm
            itemBinding.tvAddress.text = toilet.lnmadr
            itemBinding.tvUnisex.text = "공용화장실여부: ${toilet.unisexToiletYn}"

            if (distance != 0)
                itemBinding.tvDistance.text = "거리: ${distance}m"

            itemBinding.viewOnMap.setOnClickListener {
                val intent = Intent(it.context, MainActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable("toilet", toilet)
                bundle.putBoolean("clicked", true)
                intent.putExtra("bundle", bundle)
                it.context.startActivity(intent)
            }
        }
    }
}