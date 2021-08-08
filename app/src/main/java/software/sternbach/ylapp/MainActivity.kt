package software.sternbach.ylapp

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity) //inflate layout file to screen
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        GlobalScope.launch { getDestinationsFromAPIAndUpdateUI(recyclerView) }
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private suspend fun getDestinationsFromAPIAndUpdateUI(recyclerView: RecyclerView) {
        val request = createAPIService().getWorkerRoutes(1)
        if (request.code() != 200) {
            val errorString = request.errorBody()?.string()
            Toast.makeText(this@MainActivity, "Error occured: $errorString", Toast.LENGTH_LONG)
                .show()
        } else {
            val worker = request.body()!!
            recyclerView.adapter = MapLocationsAdapter(worker.routes)
            withContext(Dispatchers.Main) {//coroutine equivalent of java runOnUiThread {
                findViewById<TextView>(R.id.worker_name).text = "Hello, ${worker.name}!"
                findViewById<CircularProgressIndicator>(R.id.progress_indicator).visibility =
                    View.GONE
                findViewById<LinearLayout>(R.id.main_activity_linear_layout).visibility =
                    View.VISIBLE
            }
        }
    }
    //TODO get data from bluetooth
    //TODO add location listener to 1) post data when user gets to
    // next destination/leaves current destination and 2) remove destinations from list
    //TODO possibly add cute little truck icon next to list and as the user navigates through
    // the destinations, the icon slowly moves down the list. Otherwise, add some signifier
    // like "Next destination:    $location
    //       Upcoming locations:
    //                            ----------"
    //                            ----------"
    //                            ----------"
    //                            ----------"
}