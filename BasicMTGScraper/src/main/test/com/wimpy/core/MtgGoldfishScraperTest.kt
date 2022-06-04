package com.wimpy.core

import com.wimpy.core.util.MtgGoldfishExtractor
import com.wimpy.dao.MtgCardCrudDao
import com.wimpy.dao.MtgHistoryCrudDao
import com.wimpy.rest.v1.model.MtgQuery
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension


@ExtendWith(MockitoExtension::class)
class MtgGoldfishScraperTest {

    @InjectMocks
    lateinit var mtgGoldfishScraper: MtgGoldfishScraper

    @Mock
    lateinit var mtgGoldfishExtractor: MtgGoldfishExtractor

    @Mock
    lateinit var mtgHistoryCrudDao: MtgHistoryCrudDao

    @Mock
    lateinit var mtgCardCrudDao: MtgCardCrudDao

    @Mock
    lateinit var scraperUtil: ScraperUtil

    @BeforeEach
    internal fun setUp() {
        mtgGoldfishScraper = MtgGoldfishScraper(mtgGoldfishExtractor,mtgHistoryCrudDao,mtgCardCrudDao,scraperUtil)
    }

    @Test
    fun retrieveCardPrice() {
//        val retrieveCardPrice = mtgGoldfishScraper.retrieveCardPrice(MtgQuery("", "", "https://www.mtggoldfish.com/price/Tenth+Edition/Crucible+of+Worlds#paper"))
    }
}