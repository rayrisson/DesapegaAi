package com.example.desapegaai.ui.chatlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desapegaai.R
import com.example.desapegaai.adapters.ChatAdapter
import com.example.desapegaai.databinding.FragmentChatlistBinding
import com.example.desapegaai.ui.chat.ChatActivity
import koleton.api.hideSkeleton
import koleton.api.isSkeletonShown
import koleton.api.loadSkeleton

class ChatListFragment : Fragment() {

    private var _binding: FragmentChatlistBinding? = null

    private val binding get() = _binding!!
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val chatListViewModel =
            ViewModelProvider(this)[ChatListViewModel::class.java]

        _binding = FragmentChatlistBinding.inflate(inflater, container, false)

        val root: View = binding.root

        initRecyclerView()

        binding.chatsRecyclerView.loadSkeleton(R.layout.chat_item)
        chatListViewModel.getChats()

        chatListViewModel.users.observe(viewLifecycleOwner) {
            if (binding.chatsRecyclerView.isSkeletonShown())
                binding.chatsRecyclerView.hideSkeleton()
            chatAdapter.submitList(it)
        }

        binding.header.toolbar.title = getString(R.string.title_nav_chat)

        return root
    }

    private fun initRecyclerView() {
        chatAdapter = ChatAdapter {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("vendorId", it.id)

            startActivity(intent)
        }

        binding.chatsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}