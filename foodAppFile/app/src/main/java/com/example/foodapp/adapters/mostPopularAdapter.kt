package com.example.foodapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.PopularItemsBinding
import com.example.foodapp.pojo.MealsByCategory

class mostPopularAdapter():RecyclerView.Adapter<mostPopularAdapter.popularMealViewHolder>() {
    lateinit var onItemClick:((MealsByCategory)->Unit)
    private var mealList = ArrayList<MealsByCategory>()
    lateinit var onLongItemClick:((MealsByCategory)->Unit)

    fun setMeals(mealsList:ArrayList<MealsByCategory>){
        this.mealList = mealsList
        notifyDataSetChanged()
    }

    class popularMealViewHolder(val binding:PopularItemsBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): popularMealViewHolder {
      return popularMealViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: popularMealViewHolder, position: Int) {
     Glide.with(holder.itemView.context)
         .load(mealList[position].strMealThumb)
         .into(holder.binding.imgPopularMealItem)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealList[position])
        }

        holder.itemView.setOnLongClickListener{
            onLongItemClick?.invoke(mealList[position])
           true
        }

    }
}