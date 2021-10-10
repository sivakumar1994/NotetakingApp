package com.example.notetakingapp

import android.R.attr
import android.annotation.SuppressLint
import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.my_toolbar.*
import androidx.appcompat.view.menu.MenuPopupHelper

import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.security.AccessController.getContext
import android.R.attr.button
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.notetakingapp.CameraConstant.REQ_CAPTURE
import com.example.notetakingapp.CameraConstant.RES_IMAGE
import com.olam.farmapp.utils.compressImageFile
import kotlinx.android.synthetic.main.fragment_notes_details.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null

    lateinit var mainActivityViewModel : MainActivityViewModel

    lateinit var popupMenu : PopupMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = pin_notes_host_fragment as NavHostFragment
        val graphInflater = navHostFragment.navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.nav_graph_pin_notes)

        navController = navHostFragment.navController
        navGraph.startDestination = R.id.noteFragmentDest
        navController!!.setGraph(navGraph)

        popupMenu = PopupMenu(this@MainActivity, img_save)
        popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu())
        setForceShowIcon(popupMenu)
        img_add.setOnClickListener {
            navController!!.navigate(R.id.notesDetailsFragmentDest)
            img_save.visibility = View.VISIBLE
            img_add.visibility = View.GONE
            img_more.visibility = View.VISIBLE
        }
        img_more.setOnClickListener {
            popupMenu.show();
        }
        popupMenu.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.action_add_image -> {
                    // Toast.makeText(this@MainActivity, item.title, Toast.LENGTH_SHORT).show()
                    checkCameraAndStoragePermission();
                }
            }
            true
        }

        initViewModel()
        setObserver()

        img_save.setOnClickListener {
            mainActivityViewModel.isSaveButtonClicked.value =true
            img_save.visibility = View.GONE
            img_add.visibility = View.VISIBLE
            img_more.visibility = View.GONE
        }

    }

    private fun setObserver() {
       mainActivityViewModel.isNoteAdapterItemClicked.observe(this,{
           img_save.visibility = View.VISIBLE
           img_add.visibility = View.GONE
           img_more.visibility = View.VISIBLE
           mainActivityViewModel.isSaveButtonClicked.value = false
       })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Back to main screen so toolbar item will be show based on below condition
        if (navController?.currentDestination?.id == R.id.noteFragmentDest) {
            img_save.visibility = View.GONE
            img_add.visibility = View.VISIBLE
            img_more.visibility = View.GONE
        }
    }

    private fun initViewModel(){
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }


    // This method help to force to show image in popup toolbar
    fun setForceShowIcon(popupMenu: PopupMenu) {
        try {
            val fields: Array<Field> = popupMenu.javaClass.declaredFields
            for (field in fields) {
                if ("mPopup" == field.getName()) {
                    field.setAccessible(true)
                    val menuPopupHelper: Any = field.get(popupMenu)
                    val classPopupHelper = Class.forName(
                        menuPopupHelper
                            .javaClass.name
                    )
                    val setForceIcons: Method = classPopupHelper.getMethod(
                        "setForceShowIcon", Boolean::class.javaPrimitiveType
                    )
                    setForceIcons.invoke(menuPopupHelper, true)
                    break
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun checkCameraAndStoragePermission() {
        showCameraDialogue()
    }
    private fun showCameraDialogue() {
        if (isPermissionsAllowed(mainActivityViewModel.permissions, true, REQ_CAPTURE)) {
            chooseImage()
        }
    }
    private fun chooseImage() {
        startActivityForResult(getPickImageIntent(), RES_IMAGE)
    }



    fun isPermissionsAllowed(
        permissions: Array<String>,
        shouldRequestIfNotAllowed: Boolean = false,
        requestCode: Int = -1
    ): Boolean {
        var isGranted = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                isGranted = ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
                if (!isGranted)
                    break
            }
        }
        if (!isGranted && shouldRequestIfNotAllowed) {
            if (requestCode.equals(-1))
                throw RuntimeException("Send request code in third parameter")
            requestRequiredPermissions(permissions, requestCode)
        }

        return isGranted
    }
    fun requestRequiredPermissions(permissions: Array<String>, requestCode: Int) {
        val pendingPermissions: ArrayList<String> = ArrayList()
        permissions.forEachIndexed { index, permission ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_DENIED
            )
                pendingPermissions.add(permission)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val array = arrayOfNulls<String>(pendingPermissions.size)
            pendingPermissions.toArray(array)
            requestPermissions(array, requestCode)
        }
    }

    fun isAllPermissionsGranted(grantResults: IntArray): Boolean {
        var isGranted = true
        for (grantResult in grantResults) {
            isGranted = grantResult.equals(PackageManager.PERMISSION_GRANTED)
            if (!isGranted)
                break
        }
        return isGranted
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 545 || requestCode == 544) {
            if (grantResults.isNotEmpty()) {
                var granted = false
                grantResults.forEach {
                    granted = it == PackageManager.PERMISSION_GRANTED
                }

            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Permission needed to proceed further",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        } else {
            when (requestCode) {
                REQ_CAPTURE -> {
                    if (isAllPermissionsGranted(grantResults)) {
                        chooseImage()
                    } else {
                        Toast.makeText(
                            this,
                            getString(R.string.permission_not_granted),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun getPickImageIntent(): Intent? {
        var chooserIntent: Intent? = null

        var intentList: MutableList<Intent> = ArrayList()

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri())

        intentList = addIntentsToList(this, intentList, pickIntent)
        intentList = addIntentsToList(this, intentList, takePhotoIntent)

        if (intentList.size > 0) {
            chooserIntent = Intent.createChooser(
                intentList.removeAt(intentList.size - 1),
                getString(R.string.select_capture_image)
            )
            chooserIntent!!.putExtra(
                Intent.EXTRA_INITIAL_INTENTS,
                intentList.toTypedArray<Parcelable>()
            )
        }

        return chooserIntent
    }

    private fun setImageUri(): Uri {
        val folder = File("${getExternalFilesDir("notesimages")}")
        folder.mkdirs()

        val file = File(folder, "Image_${System.currentTimeMillis()}.jpg")
        if (file.exists())
            file.delete()
        file.createNewFile()
        mainActivityViewModel.imageUri = FileProvider.getUriForFile(
            applicationContext,
            BuildConfig.APPLICATION_ID + getString(R.string.file_provider_name),
            file
        )
        mainActivityViewModel.imgPath = file.absolutePath
        return mainActivityViewModel.imageUri!!
    }

    private fun addIntentsToList(
        context: Context,
        list: MutableList<Intent>,
        intent: Intent
    ): MutableList<Intent> {
        val resInfo = context.packageManager.queryIntentActivities(intent, 0)
        for (resolveInfo in resInfo) {
            val packageName = resolveInfo.activityInfo.packageName
            val targetedIntent = Intent(intent)
            targetedIntent.setPackage(packageName)
            list.add(targetedIntent)
        }
        return list
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            when (requestCode) {
                RES_IMAGE -> {
                    if (resultCode == Activity.RESULT_OK) {
                        handleImageRequest(data)
                    }
                }
            }
        }
    private fun handleImageRequest(data: Intent?) {
        val exceptionHandler = CoroutineExceptionHandler { _, t ->
            t.printStackTrace()
            Toast.makeText(
                this,
                t.localizedMessage ?: getString(R.string.some_err),
                Toast.LENGTH_SHORT
            ).show()
        }

        GlobalScope.launch(Dispatchers.Main + exceptionHandler) {

            if (data?.data != null) {     //Photo from gallery
                mainActivityViewModel.imageUri = data.data
                mainActivityViewModel.queryImageUrl = mainActivityViewModel.imageUri?.path!!
                mainActivityViewModel.queryImageUrl = compressImageFile(
                    mainActivityViewModel.queryImageUrl,
                    false,
                    mainActivityViewModel.imageUri!!
                )

            } else {
                mainActivityViewModel.queryImageUrl = mainActivityViewModel.imgPath
                compressImageFile(
                    mainActivityViewModel.queryImageUrl,
                    uri = mainActivityViewModel.imageUri!!
                )
            }
            mainActivityViewModel.imageUri = Uri.fromFile(File(mainActivityViewModel.queryImageUrl))
            var file = File(mainActivityViewModel.imageUri!!.path)
            if (file.exists()) {
                Log.d("--------->", file.path)

                mainActivityViewModel.setPhotoPath(file.path, file.name)
            }
        }
    }


}