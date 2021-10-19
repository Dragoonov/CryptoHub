package com.moonlightbutterfly.cryptohub.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.moonlightbutterfly.cryptohub.dataobjects.CryptoassetItem

/**
 * Used to load the cryptoassets indefinitely
 * @param cryptoassetProvider the property providing the cryptoassets list to load
 */
class CryptoassetPagingSource(private val cryptoassetProvider: suspend (Int) -> List<CryptoassetItem>) :
    PagingSource<Int, CryptoassetItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CryptoassetItem> {
        val nextPage = params.key ?: 1
        val cryptoassets = cryptoassetProvider(nextPage)
        val nextKey = if (cryptoassets.isEmpty()) null else nextPage + 1
        return LoadResult.Page(
            data = cryptoassets,
            prevKey = if (nextPage == 1) null else nextPage - 1,
            nextKey = nextKey
        )
    }

    override fun getRefreshKey(state: PagingState<Int, CryptoassetItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)
                ?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)
                    ?.nextKey?.minus(1)
        }
    }
}
