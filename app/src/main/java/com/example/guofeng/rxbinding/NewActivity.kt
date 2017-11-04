package com.example.guofeng.rxbinding

import android.os.Bundle
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_new.*
import org.jetbrains.anko.toast

class NewActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        val observable = Observable.create<String> {
            Thread.sleep(5_000)
            it.onNext("I am toast")
        }

        common_button.setOnClickListener {
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::toast)
        }

        lifecycle_button.setOnClickListener {
            observable.bindToLifecycle(this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::toast)
        }
    }
}
