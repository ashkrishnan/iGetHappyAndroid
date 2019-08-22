package com.example.singhrahuldeep.igethappy.views.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.igethappy.R
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.model.output.FacialResponse
import com.example.singhrahuldeep.igethappy.utils.PreferenceKeys
import com.example.singhrahuldeep.igethappy.utils.SharedPrefsHelper
import com.example.singhrahuldeep.igethappy.viewModel.FacialUploadModel
import com.example.singhrahuldeep.igethappy.viewModel.FacialViewModel
import com.example.singhrahuldeep.igethappy.views.auth.LoginActivity
import com.example.singhrahuldeep.igethappy.views.community.CommunityActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by Gharjyot Singh
 */


class LandingPageActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    CallBackResult.SignUpProfileCallbacks {
    override fun onFirstNameEmpty() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onlastNameEmpty() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNickNameEmpty() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onInvalidFirstName() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onInvalidLastName() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onInvalidNickName() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDummyImageSelected(imageNo: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onValidationsSuccess(firstName: String, lastName: String, nickName: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var tvFeeling: TextView? = null
    var tvWelcomeName: TextView? = null
    var ivCommunity: ImageView? = null
    var ivFaceMood: ImageView? = null
    var tvClickTrackMood: TextView? = null
    private var imageUpload: FacialUploadModel? = null
    private var facialViewModel: FacialViewModel? = null
    var ciProfile: CircleImageView? = null
    var tvName: TextView? = null
    private var prefsHelper: SharedPrefsHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = ""

        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        facialViewModel = ViewModelProviders.of(this).get(FacialViewModel::class.java)
        facialViewModel?.init()

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        toolbar.setNavigationIcon(R.drawable.ic_side_bar_menu_icon)

        val header = navView.getHeaderView(0)

        ciProfile = header.findViewById(R.id.ci_profile)
        tvName = header.findViewById(R.id.tv_name)

        prefsHelper = SharedPrefsHelper(this)
        val name = prefsHelper!!.get(PreferenceKeys.PREF_USER_NAME, "")
        val profile = prefsHelper!!.get(PreferenceKeys.PREF_USER_IMAGE, "dadsa")

        tvName!!.text = name
        if (!profile.isNullOrBlank()) {
            Picasso.with(this).load(profile).fit().centerCrop()
                .placeholder(R.drawable.ic_user_profle)
                .error(R.drawable.ic_user_profle)
                .into(ciProfile)
        } else {
            Picasso.with(this).load(R.drawable.ic_user_profle).fit().centerCrop()
                .placeholder(R.drawable.ic_user_profle)
                .into(ciProfile)
        }

        tvFeeling = findViewById(R.id.tv_feeling)
        tvWelcomeName = findViewById(R.id.tv_wlcm_name)
        tvWelcomeName!!.text = name
        tvClickTrackMood = findViewById(R.id.tv_click_track_mood)
        ivCommunity = findViewById(R.id.iv_community)
        ivFaceMood = findViewById(R.id.iv_face_mood)
        tvClickTrackMood!!.setOnClickListener {

            imageUpload = FacialUploadModel(this, this)
            imageUpload!!.getImage()

        }

        ivCommunity!!.setOnClickListener {

            val intent = Intent(this@LandingPageActivity, CommunityActivity::class.java)
            startActivity(intent)

        }

        facialViewModel?.getData()?.observe(this, object : Observer<FacialResponse> {
            override fun onChanged(response: FacialResponse?) {

                if (response != null) {
                    Log.d("", "" + response)

                    tvFeeling!!.text = response.prediction

                    when (response.prediction) {
                        "Happy" -> {

                            ivFaceMood!!.setBackgroundResource(R.drawable.ic_laughed_mood)
                        }
                        "Sad" -> {
                            ivFaceMood!!.setBackgroundResource(R.drawable.ic_sad_mood)
                        }
                        "Neutral" -> {
                            ivFaceMood!!.setBackgroundResource(R.drawable.ic_sad_mood)
                        }
                        "Fear" -> {
                            ivFaceMood!!.setBackgroundResource(R.drawable.ic_sad_mood)
                        }
                        "Surprise" -> {
                            ivFaceMood!!.setBackgroundResource(R.drawable.ic_excited_mood)
                        }
                        "Angry" -> {
                            ivFaceMood!!.setBackgroundResource(R.drawable.ic_angry_mood)
                        }
                        "Disgust" -> {
                            ivFaceMood!!.setBackgroundResource(R.drawable.ic_sad_mood)
                        }
                    }
                } else {

                }
            }
        })

        facialViewModel?.loading?.observe(this, Observer<Boolean> { isLoading ->

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageUpload!!.onActivityResult(requestCode, resultCode, data)
    }


    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun showToast(message: String) {

        /*  var snackbar = Snackbar.make(parentLayout!!, message, Snackbar.LENGTH_SHORT)
          snackbar.show()*/

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(resId: Int) {

        val snackbar = Snackbar.make(tvFeeling!!, resources.getString(resId), Snackbar.LENGTH_SHORT)
        snackbar.show()

        //  Toast.makeText(this, resources.getString(resId), Toast.LENGTH_SHORT).show()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.landing_page, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_happy_memories -> {
                // Handle the camera action
            }
            R.id.nav_mood_logs -> {

            }
            R.id.nav_my_vents -> {

            }
            R.id.nav_doc -> {

            }
            R.id.nav_logout -> {

                val prefsHelper = SharedPrefsHelper(this)
                prefsHelper.clearAll()
                val intent = Intent(this@LandingPageActivity, LoginActivity::class.java)
                intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onImageSelected(path: String) {

        facialViewModel!!.trackMood(path)
    }


}
