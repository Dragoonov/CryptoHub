package com.moonlightbutterfly.cryptohub.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.moonlightbutterfly.cryptohub.dataobjects.CryptocurrencyItem

/**
 * Used to load the cryptocurrencies indefinitely
 * @param cryptocurrencyProvider the property providing the cryptocurrencies list to load
 */
class CryptocurrencyPagingSource(private val cryptocurrencyProvider: suspend (Int) -> List<CryptocurrencyItem>) :
    PagingSource<Int, CryptocurrencyItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CryptocurrencyItem> {
        val nextPage = params.key ?: 1
        val cryptocurrencies = cryptocurrencyProvider(nextPage)
        return LoadResult.Page(
            data = cryptocurrencies,
            prevKey = if (nextPage == 1) null else nextPage - 1,
            nextKey = nextPage + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, CryptocurrencyItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)
                ?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)
                    ?.nextKey?.minus(1)
        }
    }
}
