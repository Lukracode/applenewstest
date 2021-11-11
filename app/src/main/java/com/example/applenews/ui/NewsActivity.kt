package com.example.applenews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.applenews.R
import com.example.applenews.data.database.ArticleDatabase
import com.example.applenews.domain.viewmodels.NewsViewModel
import com.example.applenews.domain.viewmodels.NewsViewModelProviderFactory
import com.example.applenews.data.repository.NewsRepository
import com.example.applenews.databinding.ActivityNewsBinding
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    lateinit var binding : ActivityNewsBinding
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        binding.bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }
}
