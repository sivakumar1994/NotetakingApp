package com.example.notetakingapp

import android.Manifest
import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


public class MainActivityViewModel(application : Application) : AndroidViewModel(application) {
    var queryImageUrl : String=""

    @Inject
    lateinit var mainActivityRepo: MainActivityRepo


    init {
        (application as NoteTakingApplication).component.inject(this)
    }

    val permissions = arrayOf(Manifest.permission.CAMERA
        ,Manifest.permission.READ_EXTERNAL_STORAGE
        ,Manifest.permission.WRITE_EXTERNAL_STORAGE)
    var imgPath : String = ""
    var imageUri : Uri? = null

    var imageUriForLoadImage = MutableLiveData<String>()



     fun aa() {
         viewModelScope.launch (Dispatchers.IO){
             mainActivityRepo.getSome()
         }

    }

    fun setPhotoPath(path: String, name: String) {
        imageUriForLoadImage.value.let { path }
    }
}