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

    private val VISIBLE_THRESHOLD = 20;

    private val PAGE_START = 1;
    private val PAGE_SIZE = 10;

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

//        val totalItemCount  =layoutManager.itemCount
//        val lastVisibleItem  =layoutManager.findLastVisibleItemPosition()
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
//
//        if (!isLoading() && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
//            loadMoreItems()
//
//        }

        if(!isLoading() && !isLastPage()){
            if (totalItemCount <= (lastVisibleItemPosition + 1) && totalItemCount > 9 && dy > 0) {
                loadMoreItems()
            }
        }



//        if (!isLoading() && !isLastPage()) {
//            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
//                && firstVisibleItemPosition >= 0
//                && totalItemCount >= PAGE_SIZE) {
//                loadMoreItems();
//            }
//        }

//        if (!isLoading() && !isLastPage()) {
//
//            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
//                loadMoreItems()
//            }//                    && totalItemCount >= ClothesFragment.itemsCount
//        }
    }
    abstract fun loadMoreItems()
}