package com.example.findmentorapp.ui.add;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("未登录，请登录");
    }

    public LiveData<String> getText() {
        return mText;
    }
}