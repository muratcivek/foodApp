package com.example.foodapp.retrofit

import com.example.foodapp.pojo.CategoryList
import com.example.foodapp.pojo.MealsByCategoryList
import com.example.foodapp.pojo.mealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("random.php")
    fun getRandomMeal(): Call<mealList>

    @GET("lookup.php?") //https://www.themealdb.com/api/json/v1/1/lookup.php?i=52772
    fun getMealDetails(@Query("i")id:String): Call<mealList>

    @GET("filter.php?")
    fun getPopularItems(@Query("c") categoryName:String) : Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategories():Call<CategoryList>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName:String):Call<MealsByCategoryList>

    @GET("search.php?")
    fun searchMeal(@Query("s") searchQuery:String): Call<mealList>
}