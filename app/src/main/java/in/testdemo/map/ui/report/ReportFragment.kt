package `in`.testdemo.map.ui.report

import `in`.testdemo.map.R
import `in`.testdemo.map.utils.ConstantsUtil
import `in`.testdemo.map.utils.ConstantsUtil.REQUEST_CAMERA
import `in`.testdemo.map.utils.ConstantsUtil.REQUEST_GALLERY
import `in`.testdemo.map.utils.ImageUtil
import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_report.*
import java.io.IOException


/**
 * Created by karthi-2322 on 03,December,2018
 */
class ReportFragment : Fragment(), View.OnClickListener {

    private var pickImgRequestCode: Int = -1

    private var frontImgBitmap: Bitmap? = null
    private var backImgBitmap: Bitmap? = null
    private var leftImgBitmap: Bitmap? = null
    private var rightImgBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View = inflater.inflate(R.layout.fragment_report, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTempImage()
        front_btn.setOnClickListener(this)
        back_btn.setOnClickListener(this)
        left_btn.setOnClickListener(this)
        right_btn.setOnClickListener(this)
        next_btn.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id){
            R.id.next_btn ->{
                if(TextUtils.isEmpty(cmd_txt.text)) {
                    cmd_txt.error = getString(R.string.empty_alert)
                    return;
                }else if(frontImgBitmap == null){
                    Toast.makeText(context, getString(R.string.alert_front_img_missing), Toast.LENGTH_SHORT).show()
                }else if(backImgBitmap == null){
                    Toast.makeText(context, getString(R.string.alert_back_img_missing), Toast.LENGTH_SHORT).show()
                }else if(leftImgBitmap == null){
                    Toast.makeText(context, getString(R.string.alert_left_img_missing), Toast.LENGTH_SHORT).show()
                }else if(rightImgBitmap == null){
                    Toast.makeText(context, getString(R.string.alert_right_img_missing), Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, getString(R.string.complint_accepted), Toast.LENGTH_SHORT).show()
                    setTempImage()
                    findNavController().navigateUp()
                }

            }

            else ->{
                checkCameraPermission()
                pickImgRequestCode = v.id
                showPictureDialog()
            }
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.CAMERA),
                ConstantsUtil.CAMERA_PERMISSION_REQUEST_CODE
            )
            return
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ConstantsUtil.LOCATION_PERMISSION_REQUEST_CODE) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED ->
                    showPictureDialog()
                PackageManager.PERMISSION_DENIED -> {

                }
            }
        }
    }


    private fun setTempImage() {
        val thumbImage = context?.let { ImageUtil().getThumbImage(it) }
        front_img.setImageBitmap(thumbImage)
        back_img.setImageBitmap(thumbImage)
        left_img.setImageBitmap(thumbImage)
        right_img.setImageBitmap(thumbImage)
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(context)
        pictureDialog.setTitle(getString(R.string.pick_img))
        val pictureDialogItems = arrayOf(getString(R.string.pick_from_gallery), getString(R.string.pick_from_camera))
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CANCELED) {
            return
        }
        if (requestCode == REQUEST_GALLERY) {
            if (data != null) {
                val contentURI = data.data
                try {
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(activity!!.contentResolver, contentURI)
                    setImageBitmap( ImageUtil().getResizedBitmap(bitmap,400,400))
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(context, getString(R.string.failed), Toast.LENGTH_SHORT).show()
                }
            }

        } else if (requestCode == REQUEST_CAMERA) {
            val thumbnail = data!!.extras!!.get(getString(R.string.data)) as Bitmap?
            setImageBitmap(thumbnail?.let {ImageUtil().getResizedBitmap(it,400,400) })
        }
    }


    private fun setImageBitmap(bitmap: Bitmap?) {
        when (pickImgRequestCode){
            R.id.front_btn ->{
                frontImgBitmap = bitmap
                front_img.setImageBitmap(bitmap)
            }

            R.id.back_btn ->{
                backImgBitmap = bitmap
                back_img.setImageBitmap(bitmap)
            }

            R.id.left_btn ->{
                leftImgBitmap = bitmap
                left_img.setImageBitmap(bitmap)
            }

            R.id.right_btn ->{
                rightImgBitmap = bitmap
                right_img.setImageBitmap(bitmap)
            }
        }
    }

}