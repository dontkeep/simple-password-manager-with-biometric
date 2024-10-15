package com.nicelydone.passwordmanager.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nicelydone.passwordmanager.MainViewModel
import com.nicelydone.passwordmanager.databinding.FragmentHomeBinding
import com.nicelydone.passwordmanager.helper.ViewModelFactory

class HomeFragment : Fragment() {
   private lateinit var binding: FragmentHomeBinding
   private val mainViewModel: MainViewModel by viewModels {
      ViewModelFactory.getInstance(requireActivity().application)
   }
   private lateinit var adapter: PasswordAdapter

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      binding = FragmentHomeBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      setupRecyclerView()
      setupSearchView()

      mainViewModel.passwordList.observe(viewLifecycleOwner) { items ->
         Log.d("Item", "onViewCreated: $items")
         if (items.isNullOrEmpty()) {
            binding.emptyStateView.visibility = View.VISIBLE
            binding.passwordRecyclerView.visibility = View.GONE
         } else {
            binding.emptyStateView.visibility = View.GONE
            binding.passwordRecyclerView.visibility = View.VISIBLE
            adapter.submitList(items)
         }
      }

      binding.addButton.setOnClickListener {
         val item = null
         val action = HomeFragmentDirections.actionNavigationHomeToAddPasswordFragment2(item)
         findNavController().navigate(action)
      }
   }

   private fun setupRecyclerView() {
      adapter = PasswordAdapter(mainViewModel)
      binding.passwordRecyclerView.layoutManager = LinearLayoutManager(requireContext())
      binding.passwordRecyclerView.adapter = adapter
   }

   private fun setupSearchView() {
      binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
         override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let { searchPasswords(it) }
            return false
         }

         override fun onQueryTextChange(newText: String?): Boolean {
            newText?.let { searchPasswords(it) }
            return false
         }
      })
   }

   private fun searchPasswords(query: String) {
      val filteredPasswords = mainViewModel.passwordList.value?.filter {
         it.title.contains(query, ignoreCase = true) ||
             it.username.contains(query, ignoreCase = true)
      }
      adapter.submitList(filteredPasswords)
   }
}