package com.example.mobilenews

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kwabenaberko.newsapilib.NewsApiClient
import com.kwabenaberko.newsapilib.models.Article
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest
import com.kwabenaberko.newsapilib.models.response.ArticleResponse

class NewsViewModel : ViewModel() {

    // MutableLiveData should hold a List<Article>, not a single Article
    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    init {
        fetchNewsTopHeadLines()
    }

    fun fetchNewsTopHeadLines() {
        val newsApiClient = NewsApiClient(Constant.apikey)
        val request = TopHeadlinesRequest.Builder().language("en").build()

        newsApiClient.getTopHeadlines(request, object : NewsApiClient.ArticlesResponseCallback {
            override fun onSuccess(response: ArticleResponse?) {
                // Ensure response and articles list is not null
                response?.articles?.let { articleList ->
                    // Post the full list of articles
                    _articles.postValue(articleList)
                }
            }

            override fun onFailure(throwable: Throwable?) {
                // Handle failure case
                Log.i("NewsApi Failure", throwable?.localizedMessage ?: "Unknown error occurred")
            }
        })
    }
}
