package com.assignment.covid_19cases.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.assignment.covid_19cases.R
import com.assignment.covid_19cases.pojo.CountriesDetail
import com.bumptech.glide.Glide


private const val TAG = "CountryListAdapter"
class CountryListAdapter: ListAdapter<CountriesDetail, CountryListAdapter.ViewHolder>(DiffCallback()) {
    var previous=""
    var current=""
    inner class ViewHolder(private val view:View):RecyclerView.ViewHolder(view) {
        val alphabetIndex:TextView=itemView.findViewById(R.id.country_first_alphabet)
        val countryName:TextView=itemView.findViewById(R.id.current_location_country_name_recy)
        val flag:ImageView=itemView.findViewById(R.id.current_location_flag)

        fun onBind(item: CountriesDetail) {
            if (previous != current){
                alphabetIndex.visibility=View.VISIBLE
                alphabetIndex.text= current
            }
            val sf1 = String.format("https://flagcdn.com/h40/%s.png",
                item.alpha2code.lowercase())
            Glide.with(itemView)
                .load(sf1)
                .error(R.drawable.ic_launcher_background)
                .into(flag)
            countryName.text=item.name


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.country_alphabet,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position==0){
            current= getItem(position).name[0].toString()
        }else{
            current= getItem(position).name[0].toString()
            previous=getItem(position-1).name[0].toString()
        }
        holder.onBind(getItem(position))
    }


}

class DiffCallback: DiffUtil.ItemCallback<CountriesDetail>() {
    override fun areItemsTheSame(oldItem: CountriesDetail, newItem: CountriesDetail): Boolean {
        return oldItem.name==newItem.name
    }

    override fun areContentsTheSame(oldItem: CountriesDetail, newItem: CountriesDetail): Boolean {
        return newItem==oldItem
    }


}