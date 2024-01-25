package com.example.foodapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityMealBinding
import com.example.foodapp.db.mealDatabase
import com.example.foodapp.fragments.HomeFragment
import com.example.foodapp.pojo.Meal
import com.example.foodapp.viewModel.MealViewModel
import com.example.foodapp.viewModel.mealViewModelFactory


class mealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var youtubeLink:String
    private lateinit var mealMvvm:MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mealDatabase = mealDatabase.getInstance(this)
        val mealViewModelFactory = mealViewModelFactory(mealDatabase)
       // mealMvvm  = ViewModelProviders.of(this)[MealViewModel::class.java]
        mealMvvm = ViewModelProvider(this,mealViewModelFactory)[MealViewModel::class.java]
        getMealInformationFromIntent()
        setInformationInViews()
        loadingCase()
        mealMvvm.getMealDetail(mealId)
        observerMealDetailsLiveData()
        onYoutubeImageClick()

        onFavoriteClick()
    }

    @SuppressLint("ResourceAsColor", "SuspiciousIndentation")
    private fun onFavoriteClick() {
        binding.addFav.setOnClickListener{
                mealToSave?.let {
                    mealMvvm.insertMeal(it)
                    val animation: Animation =
                        AnimationUtils.loadAnimation(this, com.example.foodapp.R.anim.scale_animation)
                         binding.addFav.startAnimation(animation)
                         Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();

                }
            }
    }

    private fun onYoutubeImageClick() {
        binding.imageviewYoutube.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave:Meal?=null
    private fun observerMealDetailsLiveData() {
        onResponseCase()
        mealMvvm.observerMealDetailsLiveData().observe(this,object:Observer<Meal>{
            override fun onChanged(value: Meal) {
                val meal = value
                binding.tvCategories.text="Category:  ${meal.strCategory}"
                binding.tvInstructions.text=meal.strInstructions
                binding.tvArea.text="Area:  ${meal.strArea}"
                mealToSave = meal
                youtubeLink =meal.strYoutube.toString()

            }

        })
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imageMealDetay)

        binding.collapsingToolbar.title= mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(com.example.foodapp.R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(com.example.foodapp.R.color.white))

    }

    private fun getMealInformationFromIntent() {
        val intent  = intent
        mealId=intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName=intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb=intent.getStringExtra(HomeFragment.MEAL_THUMB)!!


    }
    private fun loadingCase(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.addFav.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvCategories.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.imageviewYoutube.visibility = View.INVISIBLE

    }
    private fun onResponseCase(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.addFav.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvCategories.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.imageviewYoutube.visibility = View.VISIBLE
    }
}