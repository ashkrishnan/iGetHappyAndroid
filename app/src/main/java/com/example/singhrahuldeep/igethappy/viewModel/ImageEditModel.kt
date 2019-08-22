package com.example.singhrahuldeep.igethappy.viewModel

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.igethappy.R
import com.bumptech.glide.Glide
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.ImageCompress
import com.example.singhrahuldeep.igethappy.imagechooser.api.*
import com.example.singhrahuldeep.igethappy.utils.CheckRuntimePermissions
import com.example.singhrahuldeep.igethappy.utils.FileUtils
import com.example.singhrahuldeep.igethappy.utils.Utilities
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File
/**
 * Created by Gharjyot Singh
 */

class ImageEditModel : ImageChooserListener {

    private val ACTION_CAMERA_REQUEST_CODE = 100
    var baseActivity: BaseActivity? = null
    var imageFile: File? = null
    var mBitmap: Bitmap? = null
    val filePath: String? = null
    var originalFilePath: String? = null
    var thumbnailFilePath: String? = null
    var thumbnailSmallFilePath: String? = null
    var signupProfileCallback: CallBackResult.SignUpProfileCallbacks? = null
    var imageChooserManager: ImageChooserManager? = null
    var image_view_: ImageView? = null
    var dialog: Dialog? = null
    var PERMISSION_REQUEST_CODE = 200

    constructor(baseActivity: BaseActivity, callback: CallBackResult.SignUpProfileCallbacks) {
        this.baseActivity = baseActivity
        this.signupProfileCallback = callback
    }


    fun getImage() {
        if (Utilities.checkInternet(baseActivity!!)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (CheckRuntimePermissions.checkMashMallowPermissions(
                        baseActivity,
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                        ), PERMISSION_REQUEST_CODE
                    )
                ) {
                    selectImage()
                }
            } else {
                selectImage()
            }
        } else {
            baseActivity!!.showToast(R.string.no_internet_msg)
        }

    }

    private fun selectImage() {

        dialog = Dialog(baseActivity)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.custom_pictureoption_dialog)
        dialog!!.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //post_title
        val tvCamera = dialog!!.findViewById<TextView>(R.id.tv_camera) as TextView
        val tvGallery = dialog!!.findViewById<TextView>(R.id.tv_gallery) as TextView
        val tvCancel = dialog!!.findViewById<TextView>(R.id.tv_cancel) as TextView

        tvCamera.setOnClickListener {
            takePicture()

        }

        tvGallery.setOnClickListener {
            openGallery()
        }

        tvCancel.setOnClickListener {

            dialog!!.dismiss()

        }
        dialog!!.show()
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        baseActivity!!.startActivityForResult(intent, ACTION_CAMERA_REQUEST_CODE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        baseActivity!!.startActivityForResult(intent, ChooserType.REQUEST_PICK_PICTURE_OR_VIDEO)
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == AppCompatActivity.RESULT_OK) {
            //Log.i(TAG, "Chooser Type: " + chooserType);
            if (requestCode == ACTION_CAMERA_REQUEST_CODE) {
                if (data != null && data.extras.get("data") != null) {
                    dialog!!.dismiss()
                    val bitmap = data.extras.get("data") as Bitmap
                    val uri = ImageCompress.getImageUri(baseActivity!!, bitmap)
                    handleUri(uri)
                }
            } else if (requestCode == ChooserType.REQUEST_PICK_PICTURE_OR_VIDEO ||
                requestCode == ChooserType.REQUEST_PICK_PICTURE
            ) {
                dialog!!.dismiss()
                if (data != null && data.getData() != null) {
                    val uri = data.getData()
                    handleUri(uri)
                } else {

                    val fileObj = File(filePath)
                    val uri = Uri.fromFile(fileObj)
                    if (uri != null) {
                        handleUri(uri)

                    } else {
                        if (imageChooserManager == null) {
                            reinitializeImageChooser()
                        }
                        imageChooserManager!!.submit(requestCode, data);
                    }
                }
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                dialog!!.dismiss()
                val result = CropImage.getActivityResult(data)
                if (resultCode == AppCompatActivity.RESULT_OK) {

                    val resultUri = result.getUri()
                    var path = ImageCompress.getRealPath(resultUri!!, baseActivity)
                    //uploadToS3 = File(path)
                    //  uploadFileToS3()

                    handleCroppedURI(resultUri);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    dialog!!.dismiss()
                    val error = result.getError()
                    baseActivity!!.showToast(error.message!!)

                }
            } /*else if (requestCode == TRIM_VIDEO_CODE) {
                *//*croppedVideoUri = data.getData();
                int currentPosition = data . getIntExtra ("position", 0);
                setVideoImage(currentPosition);*//*
            }*/

        } else if (resultCode == AppCompatActivity.RESULT_CANCELED) {
            if (dialog != null)
                dialog!!.dismiss()
        }

    }


    private fun handleCroppedURI(resultUri: Uri) {

        val picturePath = FileUtils.getPath(baseActivity, resultUri)
        imageFile = File(picturePath!!)
        mBitmap = BitmapFactory.decodeFile(picturePath)

        // hit callback here
        signupProfileCallback!!.onImageSelected(picturePath)

    }


    private fun reinitializeImageChooser() {
        imageChooserManager =
            object : ImageChooserManager(baseActivity, ChooserType.REQUEST_PICK_PICTURE_OR_VIDEO, true) {
                override fun onProcessedVideo(video: ChosenVideo) {

                }

                override fun onProcessedVideos(videos: ChosenVideos) {

                }
            }
        val bundle = Bundle()
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true)

        imageChooserManager!!.setExtras(bundle)
        imageChooserManager!!.setImageChooserListener(this)
        imageChooserManager!!.reinitialize(filePath)
    }

    private fun handleUri(mSelectedImageUri: Uri?) {
        if (mSelectedImageUri == null) {
            Log.i("MainActivity", "imageUri is null");
        }

        //startActivityForResult(getIntent(activity, cls), CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        CropImage.activity(mSelectedImageUri!!).start(baseActivity!!)

    }


    override fun onImageChosen(image: ChosenImage?) {
        baseActivity!!.runOnUiThread(Runnable {
            originalFilePath = image!!.filePathOriginal
            thumbnailFilePath = image.getFileThumbnail()
            thumbnailSmallFilePath = image.filePathOriginal
            //pbar.setVisibility(View.GONE);
            if (image != null) {
                loadImage(image_view_!!, image.filePathOriginal)
            } else {
            }
        })
    }

    override fun onError(reason: String?) {

    }

    override fun onImagesChosen(images: ChosenImages?) {

    }

    fun removeImage() {


    }

    private fun loadImage(iv: ImageView, path: String?) {
        //get image bitmap to get image code
        if (path != null && path.length > 0) {
            Glide.with(baseActivity!!)
                .load(path)
                .into(iv)


        }
    }


}
