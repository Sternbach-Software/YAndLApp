package software.sternbach.ylapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import android.content.Intent
import android.net.Uri
import software.sternbach.ylapp.data.CONSTANTS.ERROR_STRING


class MapLocationsAdapter(originalList: List<String>) :
    RecyclerView.Adapter<MapLocationsAdapter.ViewHolder>() {

    val workingList = originalList.toMutableList()//for filtering/removing locations from the list once they pass it

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            view.setOnClickListener {
                //invoke Google Maps URL to redirect user to Google Maps
                val itemClicked = workingList[adapterPosition]
                if(itemClicked == ERROR_STRING) Toast.makeText(view.context, "Error connecting to server. Cannot start navigation",Toast.LENGTH_SHORT).show()
                else{
                    val mapsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$itemClicked"))
                    view.context.startActivity(mapsIntent)
                }
            }
            textView = view.findViewById(R.id.text_view)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val mapsLocationListItemLayout = R.layout.maps_location_list_item
        return ViewHolder(
            // Create a new view
            LayoutInflater.from(viewGroup.context)
                .inflate(mapsLocationListItemLayout, viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        //set UI based on data
        viewHolder.textView.text = workingList[position]
    }

    override fun getItemCount(): Int = workingList.size


    //Functions for updating list:

    /**
     * Removes the location at the top of the list (e.g. when the user drives past that location)
     * */
    fun removeFirstLocation(){
        workingList.removeAt(0)
        notifyItemRemoved(0)
    }

    /**
     * Removes a specific location from the list (e.g. if a user is rescheduled to a different location)
     * */
    fun removeLocation(location: String){
        val index = workingList.indexOfFirst { it == location }
        workingList.removeAt(index)
        notifyItemRemoved(index)
    }

    /**
     * Removes all locations from the list which come before [location], including location.
     * Useful for removing all locations until user's current location if the user temporarily
     * lost GPS signal and finds themselves multiple locations ahead of the first location in the
     * list, where the previous two methods will make the task slightly more cumbersome.
     * */
    fun removeFromFirstUntilLocation(location: String){
        val index = workingList.indexOfFirst { it == location }
        workingList.removeAt(index)
        notifyItemRemoved(index)
    }
}