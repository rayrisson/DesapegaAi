package com.example.desapegaai.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.desapegaai.R
import com.example.desapegaai.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val favoritesViewModel =
            ViewModelProvider(this)[FavoritesViewModel::class.java]

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textFavorites
        favoritesViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

//        binding.header.toolbar.removeView(binding.header.homeSearchbar)
        binding.header.toolbar.title = getString(R.string.title_nav_favorites)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}