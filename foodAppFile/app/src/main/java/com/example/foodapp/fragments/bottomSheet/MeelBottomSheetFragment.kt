package com.example.foodapp.fragments.bottomSheet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.activities.MainActivity
import com.example.foodapp.activities.mealActivity
import com.example.foodapp.databinding.FragmentMeelBottomSheetBinding
import com.example.foodapp.fragments.HomeFragment
import com.example.foodapp.viewModel.homeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


private const val ARG_PARAM1 = "param1"


class MeelBottomSheetFragment : BottomSheetDialogFragment() {
    private var mealID: String? = null
    private lateinit var  binding : FragmentMeelBottomSheetBinding
    private lateinit var viewModel:homeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealID = it.getString(ARG_PARAM1)
        }
        viewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMeelBottomSheetBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealID?.let { viewModel.getMealById(it) }
        observeBottomSheetMeal()

        bottomSheetDialogClick()
    }


    private fun bottomSheetDialogClick() {
       binding.designBottomSheet.setOnClickListener{
            if(mealName != null && mealThumb !=null){
                val intent = Intent(activity,mealActivity::class.java)
                intent.apply {
                    putExtra(HomeFragment.MEAL_ID,mealID)
                    putExtra(HomeFragment.MEAL_NAME,mealName)
                    putExtra(HomeFragment.MEAL_THUMB,mealThumb)
                }
                startActivity(intent)
            }
       }
    }
    private var mealName:String?=null
    private var mealThumb:String?=null
    private fun observeBottomSheetMeal() {
            viewModel.observeBottomSheetMeal().observe(viewLifecycleOwner, Observer {
                Glide.with(requireContext()).load(it.strMealThumb).into(binding.imgCategory)
                binding.tvMealCountry.text = it.strArea
                binding.tvMealCategory.text = it.strCategory
                binding.tvMealNameInBtmsheet.text = it.strMeal

                mealName = it.strMeal
                mealThumb = it.strMealThumb

            })
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            MeelBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}