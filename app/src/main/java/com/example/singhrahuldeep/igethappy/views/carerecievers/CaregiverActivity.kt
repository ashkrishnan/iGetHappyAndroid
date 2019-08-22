package com.example.singhrahuldeep.igethappy.views.carerecievers

import android.content.Intent
import android.view.View
import com.android.igethappy.R
import com.android.igethappy.databinding.ActivityCaregiverBinding
import com.example.singhrahuldeep.igethappy.views.chatbot.ChatBotActivity
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity
/**
 * Created by Gharjyot Singh
 */

class CaregiverActivity : BaseActivity(), View.OnClickListener {
    private var binding: ActivityCaregiverBinding? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_caregiver
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityCaregiverBinding?
        binding!!.btnContinue.setOnClickListener(this)
        binding!!.btnSkip.setOnClickListener(this)
        binding!!.ivBack.setOnClickListener(this)
    }

    override fun dispose() {

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_continue -> {
                val intent = Intent(this@CaregiverActivity, AddCareRecieverActivity::class.java)
                intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }

            R.id.btn_skip -> {
                val intent = Intent(this@CaregiverActivity, ChatBotActivity::class.java)
                intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }

            R.id.iv_back -> {
                val intent = Intent(this@CaregiverActivity, ChatBotActivity::class.java)
                intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@CaregiverActivity, ChatBotActivity::class.java)
        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

}
