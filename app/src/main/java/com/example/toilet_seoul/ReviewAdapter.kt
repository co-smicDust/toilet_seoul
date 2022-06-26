package com.example.toilet_seoul

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.jar.Attributes

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    var items: MutableList<ReviewData> = mutableListOf(ReviewData("깨끗해요", "'빨간망토' 님의 리뷰"),
        ReviewData("휴지가 없네요...", "'너무급해' 님의 리뷰"),ReviewData("굿굿", "'제발버텨' 님의 리뷰"),
        ReviewData("시원하다", "'KSH' 님의 리뷰"),ReviewData("깨끗한 편이네요", "'LJY' 님의 리뷰")
        ,ReviewData("건물 들어가서 오른쪽에 있어요", "'HSY' 님의 리뷰"))
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = ReviewViewHolder(parent)
    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holer: ReviewViewHolder, position: Int) {
        items[position].let { item ->
            with(holer) {
                Review.text = item.title
                UserName.text = item.content
            }
        }
    }
    inner class ReviewViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)) {
        val UserName = itemView.findViewById<TextView>(R.id.userName)
        val Review = itemView.findViewById<TextView>(R.id.review)
    }
}
