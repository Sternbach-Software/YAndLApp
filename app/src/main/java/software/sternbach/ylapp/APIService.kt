package software.sternbach.ylapp

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import software.sternbach.ylapp.data.CustomWorkerPostResponse
import software.sternbach.ylapp.data.Worker
import java.util.concurrent.TimeUnit

interface APIService {

    @GET("worker-routes/{id}")
    suspend fun getWorkerRoutes(@Path("id") id: Int): Response<Worker>

    @POST("workers/{id}")
    suspend fun postWorkerData(@Path("id") id: Int, @Body data: String): Response<CustomWorkerPostResponse>
}
fun createAPIService(): APIService {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://apimocha.com/shmuly-y-and-l-app/example/")
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .client(OkHttpClient.Builder().connectTimeout(1, TimeUnit.DAYS).build())//TODO what to do about request timing out
        .build()
    return retrofit.create(APIService::class.java)
}
