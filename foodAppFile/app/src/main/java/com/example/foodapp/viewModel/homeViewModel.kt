package com.example.foodapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.db.mealDatabase
import com.example.foodapp.pojo.Category
import com.example.foodapp.pojo.CategoryList
import com.example.foodapp.pojo.MealsByCategoryList
import com.example.foodapp.pojo.MealsByCategory
import com.example.foodapp.pojo.Meal
import com.example.foodapp.pojo.mealList
import com.example.foodapp.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class homeViewModel(
    private val mealDtabase:mealDatabase
): ViewModel() {
    private  var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData =MutableLiveData<List<MealsByCategory>>()
        private  var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favoritesMealLiveData = mealDtabase.mealDao().getAllMeals()
    private  var bottomSheetMealLiveData = MutableLiveData<Meal>()
    private var searchedMealsLiveData = MutableLiveData<List<Meal>>()



    fun getRandomMeal(){
        //retrofit api çağrısı
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<mealList> {
            override fun onResponse(call: Call<mealList>, response: Response<mealList>) {
                if(response.body()!=null){
                    val randomMeal: Meal =response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal;
                }else{
                }
            }

            override fun onFailure(call: Call<mealList>, t: Throwable) {
            }
        })
    }

    fun getPopulerItems(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if(response.body()!=null){
                        popularItemsLiveData.value = response.body()!!.meals
                }else{
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
            }

        })
    }

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object :Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {

                if(response.body()!=null){
                    categoriesLiveData.value = response.body()!!.categories
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
            }
        })


    }

    fun getMealById(id:String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<mealList>{
            override fun onResponse(call: Call<mealList>, response: Response<mealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let {
                    bottomSheetMealLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<mealList>, t: Throwable) {

            }

        })
    }

    fun deleteMeal(meal:Meal){
        viewModelScope.launch {
            mealDtabase.mealDao().delete(meal)
        }
    }

    fun insertMeal(meal:Meal) {
        viewModelScope.launch {
            mealDtabase.mealDao().upsert(meal)
        }
    }

    fun  searchMeal(searchQuery:String){
        RetrofitInstance.api.searchMeal(searchQuery).enqueue(
            object :Callback<mealList>{
                override fun onResponse(call: Call<mealList>, response: Response<mealList>) {
                    val mealList = response.body()?.meals
                    mealList?.let {
                        searchedMealsLiveData.postValue(it)
                    }

                }

                override fun onFailure(call: Call<mealList>, t: Throwable) {

                }

            }
        )
    }

    fun observesearchedMealListLiveData():LiveData<List<Meal>>{return searchedMealsLiveData}
    fun observeRandomMealLiveData():LiveData<Meal>{ return randomMealLiveData }
    fun observePopularItemsLiveData():LiveData<List<MealsByCategory>>{return popularItemsLiveData }
    fun observeCategoriesLiveData():LiveData<List<Category>>{return categoriesLiveData}

    fun observeFavoritesMealsLiveData():LiveData<List<Meal>>{
        return favoritesMealLiveData
    }

    fun observeBottomSheetMeal() :LiveData<Meal>{
        return bottomSheetMealLiveData
    }
}