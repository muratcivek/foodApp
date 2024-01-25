package com.example.foodapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.MainActivity
import com.example.foodapp.R
import com.example.foodapp.adapters.MealAdapter
import com.example.foodapp.databinding.FragmentSearchBinding
import com.example.foodapp.viewModel.homeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class searchFragment : Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private lateinit var viewModel:homeViewModel
    private lateinit var searchRecyclerviewAdapter:MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as com.example.foodapp.activities.MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        binding.icSearch.setOnClickListener{
            searchMeals()
        }

        observeSearchedMealsLiveData()

        var searchJob: Job? = null
        binding.edSearch.addTextChangedListener {

            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.searchMeal(it.toString())
            }
        }


    }

    private fun observeSearchedMealsLiveData() {
       viewModel.observesearchedMealListLiveData().observe(viewLifecycleOwner, Observer {
           searchRecyclerviewAdapter.differ.submitList(it)
       })
    }

    private fun searchMeals() {
       val searchQuery = binding.edSearch.text.toString()
        if(searchQuery.isNotEmpty()){
            viewModel.searchMeal(searchQuery)
        }
    }

    private fun prepareRecyclerView() {
        searchRecyclerviewAdapter = MealAdapter()
        binding.rvSearchedMeals.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = searchRecyclerviewAdapter
        }
    }


}