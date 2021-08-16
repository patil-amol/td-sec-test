package com.td.secirity.valuation.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ValuationDto {

    @CsvBindByName(column = "TradeId")
    private BigInteger tradeId;

    @CsvBindByName(column = "UQL_OC_MMB_MS")
    private BigDecimal mmbMs;

    @CsvBindByName(column = "UQL_OC_MMB_MS_PC")
    private BigDecimal mmbMsPc;

}
