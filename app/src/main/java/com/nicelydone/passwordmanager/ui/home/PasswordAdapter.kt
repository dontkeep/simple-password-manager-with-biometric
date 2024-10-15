package com.nicelydone.passwordmanager.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.nicelydone.passwordmanager.MainViewModel
import com.nicelydone.passwordmanager.databinding.PasswordItemBinding
import com.nicelydone.passwordmanager.model.entity.Password

class PasswordAdapter(val mainViewModel: MainViewModel): ListAdapter<Password, PasswordAdapter.PasswordViewHolder>(DIFF_CALLBACK) {
   class PasswordViewHolder(val binding: PasswordItemBinding): RecyclerView.ViewHolder(binding.root) {
      fun bind(item: Password){
         binding.websiteTextView.text = "Platform : " + item.title
         binding.usernameTextView.text = "Username : " + item.username
      }
   }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder {
      val binding = PasswordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return PasswordViewHolder(binding)
   }

   override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
      val item = getItem(position)
      holder.bind(item)

      holder.binding.deleteButton.setOnClickListener {
         val positionAdapter = holder.adapterPosition
         if(positionAdapter != RecyclerView.NO_POSITION) {
            val itemToDelete = getItem(positionAdapter)
            mainViewModel.delete(itemToDelete)

            Snackbar.make(holder.binding.root, "Password deleted", Snackbar.LENGTH_LONG)
               .setAction("Undo") {
                  mainViewModel.insert(itemToDelete)
               }
               .show()
         }
      }

      holder.itemView.setOnClickListener {
         val action = HomeFragmentDirections.actionNavigationHomeToAddPasswordFragment2(item)
         holder.itemView.findNavController().navigate(action)
      }
   }

   companion object {
      val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Password>() {
         override fun areItemsTheSame(oldItem: Password, newItem: Password): Boolean {
            return oldItem.id == newItem.id
         }

         override fun areContentsTheSame(oldItem: Password, newItem: Password): Boolean {
            return oldItem == newItem
         }
      }
   }
}