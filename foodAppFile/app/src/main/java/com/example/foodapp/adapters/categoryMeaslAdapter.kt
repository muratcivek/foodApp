package com.example.foodapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.databinding.MealItemBinding
import com.example.foodapp.pojo.MealsByCategory

class categoryMeaslAdapter:RecyclerView.Adapter<categoryMeaslAdapter.CategoryMealsViewModel>(){
    private var mealsList = ArrayList<MealsByCategory>()

    fun setMealList(mealsList:List<MealsByCategory>){
        this.mealsList = mealsList as ArrayList<MealsByCategory>
        notifyDataSetChanged()
    }

    inner class CategoryMealsViewModel(val binding:MealItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewModel {
        return CategoryMealsViewModel(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
       return mealsList.size
    }

    override fun onBindViewHolder(holder: CategoryMealsViewModel, position: Int) {
        val placeholderColor = ContextCompat.getColor(holder.itemView.context, R.color.white)

        Glide.with(holder.itemView.context).load(mealsList[position].strMealThumb).placeholder(placeholderColor).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text=mealsList[position].strMeal
    }

}