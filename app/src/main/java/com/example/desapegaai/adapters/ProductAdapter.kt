package com.example.desapegaai.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.desapegaai.R
import com.example.desapegaai.data.Product
import com.example.desapegaai.databinding.ProductItemBinding
import com.example.desapegaai.databinding.ProductItemWithFavButtonBinding
import com.example.desapegaai.databinding.ProductItemWithMenuBinding
import java.util.Locale

class ProductAdapter(
    onClickItem: ((Product) -> Unit)? = null,
    onClickFav: ((Product) -> Unit)? = null,
    onClickEdit: ((Product) -> Unit)? = null,
    onClickDelete: ((Product) -> Unit)? = null,
): ListAdapter<Product, RecyclerView.ViewHolder>(DiffCallback) {
    private val clickItem = onClickItem
    private val clickFav = onClickFav
    private val clickEdit = onClickEdit
    private val clickDelete = onClickDelete

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
        if(clickFav != null) {
            return ProductWithFavViewHolder(
                ProductItemWithFavButtonBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        if(clickEdit != null){
            return ProductWithMenuViewHolder(
                ProductItemWithMenuBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        return ProductViewHolder(
            ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is ProductViewHolder -> {
                holder.bind(getItem(position), clickItem)
            }
            is ProductWithFavViewHolder -> {
                holder.bind(getItem(position), clickItem, clickFav)
            }
            is ProductWithMenuViewHolder -> {
                holder.bind(getItem(position), clickItem, clickEdit, clickDelete)
            }
        }

    }

    private fun formatNumber(number: Double): String {
        return "%,.0f".format(Locale.GERMAN, number)
    }

    class ProductViewHolder constructor(
        binding: ProductItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val productName = binding.productName
        private val productValue = binding.productValue
        private val productCard = binding.productCard
        private val rootContext = binding.root.context

        fun bind(product: Product, onClickItem: ((Product) -> Unit)? = null) {
            productName.text = product.name
            productValue.text = rootContext.getString(R.string.value_with_currency, "%,.0f".format(Locale.GERMAN, product.value))
            productCard.setOnClickListener{
                if (onClickItem != null) {
                    onClickItem(product)
                }
            }
        }

    }

    class ProductWithFavViewHolder constructor(
        binding: ProductItemWithFavButtonBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val productName = binding.productName
        private val productValue = binding.productValue
        private val productCard = binding.productCard
        private val cbButton = binding.cbFav
        private val rootContext = binding.root.context

        fun bind(product: Product, onClickItem: ((Product) -> Unit)? = null, onClickFav: ((Product) -> Unit)? = null) {
            productName.text = product.name
            productValue.text = rootContext.getString(R.string.value_with_currency, "%,.0f".format(Locale.GERMAN, product.value))
            productCard.setOnClickListener{
                if (onClickItem != null) {
                    onClickItem(product)
                }
            }

            cbButton.isChecked = true

            cbButton.setOnCheckedChangeListener { compoundButton, b ->
                if (onClickFav != null) {
                    onClickFav(product)
                }
            }
        }

    }

    class ProductWithMenuViewHolder constructor(
        binding: ProductItemWithMenuBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val productName = binding.productName
        private val productValue = binding.productValue
        private val productCard = binding.productCard
        private val menuButton = binding.productMenu
        private val rootContext = binding.root.context

        fun bind(product: Product, onClickItem: ((Product) -> Unit)? = null, onClickEdit: ((Product) -> Unit)? = null, onClickDelete: ((Product) -> Unit)? = null) {
            productName.text = product.name
            productValue.text = rootContext.getString(R.string.value_with_currency, "%,.0f".format(Locale.GERMAN, product.value))
            productCard.setOnClickListener{
                if (onClickItem != null) {
                    onClickItem(product)
                }
            }

            menuButton.setOnClickListener { it ->
                val popup = PopupMenu(rootContext!!, it)
                popup.menuInflater.inflate(R.menu.product_menu, popup.menu)
                popup.setOnMenuItemClickListener {
                    if(it.title == "Deletar" && onClickDelete != null) {
                        onClickDelete(product)
                    }

                    if(it.title == "Editar" && onClickEdit != null) {
                        onClickEdit(product)
                    }
                    true
                }

                popup.show()
            }
        }

    }
}