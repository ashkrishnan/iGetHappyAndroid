package com.example.singhrahuldeep.igethappy.views.chatbot

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.igethappy.R
import com.android.igethappy.databinding.ActivityChatBotBinding
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.model.output.ChatBotResponseModel
import com.example.singhrahuldeep.igethappy.utils.PreferenceKeys
import com.example.singhrahuldeep.igethappy.utils.SharedPrefsHelper
import com.example.singhrahuldeep.igethappy.viewModel.ChatBotViewModel
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity
import com.example.singhrahuldeep.igethappy.views.dashboard.LandingPageActivity
import retrofit2.Call
import retrofit2.Response

/**
 * Created by Gharjyot Singh
 */


class ChatBotActivity : BaseActivity(), CallBackResult.ChatBotCallbacks {

    var binding: ActivityChatBotBinding? = null
    private var chatBotViewModel: ChatBotViewModel? = null
    private var prefsHelper: SharedPrefsHelper? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_chat_bot
    }

    override fun initViews() {

        binding = viewDataBinding as ActivityChatBotBinding
        parentLayout = binding!!.rlTop
        prefsHelper = SharedPrefsHelper(this)

        val name = prefsHelper!!.get(PreferenceKeys.PREF_USER_NAME, "")
        binding!!.tvTitle.text = "${getString(R.string.letschat)}, ${name}"
        binding!!.tvChatmessage.text = "Hi ${name}, ${getString(R.string.chat_welcome)}"

        chatBotViewModel = ViewModelProviders.of(this).get(ChatBotViewModel::class.java)
        chatBotViewModel!!.init(this)

        initializeListeners()

        chatBotViewModel?.chatBot()

        chatBotViewModel!!.list?.observe(this, object : Observer<ChatBotResponseModel> {
            override fun onChanged(response: ChatBotResponseModel?) {

            }
        })
        chatBotViewModel!!.isLoading.observe(this, Observer<Boolean> { isLoading ->
            if (isLoading!!) {
                showDialog()
            } else
                hideDialog()
        })

        binding!!.tvSkip.setOnClickListener {
            val intent = Intent(this@ChatBotActivity, LandingPageActivity::class.java)
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    fun initializeListeners() {
        binding!!.tvStartchat.setOnClickListener {

            showToast(getString(R.string.coming_soon))
        }

        binding!!.tvSkip.setOnClickListener {


        }

        binding!!.tvSend.setOnClickListener {

            showToast(getString(R.string.coming_soon))
        }
    }

    override fun dispose() {

    }

    override fun onResponseError(call: Call<ChatBotResponseModel>, t: Throwable) {

    }

    override fun onResponseSuccess(response: Response<ChatBotResponseModel>?) {
        if (isLoading!!) {
            showDialog()
        } else
            hideDialog()
    }

    override fun onResponseSuccessError(message: String?) {

    }

    override fun onMessageSent() {

    }

}
