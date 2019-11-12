package com.example.testdemo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.testdemo.MoviesListData
import com.example.testdemo.R
import com.example.testdemo.utils.State
import kotlinx.android.synthetic.main.item_movies.view.*

class MovieListAdapter(
    private val retry: () -> Unit
) :
    PagedListAdapter<MoviesListData, RecyclerView.ViewHolder>(NewsDiffCallback) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING
    private lateinit var onItemClick: (movie: MoviesListData, position: Int) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) MovieViewHolder.create(parent) else ListFooterViewHolder.create(
            retry,
            parent
        )
    }

    infix fun setOnItemClick(onClick: (movie: MoviesListData, position: Int) -> Unit) {

        this.onItemClick = onClick
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE) {
            (holder as MovieViewHolder).bind(getItem(position))
            holder.itemView.setOnClickListener { onItemClick(getItem(position)!!, position) }
        } else {
            (holder as ListFooterViewHolder).bind(state)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    companion object {
        val NewsDiffCallback = object : DiffUtil.ItemCallback<MoviesListData>() {
            override fun areItemsTheSame(oldItem: MoviesListData, newItem: MoviesListData): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: MoviesListData, newItem: MoviesListData): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

    fun removeData(position: Int) {
        notifyItemRemoved(position)
    }

    class MovieViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        fun bind(data: MoviesListData?) {
            if (data != null) {
                itemView.movie_name.text = data.title
                itemView.movie_desc.text = data.posterPath

            }
        }

        companion object {
            fun create(
                parent: ViewGroup
            ): MovieViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_movies, parent, false)

                return MovieViewHolder(view)
            }
        }
    }
}