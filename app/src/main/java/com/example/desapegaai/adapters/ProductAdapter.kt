package com.example.desapegaai.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.desapegaai.data.Product
import com.example.desapegaai.databinding.ProductItemBinding

class ProductAdapter(
    onClickItem: ((Product) -> Unit)? = null,
): ListAdapter<Product, RecyclerView.ViewHolder>(DiffCallback) {
//    private var items: List<Product> = ArrayList()

    private val clickItem = onClickItem

    companion object{
        private val DiffCallback = object: DiffUtil.ItemCallback<Product>(){
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductViewHolder(
            ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is ProductViewHolder -> {
                holder.bind(getItem(position), clickItem)
            }
        }

    }

//    override fun getItemCount(): Int {
//        return items.size
//    }

//    fun setList(liveList: List<Product>) {
//        this.items = liveList
//    }

    class ProductViewHolder constructor(
        binding: ProductItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val productName = binding.productName
        private val productValue = binding.productValue
        private val productCard = binding.productCard

        fun bind(product: Product, onClickItem: ((Product) -> Unit)?) {
            productName.text = product.name
            productValue.text = "R$ ${product.value}"
            productCard.setOnClickListener{
                if (onClickItem != null) {
                    onClickItem(product)
                }
            }
        }

    }
}