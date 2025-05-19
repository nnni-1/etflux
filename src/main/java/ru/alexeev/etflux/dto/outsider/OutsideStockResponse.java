package ru.alexeev.etflux.dto.outsider;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class OutsideStockResponse {

    @JsonProperty("isin")
    private String isin;

    @JsonProperty("figi")
    private String figi;

    @JsonProperty("ticker")
    private String ticker;

    @JsonProperty("classCode")
    private String classCode;

    @JsonProperty("instrumentType")
    private String instrumentType;

    @JsonProperty("name")
    private String name;

    @JsonProperty("uid")
    private String uid;

    @JsonProperty("positionUid")
    private String positionUid;

    @JsonProperty("instrumentKind")
    private String instrumentKind;

    @JsonProperty("apiTradeAvailableFlag")
    private boolean apiTradeAvailableFlag;

    @JsonProperty("forIisFlag")
    private boolean forIisFlag;

    @JsonProperty("first1minCandleDate")
    private OffsetDateTime first1minCandleDate;

    @JsonProperty("first1dayCandleDate")
    private OffsetDateTime first1dayCandleDate;

    @JsonProperty("forQualInvestorFlag")
    private boolean forQualInvestorFlag;

    @JsonProperty("weekendFlag")
    private boolean weekendFlag;

    @JsonProperty("blockedTcaFlag")
    private boolean blockedTcaFlag;

    @JsonProperty("lot")
    private int lot;
}
