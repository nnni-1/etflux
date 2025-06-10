//package ru.alexeev.etflux;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import ru.alexeev.etflux.client.TbankInstrumentsApiClient;
//import ru.alexeev.etflux.entity.User;
//import ru.alexeev.etflux.entity.UserAssetPosition;
//import ru.alexeev.etflux.entity.UserProfile;
//import ru.alexeev.etflux.entity.UserRole;
//import ru.alexeev.etflux.entity.assets.Asset;
//import ru.alexeev.etflux.entity.assets.Stock;
//import ru.alexeev.etflux.service.impl.StockServiceImpl;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class StockServiceTest extends AbstractEtfluxTest {
//
//    @Autowired
//    private StockServiceImpl stockService;
//
//    @Autowired
//    private TbankInstrumentsApiClient client;
//
//    @Test
//    public void testStockServiceGetStockByTicker() {
//        Stock stock = stockService.getStockByTicker("SBER");
//
//        assertNotNull(stock);
//        assertEquals("SBER", stock.getTicker());
//    }
//
//    @Test
//    public void testClientGetStocksWithPricesPositive(){
//        Stock stock = stockService.fetchAndSaveStockFromApi("SBER");
//        assertEquals(stock.getTicker(), "SBER");
//
//        assertTrue(stock.getStockPrice().getPrice().getNano() > 0);
//    }
//
//    @Test
//    public void testUserProfileAssets(){
//        User user = User.builder()
//                .username("nikita")
//                .email("nikita@gmail.com")
//                .roles(List.of(new UserRole("USER")))
//                .build();
//        UserProfile userProfile = UserProfile.builder()
//                .user(user)
//                .build();
//        Asset stock = stockService.getStockByTicker("SBER");
//        userProfile.getAssetPositions().add(UserAssetPosition.builder()
//                        .asset(stock)
//                        .averagePrice(BigDecimal.valueOf(302))
//                        .lastUpdated(LocalDateTime.now())
//                        .quantity(BigDecimal.valueOf(10))
//                .build());
//    }
//}
