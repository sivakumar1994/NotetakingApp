package com.example.notetakingapp

import android.Manifest
import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.notetakingapp.utils.AppUtils
import com.olam.farmapp.utils.getTimestampString
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

    val noteDetailsListLiveData = MutableLiveData<List<NoteDetail>>()
    val pinnedNoteDetailsListLiveData = MutableLiveData<List<NoteDetail>>()

    var title = MutableLiveData<String>("")
    var content = MutableLiveData<String>()
    var timeStamp = MutableLiveData<String>()
    var imageUriForLoadImage = MutableLiveData<String>()
    var isPinned = MutableLiveData<Boolean>()

    var isSaveButtonClicked = MutableLiveData<Boolean>()
    var isNoteAdapterItemClicked = MutableLiveData<Boolean>()

    var specificNoteData = MutableLiveData<NoteDetail>()
    var isDeleteButtonClicked = MutableLiveData<Boolean>()
    var isPinButtomClicked = MutableLiveData<Boolean>()
    var isMainMenuPinButtomClicked = MutableLiveData<Boolean>()
    var isPinnedStatusUpdated = MutableLiveData<Long>()
    var isNeedToShowLongPressEventToolbar = MutableLiveData<Boolean>()
    var isMainMenuDeleteButtonClicked = MutableLiveData<Boolean>()
    var isDeleteConfirmButtonClicked = MutableLiveData<Boolean>()


     fun onInsertNotesDetail() {
         viewModelScope.launch (Dispatchers.IO){
             val noteDetail = NoteDetail(
                 title =title.value.toString(),
                 content = content.value.toString(),
                 imageUrl= imageUriForLoadImage.value.toString(),
                 timeStamp= AppUtils.getTimestampString()
             )
             mainActivityRepo.insertNotesDetails(noteDetail)
         }
    }

    fun onUpdateNotesDetail(id: Long) {
        viewModelScope.launch (Dispatchers.IO){
            val noteDetail = NoteDetail(
                title =title.value.toString(),
                content = content.value.toString(),
                imageUrl= imageUriForLoadImage.value.toString(),
                timeStamp= AppUtils.getTimestampString(),
                isPinned = isPinned.value == true,
                id = id
            )
            mainActivityRepo.updateNotesDetails(noteDetail)
        }
    }

    fun onDeleteNotesDetail(id : Long) {
        viewModelScope.launch (Dispatchers.IO) {
            mainActivityRepo.deleteNoteDetail(id)
            if(isMainMenuDeleteButtonClicked.value==true) {
                getNotesDetail()
                getPinnedNotesDetail(true)
            }
        }
    }


    fun getNotesDetail(): LiveData<List<NoteDetail>> {
        viewModelScope.launch (Dispatchers.IO){
            mainActivityRepo.getPinnedNotesDetails(false,noteDetailsListLiveData)
        }
        return noteDetailsListLiveData
    }

    fun onUpdatePinStatusNotesDetail(id : Long, isPinEnable: Boolean) :LiveData<Long> {
        viewModelScope.launch (Dispatchers.IO) {
            mainActivityRepo.updatePinStatus(id,isPinEnable,isPinnedStatusUpdated)
        }
        return isPinnedStatusUpdated
    }

    fun getPinnedNotesDetail(isPinned : Boolean): LiveData<List<NoteDetail>> {
        viewModelScope.launch (Dispatchers.IO){
            mainActivityRepo.getPinnedNotesDetails(isPinned,pinnedNoteDetailsListLiveData)
        }
        return pinnedNoteDetailsListLiveData
    }


    fun getNoteDetailById (id : Long) : LiveData<NoteDetail> {
        viewModelScope.launch (Dispatchers.IO) {
            mainActivityRepo.getSpecificNoteDataFromId(id,specificNoteData)
        }
        return specificNoteData
    }

    fun setPhotoPath(path: String, name: String) {
        imageUriForLoadImage.value = path
    }

    fun setData(noteDetail: NoteDetail) {
        noteDetail.let {
            title.value = it.title
            content.value = it.content
            timeStamp.value = it.timeStamp
            isPinned.value= it.isPinned
        }
    }
}