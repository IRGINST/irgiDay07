package com.example.irgilatihan07.data.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.irgilatihan07.data.ApiConfig
import com.example.irgilatihan07.data.CustomerReviewsItem
import com.example.irgilatihan07.data.PostReviewResponse
import com.example.irgilatihan07.data.ResponseRestaurant
import com.example.irgilatihan07.data.Restaurant
import com.example.irgilatihan07.data.ReviewAdapter
import com.example.irgilatihan07.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val layoutManager = LinearLayoutManager(this)

        binding.rvReview.layoutManager = layoutManager
        val itemDecor = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecor)

        findRestaurant()

        binding.btnSend.setOnClickListener {
            if (binding.edReview.text != null) {
                postReview(binding.edReview.text.toString())
            }
        }
    }

    private fun postReview(review: String) {
        val client = ApiConfig.getApiService().postReview(RESTAURANT_ID, "Dicoding", review)

        client.enqueue(object : Callback<PostReviewResponse> {
            override fun onResponse(
                call: Call<PostReviewResponse>,
                response: Response<PostReviewResponse>
            ) {
                if (response.isSuccessful){
                    if (response.body() != null){
                        setReviewData(response.body()!!.customerReviews)
                    }
                } else {
                    if (response.body() != null){
                        Log.e(TAG, "onFailure: ${response.body()!!.message}")
                    }
                }
            }

            override fun onFailure(call: Call<PostReviewResponse>, t: Throwable) {
               Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private fun findRestaurant() {
        val client = ApiConfig.getApiService().getRestaurant(RESTAURANT_ID)

        client.enqueue(object : Callback<ResponseRestaurant>{
            override fun onResponse(
                call: Call<ResponseRestaurant>,
                response: Response<ResponseRestaurant>
            ) {
                if (response.isSuccessful){
                    setRestaurantData(response.body()!!.restaurant)
                    setReviewData(response.body()!!.restaurant.customerReviews)
                } else {
                    if (response.body() != null){
                        Log.e(TAG, "onFailure: ${response.body()!!.message}")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseRestaurant>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private fun setRestaurantData(restaurant: Restaurant) {
        binding.title.text = restaurant.name
        binding.textView2.text = restaurant.description
        Glide.with(this@MainActivity)
            .load("https://restaurant-api.dicoding.dev/images/large/${restaurant.pictureId}")
            .into(binding.ivPic)
    }

    private fun setReviewData(customerReviews: List<CustomerReviewsItem>) {
        val listReview = mutableListOf<String>()

        customerReviews.forEach {
            listReview.add("${it.review}\n-${it.name}")
        }

        val adapter = ReviewAdapter()
        adapter.submitList(customerReviews)
        binding.rvReview.adapter = adapter
        binding.edReview.setText("")
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val RESTAURANT_ID = "uewq1zg2zlskfw1e867"
    }
}