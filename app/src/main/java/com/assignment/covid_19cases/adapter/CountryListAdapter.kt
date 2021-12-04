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
class CountryListAdapter(private val listener:(String)->Unit): ListAdapter<CountriesDetail, CountryListAdapter.ViewHolder>(DiffCallback()) {
    var previous=""
    var current=""
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view) {
        private val alphabetIndex:TextView=itemView.findViewById(R.id.country_first_alphabet)
        private val countryName:TextView=itemView.findViewById(R.id.current_location_country_name_recy)
        private val flag:ImageView=itemView.findViewById(R.id.current_location_flag)
        init {
            itemView.setOnClickListener {
                listener.invoke(getItem(adapterPosition).alpha2code)
            }
        }
        fun onBind(item: CountriesDetail) {
            if (previous != current){
                alphabetIndex.visibility=View.VISIBLE
                alphabetIndex.text= current
            }
            Glide.with(itemView)
                .load(itemView.context.getString(R.string.flag_url,item.alpha2code.lowercase()))
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