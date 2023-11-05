package com.example.mystoryapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityLoginBinding
import com.example.mystoryapp.helper.ViewModelFactory
import com.example.mystoryapp.model.UserModel
import com.example.mystoryapp.ui.main.MainActivity
import com.example.mystoryapp.ui.register.RegisterActivity


class LoginActivity : AppCompatActivity(){

    private lateinit var binding : ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.emailValid.observe(this) {
            loginValidation(it, viewModel.passwordValid.value!!)
        }

        viewModel.passwordValid.observe(this) {
            loginValidation(viewModel.emailValid.value!!, it)
        }

        playAnimation()

    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val tittletxt = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val messagetxt = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val emailtxt = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.loginEmailEditText, View.ALPHA, 1f).setDuration(500)
        val passtxt = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.loginPasswordEditText, View.ALPHA, 1f).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.registerLayout, View.ALPHA, 1f).setDuration(500)

        val togetherEmail = AnimatorSet().apply {
            playTogether(emailtxt, email)
        }

        val togetherPass = AnimatorSet().apply {
            playTogether(passtxt, password)
        }

        viewModel.getUser().observe(this) {
            if (it.token != "" && it.isLogin != false) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        AnimatorSet().apply {
            playSequentially(tittletxt, messagetxt, togetherEmail, togetherPass, button, register)
            start()
        }

        with(binding) {
            loginEmailEditText.addTextChangedListener(object : TextWatcher {
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

            loginPasswordEditText.addTextChangedListener(object : TextWatcher {
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

        viewModel.errorMsg.observe(this) {
            var message = ""

            if (it == "Unauthorized") {
                message = resources.getString(R.string.wrong_authentication)
            } else if (it != "") {
                message = resources.getString(R.string.failed_login) + " $it"
            }

            if (message != "") {
                AlertDialog.Builder(this).apply {
                    setTitle(R.string.login_failed)
                    setMessage(message)
                    setPositiveButton(R.string.ok) { _, _ -> }
                    create()
                    show()
                }
            }
        }

        viewModel.loading.observe(this) {
            showLoading(it)
        }

        binding.loginButton.setOnClickListener {
            login()
        }
        binding.tvRegisterHyperlink.setOnClickListener{
            register()
        }
    }

    private fun setEmailValidation() {
        viewModel.updateEmailStatus(binding.loginEmailEditText.valid)
    }

    private fun setPasswordValidation() {
        viewModel.updatePasswordStatus(binding.loginPasswordEditText.valid)
    }

    private fun loginValidation(emailValidation: Boolean, passwordValidation: Boolean) {
        binding.loginButton.isEnabled = emailValidation && passwordValidation
        binding.loginButton.changeStatus(emailValidation && passwordValidation)
    }

    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                pbLoading.visibility= View.VISIBLE
            } else {
                pbLoading.visibility = View.INVISIBLE
            }
        }
    }

    private fun login() {
        val email = binding.loginEmailEditText.text.toString()
        val pass = binding.loginPasswordEditText.text.toString()
        viewModel.login(email, pass)
        viewModel.saveSession(UserModel("", email, "","",0.0,0.0))
    }

    private fun register() {
       startActivity(Intent(this, RegisterActivity::class.java))
    }
}