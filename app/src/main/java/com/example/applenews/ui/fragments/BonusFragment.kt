package com.example.applenews.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.applenews.R
import com.example.applenews.domain.viewmodels.NewsViewModel
import com.example.applenews.ui.NewsActivity
import com.example.applenews.util.Constants.Companion.MY_LINKEDIN
import com.example.applenews.util.Constants.Companion.MY_PHONE
import com.example.applenews.util.Constants.Companion.MY_TELEGRAM
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.fragment_article.fab
import kotlinx.android.synthetic.main.fragment_bonus.*

class BonusFragment : Fragment(R.layout.fragment_bonus) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bPhone.setOnClickListener {
            makeCall()
        }

        bTelegram.setOnClickListener {
            openTelegram()
        }

        bLinked.setOnClickListener {
            openLinkedIn()
        }
    }



    private fun makeCall() {
        val intent = Intent(Intent.ACTION_DIAL);
        intent.data = Uri.parse("tel:$MY_PHONE")
        startActivity(intent)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun openLinkedIn() {

        webViewBonus.visibility = View.VISIBLE
        webViewBonus.webViewClient = WebViewClient()
        webViewBonus.apply {
            loadUrl(MY_LINKEDIN)
            settings.javaScriptEnabled = true
        }
    }

    private fun openTelegram() {
        val telegram = Intent(Intent.ACTION_VIEW, Uri.parse(MY_TELEGRAM))
        telegram.setPackage("org.telegram.messenger")
        startActivity(telegram)
    }
}