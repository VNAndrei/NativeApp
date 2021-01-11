package ro.ubbcluj.scs.andreiverdes.beerkeeper.auth.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_login.*
import ro.ubbcluj.scs.andreiverdes.beerkeeper.R


class LoginFragment : Fragment() {
    private lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        setupFragment()
    }

    private fun setupFragment() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                findNavController().navigate(R.id.action_loginFragment_to_lobbyFragment)
            }
        }

        loginButton.setOnClickListener {
            Log.d("da","click")
            viewModel.login(
                username.text.toString(),
                password.text.toString()
            )
        }
    }

}