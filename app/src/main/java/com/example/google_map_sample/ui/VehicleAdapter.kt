package com.example.google_map_sample.ui


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.google_map_sample.R
import com.example.google_map_sample.databinding.ItemVehicleBinding
import com.example.google_map_sample.model.Vehicle


class VehicleAdapter :  androidx.recyclerview.widget.ListAdapter<Vehicle, RecyclerView.ViewHolder>(diffCallback)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder
            = VehicleViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_vehicle, parent, false
        ))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var vehicle = getItem(position)
        if (vehicle != null) {
            (holder as VehicleViewHolder).bindTo(vehicle);
        }
    }

    companion object {
        /**
         * This diff callback informs the PagedListAdapter how to compute list differences when new
         * PagedLists arrive.
         * <p>
         * When you add a Cheese with the 'Add' button, the PagedListAdapter uses diffCallback to
         * detect there's only a single item difference from before, so it only needs to animate and
         * rebind a single view.
         *
         * @see DiffUtil
         */
        private val diffCallback = object : DiffUtil.ItemCallback<Vehicle>() {
            override fun areItemsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean =
                oldItem.id == newItem.id

            /**
             * Note that in kotlin, == checking on data classes compares all contents, but in Java,
             * typically you'll implement Object#equals, and use it to compare object contents.
             */
            override fun areContentsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean =
                oldItem == newItem
        }
    }
}
 class VehicleViewHolder( val binding : ItemVehicleBinding) :
         RecyclerView.ViewHolder(
            binding.root
         ) {
        private val txtType = itemView.findViewById<TextView>(R.id.tvVehicleType)
        var vehicle:Vehicle? = null
     fun bindTo(vehicle: Vehicle) {
         this.vehicle  = vehicle
         txtType.text = vehicle.type
         with(binding) {
             vehicleitem = vehicle
             executePendingBindings()
         }
     }

 }
