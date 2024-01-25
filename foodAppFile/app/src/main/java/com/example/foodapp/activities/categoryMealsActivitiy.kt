package com.example.foodapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodapp.adapters.categoryMeaslAdapter
import com.example.foodapp.databinding.ActivityCategoryMealsActivitiyBinding
import com.example.foodapp.fragments.HomeFragment
import com.example.foodapp.viewModel.CategoryMealsViewModel

class categoryMealsActivitiy : AppCompatActivity() {
    lateinit var binding:ActivityCategoryMealsActivitiyBinding
    lateinit var categoryMealsModel:CategoryMealsViewModel
    lateinit var categoryMealsAdapter:categoryMeaslAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsActivitiyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        categoryMealsModel  = ViewModelProviders.of(this)[CategoryMealsViewModel::class.java]

        categoryMealsModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        categoryMealsModel.observeMealsLiveData().observe(this,Observer{mealsList->
            binding.tvCategoryCount.text   = mealsList.size.toString()
           categoryMealsAdapter.setMealList(mealsList)

        })
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = categoryMeaslAdapter()
        binding.rwMeals.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter= categoryMealsAdapter
        }
    }
}