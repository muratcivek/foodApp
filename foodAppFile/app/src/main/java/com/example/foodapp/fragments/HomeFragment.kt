package com.example.foodapp.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.activities.MainActivity
import com.example.foodapp.activities.categoryMealsActivitiy
import com.example.foodapp.activities.mealActivity
import com.example.foodapp.adapters.CategoriesAdapter
import com.example.foodapp.adapters.mostPopularAdapter
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.fragments.bottomSheet.MeelBottomSheetFragment
import com.example.foodapp.pojo.MealsByCategory
import com.example.foodapp.pojo.Meal
import com.example.foodapp.viewModel.homeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var viewModel:homeViewModel
    private lateinit var randomMeal:Meal
    private lateinit var popularItemsAdapter:mostPopularAdapter
    private lateinit var categoriesAdapter:CategoriesAdapter


    companion object{
        const val MEAL_ID = "com.example.foodapp.fragments.idMeal"
        const val MEAL_NAME = "com.example.foodapp.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.foodapp.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.foodapp.fragments.CategoryName"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        popularItemsAdapter= mostPopularAdapter()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClink()

        preparePopulerItemsRecyclerView()
        viewModel.getPopulerItems()
        observerPopulerItemsLiveData()
        onPopolarItemClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        obserserverCategoriesLiveData()
        onCategoryClick()

        onPopularItemLongClick()

        onSearchIconClick()

    }

    private fun onSearchIconClick() {
       binding.imgSearch.setOnClickListener{
           findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
       }
    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick = {
            val mealBottomSheetFragment = MeelBottomSheetFragment.newInstance(it.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"meal info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick={category->
            val intent = Intent(activity,categoryMealsActivitiy::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            startActivity(intent)

        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.recViewCategories.apply{
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter=categoriesAdapter
        }
    }

    private fun obserserverCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner,Observer{categories->

                categoriesAdapter.setCategoryList(categories)




        })
    }

    private fun onPopolarItemClick() {
        popularItemsAdapter.onItemClick = {meal ->
            val intent = Intent(activity,mealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            startActivity(intent)

        }

    }

    private fun preparePopulerItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            adapter = popularItemsAdapter

        }

    }

    private fun observerPopulerItemsLiveData() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner,
         {mealList->
            popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsByCategory>)
        })
    }

    private fun onRandomMealClink() {
        binding.randomMealCard.setOnClickListener({
            val intent = Intent(activity,mealActivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME,randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
            startActivity(intent)
        })
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner,object : Observer<Meal>{
            override fun onChanged(value: Meal) {
                Glide.with(requireContext())
                    .load(value.strMealThumb)
                    .into(binding.imgRandomMeal)
                randomMeal = value
            }


        }
        )


    }


}