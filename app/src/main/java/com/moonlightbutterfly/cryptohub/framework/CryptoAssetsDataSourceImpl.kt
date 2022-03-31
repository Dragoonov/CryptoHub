package com.moonlightbutterfly.cryptohub.framework

import com.google.gson.GsonBuilder
import com.moonlightbutterfly.cryptohub.BuildConfig
import com.moonlightbutterfly.cryptohub.CRYPTO_ASSETS_LOAD_NUMBER_PER_PAGE
import com.moonlightbutterfly.cryptohub.data.CryptoAssetsDataSource
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAssetMarketInfo
import com.moonlightbutterfly.cryptohub.framework.dtos.CryptoAssetMarketQuoteDto
import com.moonlightbutterfly.cryptohub.framework.dtos.CryptoAssetMetadataDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CryptoAssetsDataSourceImpl : CryptoAssetsDataSource {

    private val service by lazy {
        Retrofit.Builder()
            .baseUrl(API_ADDRESS)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(CoinMarketCapService::class.java)
    }

    override suspend fun getCryptoAssetsMarketInfo(
        symbols: List<String>
    ): List<CryptoAssetMarketInfo> {
        if (symbols.isEmpty()) {
            return emptyList()
        }

        val metadataList = service.getMetadata(
            apiKey = BuildConfig.API_KEY,
            symbols = symbols.joinToString(separator = ",")
        ).data.values.sortedBy { it.symbol }

        val marketInfoList = service.getMarketInfo(
            apiKey = BuildConfig.API_KEY,
            symbols = symbols.joinToString(separator = ",")
        ).data.values.sortedBy { it.symbol }

        return metadataList.zip(marketInfoList) { metadata, marketInfo ->
            mergeToCryptoAssetMarketInfo(marketInfo, metadata)
        }.sortedByDescending { it.marketCap }
    }

    override suspend fun getCryptoAssetsMarketInfo(page: Int): List<CryptoAssetMarketInfo> {
        val firstIndex = CRYPTO_ASSETS_LOAD_NUMBER_PER_PAGE * (page - 1) + 1
        val assetAmountPerCall = CRYPTO_ASSETS_LOAD_NUMBER_PER_PAGE
        val listing = service.getListings(
            apiKey = BuildConfig.API_KEY,
            start = firstIndex,
            limit = assetAmountPerCall
        ).data.sortedBy { it.symbol }

        val metadataList = service.getMetadata(
            apiKey = BuildConfig.API_KEY,
            symbols = listing.map { it.symbol }.joinToString(separator = ",")
        ).data.values.sortedBy { it.symbol }

        return metadataList.zip(listing) { metadata, marketInfo ->
            mergeToCryptoAssetMarketInfo(marketInfo, metadata)
        }.sortedByDescending { it.marketCap }
    }

    private fun mergeToCryptoAssetMarketInfo(marketQuote: CryptoAssetMarketQuoteDto, metadata: CryptoAssetMetadataDto): CryptoAssetMarketInfo {
        return CryptoAssetMarketInfo(
            asset = CryptoAsset(
                name = metadata.name ?: CryptoAsset.EMPTY_NAME,
                symbol = metadata.symbol ?: CryptoAsset.EMPTY_SYMBOL,
                logoUrl = metadata.logo ?: CryptoAsset.EMPTY_LOGO_URL,
            ),
            price = marketQuote.quotes?.get("USD")?.price ?: CryptoAssetMarketInfo.EMPTY_PRICE,
            rank = marketQuote.rank ?: CryptoAssetMarketInfo.EMPTY_RANK,
            marketCap = marketQuote.quotes?.get("USD")?.marketCap ?: CryptoAssetMarketInfo.EMPTY_MARKET_CAP,
            circulatingSupply = marketQuote.circulatingSupply ?: CryptoAssetMarketInfo.EMPTY_CIRCULATING_SUPPLY,
            maxSupply = marketQuote.maxSupply ?: CryptoAssetMarketInfo.EMPTY_MAX_SUPPLY,
            volume24H = marketQuote.quotes?.get("USD")?.volume24H ?: CryptoAssetMarketInfo.EMPTY_VOLUME_24H,
            volumeChange24H = marketQuote.quotes?.get("USD")?.volumeChange24H ?: CryptoAssetMarketInfo.EMPTY_VOLUME_CHANGE_24H,
            volumeChangePct24H = marketQuote.quotes?.get("USD")?.volumeChange24H ?: CryptoAssetMarketInfo.EMPTY_VOLUME_CHANGE_PCT_24H,
            description = metadata.description ?: CryptoAssetMarketInfo.EMPTY_DESCRIPTION
        )
    }

    private companion object {
        private const val API_ADDRESS = "https://pro-api.coinmarketcap.com/"
    }
}
