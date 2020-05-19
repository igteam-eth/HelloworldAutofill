package com.ethernom.helloworldautofill

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ListAdapter(private val list: List<UserData>, mCallback: AdapterCallback)
    : RecyclerView.Adapter<UserDataViewHolder>() {

    var mDataCallback = mCallback;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDataViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UserDataViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: UserDataViewHolder, position: Int) {
        val movie: UserData = list[position]
        holder.itemView.setOnClickListener {
            mDataCallback.itemClickCallBack(position)
        }
        holder.bind(movie)
    }

    override fun getItemCount(): Int = list.size

    interface AdapterCallback {
        fun itemClickCallBack(position: Int)
    }
}

class UserDataViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item, parent, false)) {
    private var mTitleView: TextView? = null
    private var mYearView: TextView? = null


    init {
        mTitleView = itemView.findViewById(R.id.txt_username)
        mYearView = itemView.findViewById(R.id.txt_password)
    }

    fun bind(movie: UserData) {
        mTitleView?.text = movie.displayName
        mYearView?.text = movie.username
    }

}

data class UserData( val displayName: String,val username: String, val password: String)
