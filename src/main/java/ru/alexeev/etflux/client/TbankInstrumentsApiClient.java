package ru.alexeev.etflux.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.alexeev.etflux.dictionary.Currency;
import ru.alexeev.etflux.dto.outsider.LastStockPrice;
import ru.alexeev.etflux.dto.outsider.OutsideStockPricesResponseWrapper;
import ru.alexeev.etflux.dto.outsider.OutsideStockResponse;
import ru.alexeev.etflux.dto.outsider.OutsideStockResponseWrapper;
import ru.alexeev.etflux.entity.assets.CurrencyAsset;
import ru.alexeev.etflux.entity.assets.Stock;
import ru.alexeev.etflux.exception.InstrumentsNotFoundException;
import ru.alexeev.etflux.exception.StockPriceNotFoundException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TbankInstrumentsApiClient {

    private static final String FIND_INSTRUMENT_BODY = """
            {
            "query": %s,
            "instrumentKind": "INSTRUMENT_ID_TYPE_TICKER",
            "apiTradeAvailableFlag": true
            }
            """;

    private static final String GET_STOCK_BODY = """
            {
            "idType": "INSTRUMENT_ID_TYPE_UID",
            "id": "%s"
            }
            """;

    private static final String GET_LAST_PRICES = """
            {
              "instrumentId": %s,
              "lastPriceType": "LAST_PRICE_UNSPECIFIED",
              "instrumentStatus": "INSTRUMENT_STATUS_UNSPECIFIED"
            }
            """;

    @Value("${api.tbank.url.instrumentBy}")
    private String tbankUrlInstrumentBy;

    @Value("${api.tbank.url.shareBy}")
    private String tbankUrlShareBy;

    @Value("${api.tbank.url.getLastPrices}")
    private String tbankUrlLastPrices;

    private final OkHttpClient client;

    private final ObjectMapper mapper;

    public List<OutsideStockResponse> getListOfInstruments(String instrumentId) throws InstrumentsNotFoundException {

        Request request = createRequestForListOfInstruments(instrumentId, tbankUrlInstrumentBy);

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new InstrumentsNotFoundException("Request failed with code: " + response.code());
            }

            String body = response.body().string();

            OutsideStockResponseWrapper responseWrapper = mapper.readValue(body, new TypeReference<>() {});
            List<OutsideStockResponse> list = responseWrapper.getInstruments();
            if (list.isEmpty()) {
                throw new InstrumentsNotFoundException("Empty response list for instrument: " + instrumentId);
            }
            return list;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    private Request createRequestForListOfInstruments(String instrumentId, String url){
        return new Request.Builder()
                .url(url)
                .post(createRequestBodyForStocks(instrumentId))
                .build();
    }

    private Request createRequestForStocksWithPrices(List<String> stockFigis){
        System.out.println("IN REQUEST CREATING " + stockFigis);
        return new Request.Builder()
                .url(this.tbankUrlLastPrices)
                .post(createRequestBodyForStocksWithPrices(stockFigis))
                .build();
    }

    private RequestBody createRequestBodyForStocksWithPrices(List<String> stockFigis){
        MediaType jsonType = MediaType.get("application/json; charset=utf-8");
        System.out.println(GET_LAST_PRICES.formatted(Arrays.toString(stockFigis.toArray())));
        return RequestBody.create(GET_LAST_PRICES.formatted(Arrays.toString(stockFigis.toArray())), jsonType);
    }

    private RequestBody createRequestBodyForStocks(String instrumentId){
        MediaType jsonType = MediaType.get("application/json; charset=utf-8");
        return RequestBody.create(FIND_INSTRUMENT_BODY.formatted(instrumentId), jsonType);
    }

    public List<Stock> getListOfInstrumentsWithPrices(List<Stock> stocks){
        List<String> figis = stocks.stream().map(Stock::getFigi).toList();
        Request request = createRequestForStocksWithPrices(figis);

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new InstrumentsNotFoundException("Request failed with code: " + response.code());
            }

            String body = response.body().string();

            OutsideStockPricesResponseWrapper responseWrapper = mapper.readValue(body, new TypeReference<>() {});
            List<LastStockPrice> lastStockPrices = responseWrapper.getOutsideStockPrices();

            if (lastStockPrices.isEmpty()) {
                throw new StockPriceNotFoundException("Empty response list for instruments: " + figis);
            }

            Map<String, Stock> stockMap = stocks.stream()
                    .collect(Collectors.toMap(Stock::getFigi, Function.identity()));

            setLastPrices(lastStockPrices, stockMap);

        } catch (IOException | InstrumentsNotFoundException e) {
            throw new RuntimeException(e);
        }
        return stocks;
    }

    public Stock getInstrumentWithPrice(Stock stock){
        return getListOfInstrumentsWithPrices(List.of(stock)).get(0);
    }

    public void setLastPrices(List<LastStockPrice> lastStockPricesList, Map<String, Stock> stockMap) {
        for (LastStockPrice lastStockPrices : lastStockPricesList) {
            String figi = lastStockPrices.getFigi();
            Stock matchingStock = stockMap.get(figi);

            if (matchingStock == null) {
                throw new IllegalArgumentException("Stock not found for figi: " + figi);
            }

            matchingStock.setStockPrice(lastStockPrices);
            matchingStock.setPrice(BigDecimal.valueOf(lastStockPrices.getPrice().getNano()));
        }
    }
}
