package com.example.mystoryapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityRegisterBinding
import com.example.mystoryapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvLoginHyperlink.setOnClickListener {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
        }
        playAnimation()

        registerViewModel.emailValid.observe(this) {
            loginValidation(it, registerViewModel.passwordValid.value!!)
        }

        registerViewModel.passwordValid.observe(this) {
            loginValidation(registerViewModel.emailValid.value!!, it)
        }

        registerViewModel.statusMessage.observe(this) {
            var message = ""
            var title = resources.getString(R.string.register_failed)

            when {
                it == "success" -> {
                    message = resources.getString(R.string.register_success_message)
                    title = resources.getString(R.string.register_success)
                    startActivity(
                        Intent(this, LoginActivity::class.java)
                    )
                }

                it == "Bad Request" -> {
                    message = resources.getString(R.string.invalid_authentication)
                }

                it != "" -> {
                    message = resources.getString(R.string.failed_register) + " $it"
                }
            }

            if (message != "") {
                AlertDialog.Builder(this).apply {
                    setTitle(title)
                    setMessage(message)
                    setPositiveButton(R.string.ok) { _, _ -> }
                    create()
                    show()
                }
            }
        }

        registerViewModel.loading.observe(this) {
            showLoading(it)
        }

        // Listener
        with(binding) {
            registerEmailEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable?) {}

                override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    setEmailValidation()
                }
            })

            registerPasswordEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable?) {}

                override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    setPasswordValidation()
                }
            })
        }

        binding.registerButton.setOnClickListener {
            register()
        }
        binding.tvLoginHyperlink.setOnClickListener { finish() }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val tittletxt =
            ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val messagetxt =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val nametxt = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.nameEditText, View.ALPHA, 1f).setDuration(500)
        val emailtxt =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val email =
            ObjectAnimator.ofFloat(binding.registerEmailEditText, View.ALPHA, 1f).setDuration(500)
        val passtxt =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.registerPasswordEditText, View.ALPHA, 1f)
            .setDuration(500)
        val button =
            ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginLayout, View.ALPHA, 1f).setDuration(500)

        val togetherName = AnimatorSet().apply {
            playTogether(nametxt, name)
        }

        val togetherEmail = AnimatorSet().apply {
            playTogether(emailtxt, email)
        }

        val togetherPass = AnimatorSet().apply {
            playTogether(passtxt, password)
        }

        AnimatorSet().apply {
            playSequentially(
                tittletxt,
                messagetxt,
                togetherName,
                togetherEmail,
                togetherPass,
                button,
                login
            )
            start()
        }
    }

    private fun setEmailValidation() {
        registerViewModel.updateEmailStatus(binding.registerEmailEditText.valid)
    }

    private fun setPasswordValidation() {
        registerViewModel.updatePasswordStatus(binding.registerPasswordEditText.valid)
    }

    private fun loginValidation(emailValidation: Boolean, passwordValidation: Boolean) {
        binding.registerButton.isEnabled = emailValidation && passwordValidation
        binding.registerButton.changeStatus(emailValidation && passwordValidation)
    }

    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                pbLoading.visibility = View.VISIBLE
            } else {
                pbLoading.visibility = View.INVISIBLE
            }
        }
    }

    private fun register() {
        registerViewModel.register(
            binding.nameEditText.text.toString(),
            binding.registerEmailEditText.text.toString(),
            binding.registerPasswordEditText.text.toString()
        )
    }

}