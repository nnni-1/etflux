package ru.alexeev.etflux;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.alexeev.etflux.client.TbankInstrumentsApiClient;
import ru.alexeev.etflux.dictionary.AssetTypeName;
import ru.alexeev.etflux.dictionary.TransactionType;
import ru.alexeev.etflux.dto.request.DepositRequestDto;
import ru.alexeev.etflux.dto.request.StockRequestDto;
import ru.alexeev.etflux.dto.response.AssetPositionResponseDto;
import ru.alexeev.etflux.entity.User;
import ru.alexeev.etflux.entity.UserProfile;
import ru.alexeev.etflux.entity.UserRole;
import ru.alexeev.etflux.entity.assets.Stock;
import ru.alexeev.etflux.mapper.StockMapper;
import ru.alexeev.etflux.mapper.StockMapperImpl;
import ru.alexeev.etflux.repository.StockRepository;
import ru.alexeev.etflux.repository.UserAssetPositionRepository;
import ru.alexeev.etflux.repository.UserRepository;
import ru.alexeev.etflux.repository.UserRoleRepository;
import ru.alexeev.etflux.service.ProfileService;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class AssetScenarioTest extends AbstractEtfluxTest {

    @Autowired
    ProfileService profileService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DataSource dataSource;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private UserAssetPositionRepository userAssetPositionRepository;
    @Autowired
    private TbankInstrumentsApiClient tbankInstrumentsApiClient;

    @Autowired
    private StockMapper stockMapper;

    @BeforeEach
    public void setUp() throws SQLException {

        stockRepository.deleteAll();
        userAssetPositionRepository.deleteAll();
        userRepository.deleteAll();

        System.out.println("Connected to: " + dataSource.getConnection().getMetaData().getURL());
        System.out.println(userRepository.findAll());
        UserRole role = userRoleRepository.findByRole("USER");
        userRepository.save(User.builder()
                .email("nikitatest@mail.com")
                .roles(List.of(role))
                .username("nikitatest")
                .profile(new UserProfile())
                .build());
    }

    @Test
    public void testStockSavedPositive(){
        User user = userRepository.findByUsername("nikitatest").get();

        StockRequestDto stockRequestDto1 = StockRequestDto.builder()
                .ticker("SBER")
                .buyPrice(BigDecimal.valueOf(280))
                .quantity(BigDecimal.valueOf(10))
                .timeOfBuy(LocalDateTime.of(2025, 1, 15, 10, 10))
                .transactionType(TransactionType.BUY)
                .type("share")
                .build();

        AssetPositionResponseDto<Stock> positionResponse1 = profileService.addStock(stockRequestDto1, user);

        Assertions.assertEquals(1L, positionResponse1.getAssetId());
        Assertions.assertEquals(BigDecimal.valueOf(10), positionResponse1.getQuantity());
        Assertions.assertEquals("Сбер Банк", positionResponse1.getAsset().getName());

        StockRequestDto stockRequestDto2 = StockRequestDto.builder()
                .ticker("SBER")
                .buyPrice(BigDecimal.valueOf(290))
                .quantity(BigDecimal.valueOf(10))
                .timeOfBuy(LocalDateTime.of(2025, 1, 18, 10, 10))
                .transactionType(TransactionType.BUY)
                .type("share")
                .build();
        AssetPositionResponseDto<Stock> positionResponse2 = profileService.addStock(stockRequestDto2, user);

        Assertions.assertEquals(1L, positionResponse2.getAssetId());
        System.out.println(userAssetPositionRepository.findAll());
        Assertions.assertEquals(BigDecimal.valueOf(20).toPlainString(), positionResponse2.getQuantity().stripTrailingZeros().toPlainString());
        Assertions.assertEquals(BigDecimal.valueOf(285).stripTrailingZeros().toPlainString(), positionResponse2.getAverageBuyPrice().stripTrailingZeros().toPlainString());
    }

    @Test
    void testGettingPortfolioPositive(){
        User user = userRepository.findByUsername("nikitatest").get();

        StockRequestDto stockRequestDto1 = StockRequestDto.builder()
                .ticker("SBER")
                .buyPrice(BigDecimal.valueOf(290))
                .quantity(BigDecimal.valueOf(10))
                .timeOfBuy(LocalDateTime.of(2025, 1, 18, 10, 10))
                .transactionType(TransactionType.BUY)
                .type("share")
                .build();

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .bankName("Сбербанк")
                .percentYear(20.0)
                .sumFinal(BigDecimal.valueOf(100000))
                .capitalization(true)
                .capitalizationDays(30)
                .registrationDate(LocalDate.of(2025, 1, 10))
                .build();

        profileService.addStock(stockRequestDto1, user);
        profileService.addDeposit(depositRequestDto, user);

        var allAssets = profileService.getAssets(user);

        Assertions.assertEquals(
                allAssets
                        .get(AssetTypeName.STOCK.name())
                        .get(0)
                        .getCurrentPricePerUnit()
                        .longValue(),

                tbankInstrumentsApiClient
                        .getInstrumentWithPrice((Stock) user.getProfile()
                                .getAssetsByType(Stock.class)
                                .get(0)
                                .getAsset())
                        .getStockPrice()
                        .getPrice()
                        .getNano());

    }
}
