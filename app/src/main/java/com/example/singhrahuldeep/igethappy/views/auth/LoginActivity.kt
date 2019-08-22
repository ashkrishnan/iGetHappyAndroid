package com.example.singhrahuldeep.igethappy.views.auth

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.CompoundButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.android.igethappy.R
import com.android.igethappy.databinding.ActivityLoginBinding
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.adapters.CustomPagerAdapter
import com.example.singhrahuldeep.igethappy.interfaces.FacebookInterface
import com.example.singhrahuldeep.igethappy.model.output.LoginResponseModel
import com.example.singhrahuldeep.igethappy.utils.PreferenceKeys
import com.example.singhrahuldeep.igethappy.utils.SharedPrefsHelper
import com.example.singhrahuldeep.igethappy.utils.Utilities
import com.example.singhrahuldeep.igethappy.viewModel.LoginViewModel
import com.example.singhrahuldeep.igethappy.views.FacebookLogin
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity
import com.example.singhrahuldeep.igethappy.views.dashboard.LandingPageActivity
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import org.jetbrains.annotations.Nullable
import retrofit2.Call
import retrofit2.Response

/**
 * Created by Gharjyot Singh
 */

class LoginActivity : BaseActivity(), View.OnClickListener, FacebookInterface, ViewPager.OnPageChangeListener,
    CallBackResult.LoginCallbacks, CompoundButton.OnCheckedChangeListener {


    private var binding: ActivityLoginBinding? = null
    private var callbackManager: CallbackManager? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 9001
    private var isGoogleLoggedIn = false
    private var isFacebookLoggedIn = false
    private var firstName: String? = null
    private var lastName: String? = null
    private var socialLastName: String? = null
    private var email: String? = null
    private var socialId: String? = null
    private var name: String? = null
    private var displayName1: String? = null
    private var profileUrl: Uri? = null
    private var mTermsChecked: Boolean? = false
    var loginViewModel: LoginViewModel? = null
    private var customPageradapter: CustomPagerAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityLoginBinding?

        parentLayout = binding!!.linMain
        callbackManager = CallbackManager.Factory.create()

        binding!!.relLayoutFb.setOnClickListener(this)
        binding!!.relLayoutGoogle.setOnClickListener(this)
        binding!!.tvTerms.setOnClickListener(this)
        binding!!.tabDots.setupWithViewPager(binding!!.pager, true)

        val dataArray = arrayOf<String>(
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry"
        )

        customPageradapter = CustomPagerAdapter(this, dataArray)
        binding!!.pager.adapter = customPageradapter
        binding!!.pager.currentItem = 0
        binding!!.pager.addOnPageChangeListener(this)
        binding!!.checkbox.setOnCheckedChangeListener(this)

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        loginViewModel?.init(this)
        binding!!.viewModel = loginViewModel
        binding!!.executePendingBindings()


        loginViewModel?.list?.observe(this, object : Observer<LoginResponseModel> {
            override fun onChanged(response: LoginResponseModel?) {

            }
        })
        loginViewModel?.isLoading?.observe(this, Observer<Boolean> { isLoading ->
            if (isLoading!!) {
                showDialog()
            } else
                hideDialog()
        })
    }

    override fun onClick(v: View?) {
        Utilities.hideSoftKeyboard(this)
        when (v!!.id) {
            R.id.rel_layout_fb -> {
                if (Utilities.checkInternet(this)) {
                    if (mTermsChecked!!) {
                        binding!!.loginFb.performClick()
                        FacebookLogin(callbackManager, binding!!.loginFb, this)
                    } else {
                        showToast(this.resources.getString(R.string.agree_terms))
                    }
                } else {
                    showToast(this.resources.getString(R.string.no_internet_msg))
                }
            }
            R.id.rel_layout_google -> {
                if (Utilities.checkInternet(this)) {
                    if (mTermsChecked!!) {
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .build()

                        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
                        binding!!.btnGoogleSignIn.performClick()
                        googleSignIn()
                    } else {
                        showToast(this.resources.getString(R.string.agree_terms))
                    }
                } else {
                    showToast(this.resources.getString(R.string.no_internet_msg))
                }
            }
            R.id.tv_terms -> {
                showToast(this.resources.getString(R.string.coming_soon))
            }

        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        mTermsChecked = isChecked
    }

    // Google sign in result
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            updateUI(account)
        } catch (e: ApiException) {
            printDebugLog("signInResult:failed code=", e.statusCode.toString())
            updateUI(null)
        }
    }

    //  This method is used to open google account
    private fun googleSignIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // Signout from google
    private fun googleSignOut() {
        mGoogleSignInClient!!.signOut()
        isGoogleLoggedIn = false
    }

    private fun updateUI(@Nullable account: GoogleSignInAccount?) {
        when (account) {
            null -> isGoogleLoggedIn = false
            else -> {
                email = account.email
                socialId = account.id
                firstName = account.displayName
                name = account.displayName
                displayName1 = account.givenName
                profileUrl = account.photoUrl

                loginViewModel?.doValidateSocialId(socialId!!)
                isGoogleLoggedIn = true
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        } else {
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun facebookLoginSuccess(
        name: String?,
        email: String?,
        id: String?,
        lastName: String?,
        photoUrl: String?
    ) {
        this.firstName = name
        this.lastName = lastName
        this.name = name
        this.socialLastName = lastName
        this.email = email
        this.socialId = id
        isFacebookLoggedIn = true

        loginViewModel?.doValidateSocialId(id!!)
    }

    override fun onPageScrollStateChanged(p0: Int) {
        when (p0) {
            0 -> {
//                img_page1.setImageResource(R.drawable.dot_selected)
//                img_page2.setImageResource(R.drawable.dot)
//                img_page3.setImageResource(R.drawable.dot)
//                img_page4.setImageResource(R.drawable.dot)
            }

            1 -> {
//                img_page1.setImageResource(R.drawable.dot)
//                img_page2.setImageResource(R.drawable.dot_selected)
//                img_page3.setImageResource(R.drawable.dot)
//                img_page4.setImageResource(R.drawable.dot)
            }

            2 -> {
//                img_page1.setImageResource(R.drawable.dot)
//                img_page2.setImageResource(R.drawable.dot)
//                img_page3.setImageResource(R.drawable.dot_selected)
//                img_page4.setImageResource(R.drawable.dot)
            }

            3 -> {
//                img_page1.setImageResource(R.drawable.dot)
//                img_page2.setImageResource(R.drawable.dot)
//                img_page3.setImageResource(R.drawable.dot)
//                img_page4.setImageResource(R.drawable.dot_selected)
            }

            else -> {
            }
        }
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
    }

    override fun onPageSelected(p0: Int) {

    }


    override fun dispose() {

    }

    override fun onResponseError(call: Call<LoginResponseModel>, t: Throwable) {

    }

    override fun onResponseSuccess(response: Response<LoginResponseModel>?) {
        loginViewModel?.isLoading?.postValue(false)
        if (response?.body()?.status.toString() == "200") {
            var prefsHelper = SharedPrefsHelper(this)
            var userID = response!!.body().data!!._id.toString()
            prefsHelper.save(PreferenceKeys.PREF_USER_ID, userID)
            prefsHelper.save(PreferenceKeys.PREF_ISANONYMOUS, response.body().data!!.is_anonymous)
            prefsHelper.save(
                PreferenceKeys.PREF_USER_NAME,
                response.body().data!!.first_name + " " + response.body().data!!.last_name
            )
            prefsHelper.save(PreferenceKeys.PREF_USER_IMAGE, response.body().data!!.profile_image)


            val intent = Intent(this, LandingPageActivity::class.java)
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.putExtra("social_id", socialId)
            // intent.putExtra("email", email)
            intent.putExtra("first_name", firstName)
            intent.putExtra("last_name", lastName)
            startActivity(intent)
        }

    }

    override fun onContinueWithEmailClicked() {
        val intent = Intent(this, LoginEmailActivity::class.java)
        startActivity(intent)

    }

}
