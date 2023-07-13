package com.example.project01.viewModels;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;

import com.example.project01.apiServices.ApiService;
import com.example.project01.apiServices.ApiServiceGenerator;
import com.example.project01.mvp.model.Photo;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class fViewModel {
    private Disposable mDisposable;
    private ObservableArrayList<Photo> list = new ObservableArrayList<>();
    private ObservableBoolean isSuccess = new ObservableBoolean();

//    public void getPhoto(){
//        ApiService.apiService.listPhoto().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Photo>>() {
//            @Override
//            public void onSubscribe(@NonNull Disposable d) {
//                mDisposable = d;
//            }
//
//            @Override
//            public void onNext(@NonNull List<Photo> photos) {
//                list.addAll(photos);
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//                isSuccess.set(false);
//            }
//
//            @Override
//            public void onComplete() {
//                isSuccess.set(true);
//
//            }
//        });
//    }

    public Disposable getDisposable() {
        return mDisposable;
    }

    public void setDisposable(Disposable mDisposable) {
        this.mDisposable = mDisposable;
    }

    public ObservableArrayList<Photo> getList() {
        return list;
    }

    public void setList(ObservableArrayList<Photo> list) {
        this.list = list;
    }

    public ObservableBoolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(ObservableBoolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
