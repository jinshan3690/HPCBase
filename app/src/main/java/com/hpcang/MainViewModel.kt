package com.hpcang

import androidx.databinding.ObservableArrayList
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.hpcang.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val userId = MutableLiveData("")
    val userList = userId.switchMap {
        liveData {
            val users = ObservableArrayList<String>()
            for ( i in 0 until (Math.random()*10).toInt()) {
                users.add((Math.random()*2).toInt().toString())
            }
            emit(users)
        }
    }

    fun setUserId(){
        userId.value = Math.random().toString()
    }


}
