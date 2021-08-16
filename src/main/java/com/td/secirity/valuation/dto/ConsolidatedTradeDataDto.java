package com.td.secirity.valuation.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Data
@Builder
public class ConsolidatedTradeDataDto {

    @CsvBindByName(column = "topOfHouse")
    private String refDataTopOfHouse;

    @CsvBindByName(column = "segment")
    private String refDataSegment;

    @CsvBindByName(column = "viceChair")
    private String refDataViceChair;

    @CsvBindByName(column = "globalBusiness")
    private String refDataGlobalBusiness;

    @CsvBindByName(column = "Policy")
    private String refDataPolicy;

    @CsvBindByName(column = "desk")
    private String refDataDesk;

    @CsvBindByName(column = "portfolio")
    private String refDataPortfolio;

    @CsvBindByName(column = "BU")
    private BigInteger refDataBu;

    @CsvBindByName(column = "CLINE")
    private String refDataCline;

    @CsvBindByName(column = "Inventory")
    private String tradeDataInventory;

    @CsvBindByName(column = "Book")
    private String tradeDataBook;

    @CsvBindByName(column = "System")
    private String tradeDataSystem;

    @CsvBindByName(column = "LegalEntity")
    private String tradeDataLegalEntity;

    @CsvBindByName(column = "TradeId")
    private BigInteger tradeDataTradeId;

    @CsvBindByName(column = "Version")
    private Integer tradeDataVersion;

    @CsvBindByName(column = "TradeStatus")
    private String tradeDataTradeStatus;

    @CsvBindByName(column = "ProductType")
    private String tradeDataProductType;

    @CsvBindByName(column = "Resetting Leg")
    private String tradeDataResettingLeg;

    @CsvBindByName(column = "ProductSubType")
    private String tradeDataProductSubType;

    @CsvBindByName(column = "TDSProductType")
    private String tradeDataTdsProductType;

    @CsvBindByName(column = "SecCodeSubType")
    private String tradeDataSecCodeSubType;

    @CsvBindByName(column = "SwapType")
    private String tradeDataSwapType;

    @CsvBindByName(column = "Description")
    private String tradeDataDescription;

    @CsvBindByName(column = "TradeDate")
    private String tradeDataTradeDate;

    @CsvBindByName(column = "StartDate")
    private String tradeDataStartDate;

    @CsvBindByName(column = "MaturityDate")
    private String tradeDataMaturityDate;

    @CsvBindByName(column = "UQL_OC_MMB_MS")
    private BigDecimal valuationDataMmbMs;

    @CsvBindByName(column = "UQL_OC_MMB_MS_PC")
    private BigDecimal valuationDataMmbMsPc;

    @CsvBindByName(column = "MS-PC")
    private BigDecimal mspc;

    @CsvBindByName(column = "BreakStatus")
    private String breakStatus;

    @CsvBindByName(column = "Term")
    private String term;

    public BigDecimal getMspc() {
        if (valuationDataMmbMs != null && valuationDataMmbMsPc != null) {
            return valuationDataMmbMs.subtract(valuationDataMmbMsPc);
        }
        return null;
    }
}
