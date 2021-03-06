package com.rafael.alves.sunsettimes.view.ui

import android.location.Location
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.widget.Toast
import com.rafael.alves.sunsettimes.R
import com.rafael.alves.sunsettimes.model.CityListItem
import com.rafael.alves.sunsettimes.model.SunsetTimes
import com.rafael.alves.sunsettimes.presenter.CityListPresenter
import com.rafael.alves.sunsettimes.presenter.contract.SunsetTimesListener
import com.rafael.alves.sunsettimes.utils.ImageUtils
import com.rafael.alves.sunsettimes.view.adapter.CityListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class CityListActivity : BaseLocationActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mCityListPresenter = CityListPresenter()
    private var mAdapter: CityListAdapter? = null

    //region [ LifeCycle ]
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setUI()
        setNavigationDrawer()
        startFusedLocation()
    }
    //endregion

    //region [ Callbacks ]
    override fun getCurrentLocationSunsetTimes(location: Location?, updateTime: String) {
        if (location != null) {
            mCityListPresenter.getCurrentLocationSunsetTimes(location, object : SunsetTimesListener {
                override fun onSuccess(sunsetTimes: SunsetTimes?) {
                    updateUI(sunsetTimes, location, updateTime)
                }

                override fun onFail() {
                    // TODO - msg
                    Toast.makeText(this@CityListActivity, "Status Code Error", Toast.LENGTH_SHORT).show()
                }

                override fun onRequestStarted() {
                    // show loading
                }

                override fun onRequestFinished() {
                    //hide loading
                }

                override fun onError(e: Throwable) {
                    // TODO - msg
                    Toast.makeText(this@CityListActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
    //endregion

    private fun updateUI(sunsetTimes: SunsetTimes?, location: Location, updateTime: String) {
        val cityListItems = mCityListPresenter.getCityListToAdapter(sunsetTimes, location, getAddress(location), updateTime)

        if (mAdapter == null) {
            mAdapter = CityListAdapter(this, cityListItems, object : CityListAdapter.OnCityClickListener {
                override fun onCityClick(cityListItem: CityListItem) {
                    Toast.makeText(this@CityListActivity, cityListItem.countryCode, Toast.LENGTH_SHORT).show()
                }
            })

            rvCityList.layoutManager = LinearLayoutManager(this)
            rvCityList.adapter = mAdapter
        } else {
            mAdapter!!.updateList(cityListItems)
        }

        getLocationByCityName("São Paulo")
    }

    private fun setUI() {
        ImageUtils.loadImageFromRes(this, R.drawable.background, ivBackground)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    private fun setNavigationDrawer() {
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_camera -> {

            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
