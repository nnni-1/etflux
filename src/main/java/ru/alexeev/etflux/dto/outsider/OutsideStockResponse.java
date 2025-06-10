package ru.alexeev.etflux.dto.outsider;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime first1minCandleDate;

    @JsonProperty("first1dayCandleDate")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
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
