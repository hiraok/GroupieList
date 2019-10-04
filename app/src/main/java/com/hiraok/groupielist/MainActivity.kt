package com.hiraok.groupielist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val api = Api()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = arrayListOf(1, 2, 4, 5, 9)
        val count = Observable.fromIterable(list)
            .flatMap { id ->
                api.getItem(id).observeOn(Schedulers.io()).toObservable()
            }
            .filter { it }
            .blockingIterable()
            .count()

        showText(count)
    }

    private fun showText(size: Int) {
        if (size > 0) {
            textView.visibility = View.GONE
        } else {
            textView.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    class Api {
        fun getItem(id: Int): Single<Boolean> {
            return Single.create<Boolean> { emitter ->
                if (id % 10 == 0) {
                    emitter.onSuccess(true)
                } else {
                    emitter.onSuccess(false)
                }
            }
        }
    }
}
