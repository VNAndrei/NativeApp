package ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.shelf

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_beershelf.*
import ro.ubbcluj.scs.andreiverdes.beerkeeper.R
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.Filter


class BeerShelfFragment : Fragment() {
    private lateinit var beerShelfAdapter: BeerShelfAdapter
    private lateinit var beersModel: BeersViewModel
    private val filter = Filter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_beershelf, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupFragment()
    }

    override fun onResume() {
        super.onResume()

    }
    private fun setupFragment() {
        beerShelfAdapter = BeerShelfAdapter(this)

        beersModel = ViewModelProvider(this).get(BeersViewModel::class.java)
        beersModel.refresh(){
            beersModel.beers.observe(viewLifecycleOwner) { items ->
                beerShelfAdapter.values = items
                beerList.visibility = View.VISIBLE;
                progressBar.visibility = View.GONE;
            }
        }

        beersModel.details.observe(viewLifecycleOwner) { details ->
            beerShelfAdapter.details = details
        }
        search.doOnTextChanged { _, _, _, _ ->
            beerShelfAdapter.values = beersModel.applySearchFilter(filter, search.text.toString())
        }
        aleFilter.setOnCheckedChangeListener { _, isChecked ->
            filter.ale = isChecked
            beerShelfAdapter.values = beersModel.applySearchFilter(filter, search.text.toString())
        }
        lagerFilter.setOnCheckedChangeListener { _, isChecked ->
            filter.lager = isChecked
            beerShelfAdapter.values = beersModel.applySearchFilter(filter, search.text.toString())
        }
        hybridFilter.setOnCheckedChangeListener { _, isChecked ->
            filter.hybrid = isChecked
            beerShelfAdapter.values = beersModel.applySearchFilter(filter, search.text.toString())
        }


        addButton.setOnClickListener {
            findNavController().navigate(R.id.action_beerShelfFragment_to_tasteFragment)
        }
        beerList.adapter = beerShelfAdapter
        animateButton()
    }

    private fun animateButton() {


        val animator = ObjectAnimator.ofFloat(addButton,View.ALPHA, 0f,1f);
        animator.repeatCount = 0
        animator.startDelay = 500
        animator.duration = 500

        val animator1 = ObjectAnimator.ofFloat(addButton, View.TRANSLATION_Y, 0f,-50f,0f,-40f,0f,-20f,0f)
        animator1.repeatCount = 0
        animator1.duration = 1750

        val set = AnimatorSet()
        set.play(animator).before(animator1)
        addButton.visibility = View.VISIBLE
        set.start()


    }
}