package ru.alexeev.etflux.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import ru.alexeev.etflux.dto.outsider.OutsideStockResponse;
import ru.alexeev.etflux.entity.Stock;
import ru.alexeev.etflux.mapper.StockMapper;
import ru.alexeev.etflux.repository.AssetRepository;
import ru.alexeev.etflux.repository.StockRepository;
import ru.alexeev.etflux.service.AssetsService;
import ru.alexeev.etflux.service.StockService;

import java.io.IOException;

@AllArgsConstructor
public class StockServiceImpl implements StockService {
    private StockRepository stockRepository;
    private OkHttpClient okHttpClient;
    private ObjectMapper objectMapper;
    private StockMapper stockMapper;

    @Value("${api.tbank.url.shareBy}")
    private String tbankUrl;

    @Override
    public Stock getStockByTicker(String ticker) {

        if (stockRepository.findByTicker(ticker).isPresent()) {
            return stockRepository.findByTicker(ticker).get();
        }

        return stockRepository.save(stockMapper.outsideToStock(sendRequest(ticker)));

    }

    private OutsideStockResponse sendRequest(String ticker){
        MediaType json = MediaType.get("application/json; charset=utf-8");
        String jsonBody = """
                  {
                  "query": %s,
                  "instrumentKind": "INSTRUMENT_TYPE_UNSPECIFIED",
                  "apiTradeAvailableFlag": true
                  }
                """.formatted(ticker);
        RequestBody body = RequestBody.create(json, jsonBody);
        Request request = new Request.Builder()
                .url(tbankUrl)
                .post(body)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()){
            if (response.isSuccessful() && response.body() != null){
                return objectMapper.readValue(response.body().string(), OutsideStockResponse.class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
