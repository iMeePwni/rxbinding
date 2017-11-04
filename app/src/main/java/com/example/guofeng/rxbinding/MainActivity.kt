package com.example.guofeng.rxbinding

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxAdapterView
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RxView.clicks(anti_shake)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe {
                    toast("you clicked ${anti_shake.text}")
                }

        RxView.longClicks(anti_shake)
                .subscribe {
                    toast("you long clicked ${anti_shake.text}")
                }

        val list = List(40) {
            "string $it"
        }

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list)
        list_view.adapter = adapter

        RxAdapterView.itemClicks(list_view)
                .subscribe {
                    toast("you clicked position $it")
                }

        RxAdapterView.itemLongClicks(list_view)
                .subscribe {
                    toast("you clicked item ${list[it]}")
                }

        RxCompoundButton.checkedChanges(is_agree)
                .subscribe {
                    login.isEnabled = it
                }

        val emailAddress = arrayListOf("@qq.com", "@gmail.com", "@outlook.com", "@gcall.com")
        val emailAdapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1)
        email_address_list.adapter = emailAdapter

        RxTextView.textChanges(email_address)
                .debounce(600, TimeUnit.MILLISECONDS)
                .map {
                    it.toString()
                }.subscribeOn(Schedulers.io())
                .map {
                    if (!TextUtils.isEmpty(it)) {
                        emailAddress.map { suffix ->
                            it + suffix
                        }
                    } else {
                        Collections.emptyList()
                    }
                }.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    emailAdapter.clear()
                    emailAdapter.addAll(it)
                    emailAdapter.notifyDataSetChanged()
                }

        RxView.clicks(new_activity)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe {
                    startActivity<NewActivity>()
                }
    }
}
