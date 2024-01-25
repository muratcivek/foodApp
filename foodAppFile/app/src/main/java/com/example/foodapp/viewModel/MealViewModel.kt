package com.example.foodapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.db.mealDatabase
import com.example.foodapp.pojo.Meal
import com.example.foodapp.pojo.mealList
import com.example.foodapp.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(val mealDatabase: mealDatabase):ViewModel() {
    private var mealDetailsLiveData = MutableLiveData<Meal>()

    fun getMealDetail(id:String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object:Callback<mealList>{
            override fun onResponse(call: Call<mealList>, response: Response<mealList>) {
                if(response.body()!=null){
                    mealDetailsLiveData.value= response.body()!!.meals[0]
                }
                else{
                    return
                }
            }

            override fun onFailure(call: Call<mealList>, t: Throwable) {
                Log.d("onFailure: ",t.message.toString())
            }
        })
    }
    fun observerMealDetailsLiveData():LiveData<Meal> {
        return mealDetailsLiveData
    }

    fun insertMeal(meal:Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }}
