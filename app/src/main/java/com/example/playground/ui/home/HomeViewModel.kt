package com.example.playground.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _testList = MutableLiveData<ArrayList<Int>>().apply {
        value = arrayListOf(1, 2, 3)
    }

    val testList: LiveData<ArrayList<Int>> = _testList;
}