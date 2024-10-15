package com.nicelydone.passwordmanager.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nicelydone.passwordmanager.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
   private lateinit var binding: FragmentDashboardBinding

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      binding = FragmentDashboardBinding.inflate(LayoutInflater.from(context), container, false)

      return binding.root
   }
}