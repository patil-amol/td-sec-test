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
public class RefDataDto {

    @CsvBindByName(column = "topOfHouse")
    private String topOfHouse;

    @CsvBindByName(column = "segment")
    private String segment;

    @CsvBindByName(column = "viceChair")
    private String viceChair;

    @CsvBindByName(column = "globalBusiness")
    private String globalBusiness;

    @CsvBindByName(column = "Policy")
    private String policy;

    @CsvBindByName(column = "desk")
    private String desk;

    @CsvBindByName(column = "portfolio")
    private String portfolio;

    @CsvBindByName(column = "BU")
    private BigInteger bu;

    @CsvBindByName(column = "CLINE")
    private String cline;

    @CsvBindByName(column = "Inventory")
    private String inventory;
}
