package com.atar.mango.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.atar.mango.R
import com.atar.mango.ui.SingleEvent
import com.atar.mango.ui.events.NavigateEvent

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    /**
     * Data
     */
    private lateinit var mNavigation: NavController
    private lateinit var mViewModel: HomeViewModel
    private val mScreenObserver = Observer<SingleEvent<NavigateEvent>> {
        it?.getContentIfNotHandled()?.let {navigateEvent ->
            mNavigation.navigate(navigateEvent.id)
        }
    }

    /**
     * AppCompatActivity Methods
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(am_toolbar)

        mViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        mViewModel.getNavigateEvent().observe(this, mScreenObserver)
        mNavigation = findNavController(R.id.am_navigation)
    }

    override fun onDestroy() {
        mViewModel.getNavigateEvent().removeObserver(mScreenObserver)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}
