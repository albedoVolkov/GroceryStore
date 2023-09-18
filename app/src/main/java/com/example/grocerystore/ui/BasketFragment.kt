package com.example.grocerystore.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.grocerystore.databinding.BasketFragmentBinding


class BasketFragment  : Fragment() {

    private var _binding: BasketFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BasketFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }



    companion object {
        fun newInstance(): Fragment = BasketFragment()
    }

}