package com.example.playground.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.playground.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var biometricPrompt: BiometricPrompt? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.authenticate.setOnClickListener {
            val promptInfo =
                BiometricPrompt.PromptInfo.Builder().setTitle("Biometric title")
                    .setSubtitle("Biometric subtitle")
                    .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                    .build()

            biometricPrompt?.authenticate(promptInfo);
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                binding.authenticate.visibility = View.VISIBLE
                Log.d(
                    "MY_APP_TAG",
                    "App can authenticate using biometrics."
                )
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                binding.authenticate.visibility = View.GONE
                Log.e(
                    "MY_APP_TAG",
                    "No biometric features available on this device."
                )
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                binding.authenticate.visibility = View.GONE
                Log.e(
                    "MY_APP_TAG",
                    "Biometric features are currently unavailable."
                )
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent =
                    Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                        )
                    }
            }

            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                binding.authenticate.visibility = View.GONE
                TODO()
            }

            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                binding.authenticate.visibility = View.GONE
                TODO()
            }

            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                binding.authenticate.visibility = View.GONE
                TODO()
            }
        }

        biometricPrompt = BiometricPrompt(this,
            ContextCompat.getMainExecutor(requireContext()),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int, errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Log.d("********", "onAuthenticationError")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    Log.d("********", "onAuthenticationSucceeded")
                    super.onAuthenticationSucceeded(result)
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        biometricPrompt?.cancelAuthentication()
    }
}