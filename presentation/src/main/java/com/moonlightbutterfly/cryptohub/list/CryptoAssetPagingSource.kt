package com.moonlightbutterfly.cryptohub.list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo

/**
 * Used to load the crypto assets indefinitely
 * @param assetProvider the property providing the crypto assets list to load
 */
class CryptoAssetPagingSource(
    private val assetProvider: suspend (Int) -> List<CryptoAssetMarketInfo>,
) :
    PagingSource<Int, CryptoAssetMarketInfo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CryptoAssetMarketInfo> {
        return try {
            val nextPage = params.key ?: 1
            val assets = assetProvider(nextPage)
            val nextKey = if (assets.isEmpty()) null else nextPage + 1
            LoadResult.Page(
                data = assets,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CryptoAssetMarketInfo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)
                ?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)
                    ?.nextKey?.minus(1)
        }
    }
}
