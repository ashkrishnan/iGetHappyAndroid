package com.example.singhrahuldeep.igethappy.views.carerecievers

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Environment
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.igethappy.R
import com.android.igethappy.databinding.ActivityAddCareRecieverBinding
import com.example.singhrahuldeep.igethappy.adapters.AvatarImageAdapter
import com.example.singhrahuldeep.igethappy.adapters.HorizontalTextAdapter
import com.example.singhrahuldeep.igethappy.adapters.ImageAdapter
import com.example.singhrahuldeep.igethappy.interfaces.CareReceiverCallback
import com.example.singhrahuldeep.igethappy.model.input.RelationModel
import com.example.singhrahuldeep.igethappy.model.output.AddCareReceiverResponse
import com.example.singhrahuldeep.igethappy.model.output.DeleteCareReceiverResponse
import com.example.singhrahuldeep.igethappy.utils.AppConstants
import com.example.singhrahuldeep.igethappy.utils.FileUtils
import com.example.singhrahuldeep.igethappy.utils.Utilities
import com.example.singhrahuldeep.igethappy.viewModel.AddCareReceiverViewModel
import com.example.singhrahuldeep.igethappy.viewModel.DeleteCareReceiverViewModel
import com.example.singhrahuldeep.igethappy.views.chatbot.ChatBotActivity
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity
import java.io.File
/**
 * Created by Gharjyot Singh
 */


class AddCareRecieverActivity : BaseActivity(), View.OnTouchListener, CareReceiverCallback, View.OnClickListener {

    var binding: ActivityAddCareRecieverBinding? = null
    var relationArray = ArrayList<RelationModel>()
    var avatarImageAdapter: AvatarImageAdapter? = null
    var imageAdapter: ImageAdapter? = null
    var textAdapter: HorizontalTextAdapter? = null
    var viewModel: AddCareReceiverViewModel? = null
    var deleteViewModel: DeleteCareReceiverViewModel? = null
    var careReceiverArray = ArrayList<AddCareReceiverResponse.Data>()
    var dialog: Dialog? = null
    var isMinor = true
    var relation = ""
    var imageId = 0
    var ifSkipped = false
    var isDelete = false
    var itemPosition = 0

    private var photos = arrayOf(
        R.drawable.ic_man_avatar_icon,
        R.drawable.ic_man_red_icon,
        R.drawable.ic_angel_icon,
        R.drawable.ic_earmuffs_icon,
        R.drawable.ic_elf_icon
    )

    private var relationStrings = arrayOf(
        AppConstants.FATHER,
        AppConstants.Mother,
        AppConstants.Brother,
        AppConstants.Sister,
        AppConstants.Friend,
        AppConstants.Spouse
    )

    override fun getLayoutId(): Int {
        return R.layout.activity_add_care_reciever
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initViews() {

        binding = viewDataBinding as ActivityAddCareRecieverBinding
        parentLayout = binding!!.cllMain
        setUpModel()

        avatarImageAdapter = AvatarImageAdapter(this, photos, this)
        textAdapter = HorizontalTextAdapter(this, relationArray, this)
        imageAdapter = ImageAdapter(this, careReceiverArray, this)

        binding!!.rvCharacter.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false))
        binding!!.rvCharacter.setAdapter(avatarImageAdapter)

        binding!!.rvRelations.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false))
        binding!!.rvRelations.setAdapter(textAdapter)

        binding!!.rvCareRecievers.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false))

        binding!!.tvSave.setOnClickListener(this)
        binding!!.ivBack.setOnClickListener(this)

        viewModel = ViewModelProviders.of(this).get(AddCareReceiverViewModel::class.java)
        viewModel?.init(this)

        deleteViewModel = ViewModelProviders.of(this).get(DeleteCareReceiverViewModel::class.java)
        deleteViewModel?.init(this)

        binding!!.viewmodel = viewModel

        binding!!.rbYes.isChecked = true

        viewModel?.getData()?.observe(this, object : Observer<AddCareReceiverResponse.Data> {
            override fun onChanged(response: AddCareReceiverResponse.Data?) {

                if (response != null) {
                    clearText()
                    openConsentDialog(response.first_name)
                    binding!!.rvCareRecievers
                    careReceiverArray.add(response)
                    binding!!.rvCareRecievers.adapter = imageAdapter
                } else
                    showToast(R.string.something_went_wrong)
            }
        })

        viewModel?.message!!.observe(this, Observer<String> { message ->

            showToast(message)
        })


        viewModel?.loading?.observe(this, Observer<Boolean> { isLoading ->
            if (isLoading!!) {
                showDialog()
            } else
                hideDialog()
        })

        deleteViewModel?.getData()?.observe(this, object : Observer<DeleteCareReceiverResponse> {
            override fun onChanged(response: DeleteCareReceiverResponse?) {

                if (response != null) {
                    careReceiverArray.removeAt(itemPosition)
                    imageAdapter!!.notifyDataSetChanged()
                    showToast(R.string.carereceiver_removed)
                } else
                    showToast(R.string.something_went_wrong)
            }
        })

        deleteViewModel?.message!!.observe(this, Observer<String> { message ->

            showToast(message)
        })

        deleteViewModel?.loading?.observe(this, Observer<Boolean> { isLoading ->
            if (isLoading!!) {
                showDialog()
            } else
                hideDialog()
        })


        binding!!.rbNo.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked)
                isMinor = false
        }

        binding!!.rbYes.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked)
                isMinor = true

        }

        binding!!.cllMain.setOnTouchListener(this)

    }


    private fun openConsentDialog(name: String?) {

        dialog = Dialog(baseActivity)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.dialog_consent)
        dialog!!.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val consentText = resources.getString(R.string.email_sent_to)
        val afterText = resources.getString(R.string.for_consent)
        val space = " "

        val dialogText = "$consentText$space$name$space$afterText"

        val tvCancel = dialog!!.findViewById<TextView>(R.id.tv_title) as TextView
        val tvHeading = dialog!!.findViewById<TextView>(R.id.tv_message) as TextView

        if (!dialogText.isNullOrBlank()) {
            tvHeading.setText(dialogText)
        }

        tvCancel.setOnClickListener { view ->
            dialog!!.dismiss()

        }
        dialog!!.show()
    }

    fun clearText() {

        binding!!.etRecieverName.setText("")
        binding!!.etEmailAddress.setText("")
        binding!!.etPhoneNo.setText("")
    }

    fun setUpModel() {
        for (relationName in relationStrings) {
            val model = RelationModel()
            model.relation = relationName
            model.isSelected = false
            relationArray.add(model)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        Utilities.hideKeypad(v!!)
        return true
    }

    override fun itemSelected(position: Int) {

        var isParent = false

        for (index in relationArray.indices) {

            val model = RelationModel()

            if (position == 0 || position == 1) {
                isParent = true
            }

            if (index == position) {
                model.isSelected = true
                model.relation = relationArray[position].relation
                relation = relationArray[position].relation
                relationArray.set(position, model)

            } else {
                model.isSelected = false
                model.relation = relationArray[index].relation
                relationArray[index] = model
            }
        }

        textAdapter!!.setData(relationArray)

        if (isParent) {
            ifSkipped = false
            isDelete = false
            openConfirmationDialog(getString(R.string.legal_guardian))
        }
    }

    fun openConfirmationDialog(strTitle: String) {

        val dialog = Dialog(this@AddCareRecieverActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_alert_dialog)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvMessage = dialog.findViewById<TextView>(R.id.tv_message) as TextView
        tvMessage.text = strTitle

        val tvCancel = dialog.findViewById<TextView>(R.id.tv_cancel) as TextView
        val tvOk = dialog.findViewById<TextView>(R.id.tv_ok) as TextView
        tvOk.text = resources.getText(R.string.yes)
        tvCancel.text = resources.getText(R.string.no)

        tvOk.setOnClickListener { view ->

            if (isDelete) {
                if (Utilities.checkInternet(this))
                    deleteViewModel!!.deleteCareReceiver(careReceiverArray[itemPosition]._id!!)
                else
                    showToast(getString(R.string.no_internet_msg))

            } else if (ifSkipped) {
                val intent = Intent(this@AddCareRecieverActivity, ChatBotActivity::class.java)
                intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }

            dialog.dismiss()
        }

        tvCancel.setOnClickListener {

            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onClick(view: View?) {

        when (view!!.id) {
            R.id.tv_save -> {
                ifSkipped = true
                openConfirmationDialog(getString(R.string.skip_carereceiver))

            }

            R.id.iv_back -> {
                ifSkipped = true
                openConfirmationDialog(getString(R.string.skip_carereceiver))

            }
        }
    }


    override fun setAvatar(position: Int) {
        imageId = position
        binding!!.ivImage.setImageResource(photos[position])
    }


    override fun getName(): String {
        return binding!!.etRecieverName.text.toString().trim()
    }

    override fun getPhone(): String {
        return binding!!.etPhoneNo.text.toString().trim()
    }

    override fun getEmail(): String {
        return binding!!.etEmailAddress.text.toString().trim()
    }

    override fun getMinorStatus(): String {

        if (isMinor)
            return AppConstants.YES
        else
            return AppConstants.NO
    }

    override fun getRelationship(): String {
        return relation
    }

    override fun getProfileImage(): String {

        val bm = getBitmapFromVectorDrawable(this, getVectorDrawables())

        val dir = File(Environment.getExternalStorageDirectory().toString() + File.separator + "drawable")

        if (!dir.exists()) {
            dir.mkdirs()
        }

        val path = FileUtils.saveBitmapToFile(
            dir,
            "avatar_${System.currentTimeMillis()}.png",
            bm,
            Bitmap.CompressFormat.PNG,
            100
        );

        return path

    }

    private fun getVectorDrawables(): Int {
        when (imageId) {
            0 -> {
                return R.drawable.ic_man_avatar_icon
            }
            1 -> {
                return R.drawable.ic_man_red_icon
            }
            2 -> {
                return R.drawable.ic_angel_icon
            }
            3 -> {
                return R.drawable.ic_earmuffs_icon
            }
            4 -> {
                return R.drawable.ic_elf_icon
            }
        }
        return 0

    }

    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        var drawable = ContextCompat.getDrawable(context, drawableId)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable!!).mutate()
        }

        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    override fun deleteCareReceiver(position: Int) {

        itemPosition = position
        isDelete = true
        ifSkipped = false
        openConfirmationDialog(getString(R.string.delete_confirmation))

    }

    override fun connectedToInternet(): Boolean {
        return Utilities.checkInternet(this)
    }

    override fun setConnectionError() {
        showToast(R.string.no_internet_msg)
    }

    override fun setValidationError(type: String) {

        when (type) {
            AppConstants.NAME_EMPTY -> {
                showToast(getString(R.string.name_empty))
            }
            AppConstants.EMAIL_EMPTY -> {
                showToast(getString(R.string.email_empty))
            }
            AppConstants.INVALID_EMAIL -> {
                showToast(getString(R.string.invalid_email))
            }
            AppConstants.PHONE_EMPTY -> {
                showToast(getString(R.string.empty_phone))
            }
            AppConstants.PHONE_LENGTH -> {
                showToast(getString(R.string.phone_length))
            }
            AppConstants.MINOR_STATUS -> {
                showToast(getString(R.string.minor_status))
            }
            AppConstants.RELATION_NO_SELECTED -> {
                showToast(getString(R.string.no_relation))
            }
        }
    }

    override fun dispose() {

    }


}
