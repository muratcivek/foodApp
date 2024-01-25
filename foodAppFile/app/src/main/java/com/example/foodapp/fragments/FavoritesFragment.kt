package com.example.foodapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.activities.MainActivity
import com.example.foodapp.adapters.MealAdapter
import com.example.foodapp.databinding.FragmentFavoritesBinding
import com.example.foodapp.viewModel.homeViewModel
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : Fragment() {
private lateinit var binding:FragmentFavoritesBinding
private lateinit var viewModel: homeViewModel
private lateinit var favoritesAdapter:MealAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observerFavorites()

        val itemTouchHelper = object :  ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.deleteMeal(favoritesAdapter.differ.currentList[position])
                
                Snackbar.make(requireView(),"Meal deleted",Snackbar.LENGTH_LONG).setAction("Undo",
                        View.OnClickListener {
                            viewModel.insertMeal(favoritesAdapter.differ.currentList[position])
                        }
                    ).show()
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.fvFavorites) //fvFavorites recyclerview'in idsi

    }

    private fun prepareRecyclerView() {
        favoritesAdapter = MealAdapter()
        binding.fvFavorites.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = favoritesAdapter
        }

    }

    private fun     observerFavorites() {
        viewModel.observeFavoritesMealsLiveData().observe(viewLifecycleOwner, Observer {meals->
            favoritesAdapter.differ.submitList(meals)

        })
    }


}