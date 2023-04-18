package space.timur.tictactoe.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import space.timur.tictactoe.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private var action: NavDirections? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            onePlayerBtn.setOnClickListener {
                action = MainFragmentDirections.actionMainFragmentToOnePlayerFragment()
                findNavController().navigate(action!!)
            }
            twoPlayersBtn.setOnClickListener {
                action = MainFragmentDirections.actionMainFragmentToTwoPlayersFragment()
                findNavController().navigate(action!!)
            }
        }
    }
}