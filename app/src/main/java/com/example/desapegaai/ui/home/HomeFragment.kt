package com.example.desapegaai.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desapegaai.R
import com.example.desapegaai.adapters.ProductAdapter
import com.example.desapegaai.data.Product
import com.example.desapegaai.databinding.FragmentHomeBinding
import com.example.desapegaai.ui.productDetails.ProductDetailsActivity
import java.io.Serializable

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        initRecyclerView()

        homeViewModel.products.observe(viewLifecycleOwner) {
            if (it != null) {
                Log.d("", it.toString())
//                productAdapter.setList(it)
                productAdapter.submitList(it)
//                binding.productRecyclerView.adapter?.notifyDataSetChanged()
            }
        }

        addSearchBar()

        return root
    }

    private fun initRecyclerView() {
        productAdapter = ProductAdapter{product ->
            Log.d("", product.name!!)
            val productDetailsIntent = Intent(this.context, ProductDetailsActivity::class.java)
            productDetailsIntent.putExtra("product", product)

            startActivity(productDetailsIntent)
        }

        binding.productRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeFragment.context)
            adapter = productAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addSearchBar() {
        val homeSearchBar = layoutInflater.inflate(R.layout.home_searchbar, binding.header.toolbar, false)
        binding.header.toolbar.addView(homeSearchBar)
        binding.header.toolbar.setContentInsetsAbsolute(0, 0)
        binding.header.toolbar.setContentInsetsRelative(0, 0)
    }
}