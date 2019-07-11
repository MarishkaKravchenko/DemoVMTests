package com.example.demovmtests

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.demovmtests.base.HasProgressView
import com.example.demovmtests.base.HasStatusView
import com.example.demovmtests.util.StatusView
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.progress_status_view as progressStatusView

import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, HasStatusView, HasProgressView {

    @Inject
    internal lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> =
        fragmentDispatchingAndroidInjector

    override val statusView: StatusView
        get() = progressStatusView

    override fun showProgress() {
        progressStatusView.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressStatusView.visibility = View.GONE
    }
}