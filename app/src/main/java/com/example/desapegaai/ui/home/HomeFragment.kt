package com.example.desapegaai.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desapegaai.R
import com.example.desapegaai.adapters.ProductAdapter
import com.example.desapegaai.databinding.FragmentHomeBinding
import com.example.desapegaai.ui.productDetails.ProductDetailsActivity
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initRecyclerView()

        updateProductList()

        homeViewModel.products.observe(viewLifecycleOwner) {
            binding.productRecyclerView.hideSkeleton()

            if (it != null) {
                productAdapter.submitList(it)
            }
        }

        binding.homeSearchbar.setOnQueryTextListener(object: OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                closeKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText == "" || newText == null) {
                    updateProductList()
                } else {
                    updateProductList(newText)
                }
                return true
            }

        })

        return root
    }

    private fun updateProductList(query: String? = null) {
        binding.productRecyclerView.loadSkeleton(R.layout.product_item)
        homeViewModel.getProducts(query)
    }

    private fun initRecyclerView() {
        productAdapter = ProductAdapter(onClickItem = {product ->
            val productDetailsIntent = Intent(this.context, ProductDetailsActivity::class.java)
            productDetailsIntent.putExtra("product", product)

            startActivity(productDetailsIntent)
        })

        binding.productRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeFragment.context)
            adapter = productAdapter
        }
    }

    private fun closeKeyboard(){
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}