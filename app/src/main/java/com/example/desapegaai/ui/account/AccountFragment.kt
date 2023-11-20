package com.example.desapegaai.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desapegaai.R
import com.example.desapegaai.adapters.ProductAdapter
import com.example.desapegaai.databinding.FragmentAccountBinding
import com.example.desapegaai.ui.productDetails.ProductDetailsActivity
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val accountViewModel =
            ViewModelProvider(this)[AccountViewModel::class.java]

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initRecyclerView()

        accountViewModel.myProducts.observe(viewLifecycleOwner) {
            if (it != null) {
                Log.d("", it.toString())
//                productAdapter.setList(it)
                productAdapter.submitList(it)
//                binding.productRecyclerView.adapter?.notifyDataSetChanged()
            }
        }

        addProfileCard()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        productAdapter = ProductAdapter{product ->
            Log.d("", product.name!!)
            val productDetailsIntent = Intent(this.context, ProductDetailsActivity::class.java)
            productDetailsIntent.putExtra("product", product)

            startActivity(productDetailsIntent)
        }

        binding.productRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@AccountFragment.context)
            adapter = productAdapter
        }
    }

    private fun addProfileCard() {
        val profileCard = layoutInflater.inflate(R.layout.profile, binding.header.toolbar, false)
        val user = FirebaseAuth.getInstance().currentUser

        profileCard.findViewById<TextView>(R.id.profile_name).text = user?.displayName

        binding.header.toolbar.addView(profileCard)
        binding.header.toolbar.setContentInsetsAbsolute(0, 0)
        binding.header.toolbar.setContentInsetsRelative(0, 0)
    }
}