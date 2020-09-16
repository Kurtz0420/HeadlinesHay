package com.client.headlineshay.utils


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Pagination class to add more items to the list when reach the last item.
 */
abstract class PaginationScrollListener
/**
 * Supporting only LinearLayoutManager for now.
 *
 * @param layoutManager
 */
(var layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    private val VISIBLE_THRESHOLD = 19 //optimal for 20 items/page

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val totalItemCount = layoutManager.itemCount
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()



        if(!isLoading() && !isLastPage()){
            if (totalItemCount <= (lastVisibleItemPosition + 1) && totalItemCount > VISIBLE_THRESHOLD && dy > 0) {
                loadMoreItems()
            }
        }

    }
    abstract fun loadMoreItems()
}