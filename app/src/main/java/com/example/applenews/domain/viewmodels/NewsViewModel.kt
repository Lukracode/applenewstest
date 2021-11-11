package com.example.applenews.domain.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.applenews.NewsApplication
import com.example.applenews.models.Article
import com.example.applenews.models.NewsResponse
import com.example.applenews.data.repository.NewsRepository
import com.example.applenews.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class NewsViewModel(
    app: Application,
    val newsRepository: NewsRepository
) : AndroidViewModel(app) {

//    Top News

    val topNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var topNewsPage = 1
    var topNewsResponse: NewsResponse? = null

    init {
        getTopNews("apple")
    }

    fun getTopNews(topicKey: String) = viewModelScope.launch {
        safeTopNewsCall(topicKey)
    }

    private fun handleTopNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                topNewsPage++
                if(topNewsResponse == null) {
                    topNewsResponse = resultResponse
                } else {
                    val oldArticles = topNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(topNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeTopNewsCall(topicKey: String) {
        topNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = newsRepository.getTopNews(topicKey, topNewsPage)
                topNews.postValue(handleTopNewsResponse(response))
            } else {
                topNews.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> topNews.postValue(Resource.Error("Network Failure"))
                else -> topNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }



//    All News

    val allNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var allNewsPage = 1
    var allNewsResponse: NewsResponse? = null

    init {
        getAllNews("apple")
    }

    fun getAllNews(topicKey: String) = viewModelScope.launch {
        safeAllNewsCall(topicKey)
    }

    private fun handleAllNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                allNewsPage++
                if(allNewsResponse == null) {
                    allNewsResponse = resultResponse
                } else {
                    val oldArticles = allNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(allNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeAllNewsCall(topicKey: String) {
        allNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = newsRepository.getAllNews(topicKey, allNewsPage)
                allNews.postValue(handleAllNewsResponse(response))
            } else {
                allNews.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> allNews.postValue(Resource.Error("Network Failure"))
                else -> allNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }



//    CRUD Room

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }



//    Internet Connection

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }


}












