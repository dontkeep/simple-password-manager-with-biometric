package com.nicelydone.passwordmanager.ui.addpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nicelydone.passwordmanager.MainViewModel
import com.nicelydone.passwordmanager.R
import com.nicelydone.passwordmanager.databinding.FragmentAddPasswordBinding
import com.nicelydone.passwordmanager.helper.BiometricUtil
import com.nicelydone.passwordmanager.model.entity.Password
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class AddPasswordFragment : Fragment() {

   private lateinit var binding: FragmentAddPasswordBinding
   private val viewModel: MainViewModel by viewModels()
   private var isEdit: Boolean = false
   private var isDetail: Boolean = false

   private val args: AddPasswordFragmentArgs by navArgs()
   private var password: Password? = null

   private val key = "1234567890123456" // ngasal
   private val iv = ByteArray(12)

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      binding = FragmentAddPasswordBinding.inflate(layoutInflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
      bottomNav.visibility = View.GONE

      password = args.password
      if (password != null) {
         isDetail = true
         populateFields(password!!)
         setEditable(false)
         binding.generatePasswordButton.visibility = View.GONE
         binding.saveButton.text = "Edit"
      }

      binding.saveButton.setOnClickListener {
         if (isDetail && !isEdit) {
            // Switch to edit mode
            isEdit = true
            setEditable(true)
            binding.saveButton.text = "Update"
            binding.generatePasswordButton.visibility = View.VISIBLE
         } else {
            saveOrUpdatePassword()
         }
      }

      binding.generatePasswordButton.setOnClickListener {
         val generatedPassword = generatePassword()
         binding.passwordEditText.setText("")
         binding.passwordEditText.setText(generatedPassword)
      }
   }

   private fun populateFields(password: Password) {
      binding.websiteEditText.setText(password.title)
      binding.usernameEditText.setText(password.username)

      BiometricUtil.setupBiometricAuthentication(
         requireContext(),
         this,
         onSuccess = {
            val decryptedPassword = decryptPassword(password.password)
            binding.passwordEditText.setText(decryptedPassword)
         },
         onFailure = {
            findNavController().navigateUp()
         },
         onError = {
            findNavController().navigateUp()
         }
      )
   }

   override fun onDestroyView() {
      super.onDestroyView()
      // Show BottomNavigationView
      val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
      bottomNav.visibility = View.VISIBLE
   }

   private fun setEditable(editable: Boolean) {
      binding.websiteEditText.isEnabled = editable
      binding.usernameEditText.isEnabled = editable
      binding.passwordEditText.isEnabled = editable
   }

   private fun saveOrUpdatePassword() {
      val website = binding.websiteEditText.text.toString()
      val username = binding.usernameEditText.text.toString()
      val passwordText = binding.passwordEditText.text.toString()

      if (website.isBlank() || username.isBlank() || passwordText.isBlank()) {
         Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
         return
      }

      val encryptedPassword = encryptPassword(passwordText)
      val passwordData = Password(
         id = password?.id ?: 0,
         title = website,
         username = username,
         password = encryptedPassword
      )

      if (isEdit && isDetail) {
         viewModel.update(passwordData)
      } else {
         viewModel.insert(passwordData)
      }
      findNavController().navigateUp() // Go back to the previous screen
   }

   private fun generatePassword(): String {
      val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#\$%^&*()_-+=<>?"
      val random = SecureRandom()
      val password = StringBuilder()
      for (i in 0 until 12) { // Password length of 12
         password.append(chars[random.nextInt(chars.length)])
      }
      return password.toString()
   }

   private fun encryptPassword(password: String): String {
      val secureRandom = SecureRandom()
      secureRandom.nextBytes(iv) // Generate a new random IV for each encryption

      val cipher = Cipher.getInstance("AES/GCM/NoPadding")
      val keySpec = SecretKeySpec(key.toByteArray(), "AES")
      val gcmSpec = GCMParameterSpec(128, iv)
      cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec)
      val encryptedBytes = cipher.doFinal(password.toByteArray())

      // Combine IV and encrypted data
      val combined = iv + encryptedBytes
      return android.util.Base64.encodeToString(combined, android.util.Base64.DEFAULT)
   }

   private fun decryptPassword(encryptedPassword: String): String {
      val decodedBytes = android.util.Base64.decode(encryptedPassword, android.util.Base64.DEFAULT)

      // Extract IV and encrypted data
      val extractedIv = decodedBytes.copyOfRange(0, 12)
      val encryptedBytes = decodedBytes.copyOfRange(12, decodedBytes.size)

      val cipher = Cipher.getInstance("AES/GCM/NoPadding")
      val keySpec = SecretKeySpec(key.toByteArray(), "AES")
      val gcmSpec = GCMParameterSpec(128, extractedIv)
      cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec)
      return String(cipher.doFinal(encryptedBytes))
   }



   companion object {
      const val EXTRA_PASSWORD = "extra_password"
   }
}