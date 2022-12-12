package com.moonlightbutterfly.cryptohub.data.assets

import com.moonlightbutterfly.cryptohub.BuildConfig
import com.moonlightbutterfly.cryptohub.data.common.Error
import com.moonlightbutterfly.cryptohub.data.common.ErrorMapper
import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import java.util.concurrent.CancellationException

/**
 * Data source interacting with CoinMarketCap service for info.
 */
class CryptoAssetsDataSourceImpl(
    private val service: CoinMarketCapService,
    private val errorMapper: ErrorMapper,
) : CryptoAssetsDataSource {

    override fun getCryptoAssetsMarketInfo(
        symbols: List<String>,
    ): Flow<Result<List<CryptoAssetMarketInfo>>> {
        val metaDataFlow = flow {
            service.getMetadata(
                apiKey = BuildConfig.API_KEY,
                symbols = symbols.joinToString(separator = ",")
            ).let { emit(it) }
        }
        val marketInfoFlow = flow {
            service.getMarketInfo(
                apiKey = BuildConfig.API_KEY,
                symbols = symbols.joinToString(separator = ",")
            ).let { emit(it) }
        }

        return metaDataFlow.combine(marketInfoFlow) { metadata, marketInfos ->
            val metadatas = metadata.data.values.sortedBy { it.symbol }
            val infos = marketInfos.data.values.sortedBy { it.symbol }
            val resultData = metadatas.zip(infos) { data, info ->
                mergeToCryptoAssetMarketInfo(info, data)
            }.sortedByDescending {
                it.marketCap
            }
            if (symbols.isNotEmpty() && resultData.isEmpty()) {
                Result.Failure(Error.NotFound("Results for: $symbols not found"))
            } else {
                Result.Success(resultData)
            }
        }.catch {
            emit(Result.Failure(errorMapper.mapError(it)))
        }
    }

    override fun getCryptoAssetsMarketInfo(page: Int): Flow<Result<List<CryptoAssetMarketInfo>>> {
        val firstIndex = CryptoAssetsDataSource.ITEMS_PER_PAGE * (page - 1) + 1
        val assetAmountPerCall = CryptoAssetsDataSource.ITEMS_PER_PAGE
        return flow<Result<List<CryptoAssetMarketInfo>>> {
            try {
                val listing = service.getListings(
                    apiKey = BuildConfig.API_KEY,
                    start = firstIndex,
                    limit = assetAmountPerCall
                ).data.sortedBy { it.symbol }

                val metadataList =
                    service.getMetadata(
                        apiKey = BuildConfig.API_KEY,
                        symbols = listing.map { it.symbol }.joinToString(separator = ",")
                    ).data.values.sortedBy { it.symbol }

                val resultData = metadataList.zip(listing) { metadata, marketInfo ->
                    mergeToCryptoAssetMarketInfo(marketInfo, metadata)
                }.sortedByDescending { it.marketCap }

                emit(Result.Success(resultData))
            } catch (exception: Exception) {
                if (exception is CancellationException) {
                    throw exception
                } else {
                    emit(Result.Failure(errorMapper.mapError(exception)))
                }
            }
        }
    }

    private fun mergeToCryptoAssetMarketInfo(
        marketQuote: CryptoAssetMarketQuoteDto,
        metadata: CryptoAssetMetadataDto,
    ): CryptoAssetMarketInfo {
        return CryptoAssetMarketInfo(
            asset = CryptoAsset(
                name = metadata.name ?: CryptoAsset.EMPTY_NAME,
                symbol = metadata.symbol ?: CryptoAsset.EMPTY_SYMBOL,
                logoUrl = metadata.logo ?: CryptoAsset.EMPTY_LOGO_URL,
            ),
            price = marketQuote.quotes?.get("USD")?.price ?: CryptoAssetMarketInfo.EMPTY_PRICE,
            rank = marketQuote.rank ?: CryptoAssetMarketInfo.EMPTY_RANK,
            marketCap = marketQuote.quotes?.get("USD")?.marketCap
                ?: CryptoAssetMarketInfo.EMPTY_MARKET_CAP,
            circulatingSupply = marketQuote.circulatingSupply
                ?: CryptoAssetMarketInfo.EMPTY_CIRCULATING_SUPPLY,
            maxSupply = marketQuote.maxSupply ?: CryptoAssetMarketInfo.EMPTY_MAX_SUPPLY,
            volume24H = marketQuote.quotes?.get("USD")?.volume24H
                ?: CryptoAssetMarketInfo.EMPTY_VOLUME_24H,
            volumeChange24H = marketQuote.quotes?.get("USD")?.volumeChange24H
                ?: CryptoAssetMarketInfo.EMPTY_VOLUME_CHANGE_24H,
            volumeChangePct24H = marketQuote.quotes?.get("USD")?.volumeChange24H
                ?: CryptoAssetMarketInfo.EMPTY_VOLUME_CHANGE_PCT_24H,
            description = metadata.description ?: CryptoAssetMarketInfo.EMPTY_DESCRIPTION
        )
    }
}
