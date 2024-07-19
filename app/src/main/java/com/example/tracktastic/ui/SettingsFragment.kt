package com.example.tracktastic.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.tracktastic.R
import com.example.tracktastic.databinding.ConfirmDialogBinding
import com.example.tracktastic.databinding.DialogConfirmAuthBinding
import com.example.tracktastic.databinding.EditDialogBinding
import com.example.tracktastic.databinding.FragmentSettingsBinding
import com.example.tracktastic.ui.viemodels.LoginViewModel
import com.example.tracktastic.ui.viemodels.SettingsViewModel


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val settingsViewModel: SettingsViewModel by activityViewModels()
    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel.loadWallpaper()
        settingsViewModel.retrieveDataFromDatabase()


        //settings
        settingsViewModel.usEr.observe(viewLifecycleOwner) {

            //updating the name
            binding.name.setOnClickListener {
                DialogsAndToasts.addEditDialog(
                    requireContext(),
                    R.string.sucessfully_updated,
                    EditDialogBinding.inflate(layoutInflater)
                ) {
                    settingsViewModel.updateUserName(DialogsAndToasts.editText.value.toString())
                }
            }

            //updating the email //showing the email
            binding.email.setOnClickListener {

                val dialog = Dialog(requireContext())
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                val binding = DialogConfirmAuthBinding.inflate(layoutInflater)
                dialog.setCancelable(false)
                dialog.setContentView(binding.root)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                binding.etConfirmPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD

                binding.btnConfirm.setOnClickListener {
                    if (binding.confirmEmail.text.isNotEmpty() &&
                        binding.etNewValue.text.isNotEmpty() &&
                        binding.etConfirmPassword.text.isNotEmpty()
                    )

                        settingsViewModel.updateEmail(
                            binding.confirmEmail.text.toString(),
                            binding.etConfirmPassword.text.toString(),
                            binding.etNewValue.text.toString()
                        )
                    dialog.dismiss()
                    settingsViewModel.logout() {
                        findNavController().navigate(R.id.loginFragment)
                    }

                }
                binding.btnCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }


            //updating password
            binding.changePassword.setOnClickListener {

                val dialog = Dialog(requireContext())
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                val binding = DialogConfirmAuthBinding.inflate(layoutInflater)
                dialog.setCancelable(false)
                dialog.setContentView(binding.root)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                binding.etNewValue.hint = requireContext().getString(R.string.new_password)
                binding.etConfirmPassword.hint = requireContext().getString(R.string.old_password)
                binding.etConfirmPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.etNewValue.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.btnConfirm.setOnClickListener {
                    if (binding.confirmEmail.text.isNotEmpty() &&
                        binding.etNewValue.text.isNotEmpty() &&
                        binding.etConfirmPassword.text.isNotEmpty()
                    )

                        settingsViewModel.updatePassword(
                            binding.confirmEmail.text.toString(),
                            binding.etConfirmPassword.text.toString(),
                            binding.etNewValue.text.toString()
                        )
                    dialog.dismiss()
                    settingsViewModel.logout() {
                        findNavController().navigate(R.id.loginFragment)
                    }


                }
                binding.btnCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }

            //wallpaper
            binding.wallpaperChange.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToDesignFragment())
            }

            //notifications
            binding.notificationSwitch.setOnClickListener {
                if (binding.notificationSwitch.isChecked) {
                    DialogsAndToasts.showToast(R.string.notification_off, requireContext())
                    loginViewModel.notificationPermission(false)
                } else if (!binding.notificationSwitch.isChecked) {
                    DialogsAndToasts.showToast(R.string.notification_on, requireContext())
                    loginViewModel.notificationPermission(true)
                }
            }

            //language
            binding.language.setOnClickListener {
                DialogsAndToasts.createInAppAlert(
                    requireActivity(),
                    R.string.language,
                    R.string.available_languages
                )
            }
            //terms and conditions
            binding.termsAndConditions.setOnClickListener {

                findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToTermsAndConditionsFragment2())
            }
            //privacypolicy = sending something to download
            binding.policy.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToPrivacyPolicyFragment2())
            }

            //faq
            //chat
            //report aissue

        }
        settingsViewModel.firebaseWallpaperUrl.observe(viewLifecycleOwner) {
            //   binding.ivWallpaper.load(viewModel.firebaseWallpaperUrl.value)
            Log.d("firebasewallpa", settingsViewModel.firebaseWallpaperUrl.value!!)
            binding.ivWallpaper.load(R.drawable.testbg)

        }
        settingsViewModel.firebaseAvatarUrl.observe(viewLifecycleOwner) {
            binding.ivAvatar.load(settingsViewModel.firebaseAvatarUrl.value)
        }
        settingsViewModel.currentUser.observe(viewLifecycleOwner) {
            //logout
            binding.logOut.setOnClickListener {
                DialogsAndToasts.confirmDialog(
                    requireContext(),
                    R.string.are_you_sure_you_want_to_log_out,
                    ConfirmDialogBinding.inflate(layoutInflater)
                ) {
                    settingsViewModel.logout() {
                        findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToLoginFragment())

                    }
                }
                //Thread.sleep(1000)
                //  findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToLoginFragment())
            }
            binding.delete.setOnClickListener {
                DialogsAndToasts.confirmDialog(
                    requireContext(),
                    R.string.dialog_account_delete,
                    ConfirmDialogBinding.inflate(layoutInflater)
                ) {
                    settingsViewModel.deleteAccoount(
                        requireContext(),
                        R.string.toast_account_deleted,
                        R.string.toast_something_went_wrong
                    )
                }
                settingsViewModel.logout() {
                    findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToLoginFragment())

                }
                //Thread.sleep(1000)


            }
        }

        /*
                binding.name.setOnClickListener {
                    findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToDesignFragment())
                }

         */
    }


}
