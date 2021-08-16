package com.td.secirity.valuation.entity;

import com.td.secirity.valuation.dto.RefDataDto;
import com.td.secirity.valuation.dto.TradeDto;
import com.td.secirity.valuation.dto.ValuationDto;
import lombok.Data;

import java.math.BigInteger;

@Data
public class ConsolidatedTradeData {

    private RefDataDto refData;

    private TradeDto tradeData;

    private ValuationDto valuationData;

    public ConsolidatedTradeData(TradeDto tradeDto) {
        this.tradeData = tradeDto;
    }

    public BigInteger getTradeId() {
        return this.tradeData.getTradeId();
    }

    public String getInventory() {
        return this.tradeData.getInventory();
    }
}
