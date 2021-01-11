package ro.ubbcluj.scs.andreiverdes.beerkeeper.lobby

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_lobby.*
import ro.ubbcluj.scs.andreiverdes.beerkeeper.R
import ro.ubbcluj.scs.andreiverdes.beerkeeper.auth.data.AuthRepository
import ro.ubbcluj.scs.andreiverdes.beerkeeper.auth.login.LoginViewModel

class LobbyFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lobby, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!AuthRepository.isLoggedIn)
            findNavController().navigate(R.id.action_lobbyFragment_to_loginFragment)
        setupFragment()
    }

    private fun setupFragment() {

        leaveButton.setOnClickListener {
            AuthRepository.logout()
            findNavController().navigate(R.id.action_lobbyFragment_to_loginFragment)
        }
        checkShelfButton.setOnClickListener {  findNavController().navigate(R.id.action_lobbyFragment_to_beerShelfFragment) }
    }

}