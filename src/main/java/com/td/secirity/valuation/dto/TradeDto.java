package com.td.secirity.valuation.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TradeDto {
    @CsvBindByName(column = "Inventory")
    private String inventory;

    @CsvBindByName(column = "Book")
    private String book;

    @CsvBindByName(column = "System")
    private String system;

    @CsvBindByName(column = "LegalEntity")
    private String legalEntity;

    @CsvBindByName(column = "TradeId")
    private BigInteger tradeId;

    @CsvBindByName(column = "Version")
    private Integer version;

    @CsvBindByName(column = "TradeStatus")
    private String tradeStatus;

    @CsvBindByName(column = "ProductType")
    private String productType;

    @CsvBindByName(column = "Resetting Leg")
    private String resettingLeg;

    @CsvBindByName(column = "ProductSubType")
    private String productSubType;

    @CsvBindByName(column = "TDSProductType")
    private String tdsProductType;

    @CsvBindByName(column = "SecCodeSubType")
    private String secCodeSubType;

    @CsvBindByName(column = "SwapType")
    private String swapType;

    @CsvBindByName(column = "Description")
    private String description;

    @CsvBindByName(column = "TradeDate")
    private String tradeDate;

    @CsvBindByName(column = "StartDate")
    private String startDate;

    @CsvBindByName(column = "MaturityDate")
    private String maturityDate;

}
