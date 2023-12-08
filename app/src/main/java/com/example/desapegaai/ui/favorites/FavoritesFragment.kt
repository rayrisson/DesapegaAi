package com.example.desapegaai.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desapegaai.R
import com.example.desapegaai.adapters.ProductAdapter
import com.example.desapegaai.databinding.FragmentFavoritesBinding
import com.example.desapegaai.ui.productDetails.ProductDetailsActivity
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null

    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter
    private lateinit var favoritesViewModel: FavoritesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        favoritesViewModel =
            ViewModelProvider(this)[FavoritesViewModel::class.java]

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initRecyclerView()

        updateFavList()

        favoritesViewModel.favProducts.observe(viewLifecycleOwner) {
            binding.productRecyclerView.hideSkeleton()

            if (it != null) {
                productAdapter.submitList(it)
            }
        }

        binding.header.toolbar.title = getString(R.string.title_nav_favorites)

        return root
    }

    private fun updateFavList(){
        binding.productRecyclerView.loadSkeleton(R.layout.product_item_with_fav_button)
        favoritesViewModel.getFavProducts()
    }

    private fun initRecyclerView() {
        productAdapter = ProductAdapter(onClickItem = {product ->
            val productDetailsIntent = Intent(this.context, ProductDetailsActivity::class.java)
            productDetailsIntent.putExtra("product", product)

            startActivity(productDetailsIntent)
        }, onClickFav = {product ->
            product.id?.let { favoritesViewModel.removeFromFav(it) }
        })

        binding.productRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@FavoritesFragment.context)
            adapter = productAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}