package com.example.desapegaai.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desapegaai.R
import com.example.desapegaai.ui.welcome.WelcomeActivity
import com.example.desapegaai.adapters.ProductAdapter
import com.example.desapegaai.databinding.FragmentAccountBinding
import com.example.desapegaai.databinding.ProfileBinding
import com.example.desapegaai.ui.newProduct.NewProductActivity
import com.example.desapegaai.ui.productDetails.ProductDetailsActivity
import com.google.firebase.auth.FirebaseAuth
import koleton.api.hideSkeleton
import koleton.api.isSkeletonShown
import koleton.api.loadSkeleton

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        accountViewModel =
            ViewModelProvider(this)[AccountViewModel::class.java]

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initRecyclerView()

        updateProducts()

        accountViewModel.myProducts.observe(viewLifecycleOwner) {
            if(binding.productRecyclerView.isSkeletonShown())
                binding.productRecyclerView.hideSkeleton()

            if(binding.refreshLayout.isRefreshing) {
                binding.refreshLayout.isRefreshing = false
                binding.productRecyclerView.smoothScrollToPosition(-2)
            }

            if (it != null) {
                productAdapter.submitList(it)
            }
        }

        binding.refreshLayout.setOnRefreshListener {
            updateProducts {
                binding.refreshLayout.isRefreshing = false
            }
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == AppCompatActivity.RESULT_OK) {
                accountViewModel.getProductsByUserId()
                Toast.makeText(this@AccountFragment.context, "Produto atualizado", Toast.LENGTH_SHORT).show()
            }
        }

        addProfileCard()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateProducts(
        onError: ((Throwable?) -> Unit)? = null
    ){
        binding.productRecyclerView.loadSkeleton(R.layout.product_item)
        accountViewModel.getProductsByUserId(onError)
    }

    private fun initRecyclerView() {
        productAdapter = ProductAdapter(onClickItem = {product ->
            val productDetailsIntent = Intent(this.context, ProductDetailsActivity::class.java)
            productDetailsIntent.putExtra("product", product)

            startActivity(productDetailsIntent)
        }, onClickEdit = {
            val intent = Intent(this@AccountFragment.context, NewProductActivity::class.java)
            intent.putExtra("product", it)
            resultLauncher.launch(intent)
        }, onClickDelete = {
            accountViewModel.deleteProductById(it.id!!, onSuccess = {
                accountViewModel.getProductsByUserId()
                Toast.makeText(this@AccountFragment.context, "Produto deletado!", Toast.LENGTH_SHORT).show()
            }){
                Toast.makeText(this@AccountFragment.context, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
        })

        binding.productRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@AccountFragment.context)
            adapter = productAdapter
        }
    }

    private fun addProfileCard() {
        val profileCard = ProfileBinding.inflate(LayoutInflater.from(context), binding.header.toolbar, false)
        val user = FirebaseAuth.getInstance().currentUser

        profileCard.profileName.text = user?.displayName
        profileCard.logOutButton.setOnClickListener {
            accountViewModel.logOut()

            val intent = Intent(context, WelcomeActivity::class.java)

            startActivity(intent)
            requireActivity().finish()
        }

        binding.header.toolbar.addView(profileCard.root)
        binding.header.toolbar.setContentInsetsAbsolute(0, 0)
        binding.header.toolbar.setContentInsetsRelative(0, 0)
    }
}