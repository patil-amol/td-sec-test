package com.td.secirity.valuation.service;

import com.td.secirity.valuation.dto.ConsolidatedTradeDataDto;
import com.td.secirity.valuation.dto.RefDataDto;
import com.td.secirity.valuation.dto.TradeDto;
import com.td.secirity.valuation.dto.ValuationDto;
import com.td.secirity.valuation.entity.ConsolidatedTradeData;
import com.td.secirity.valuation.sources.InputDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MONTHS;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConsolidationService {

    @Qualifier("refDataCsvDataSource")
    private final InputDataSource<RefDataDto> refDataSource;

    @Qualifier("tradeCsvDataSource")
    private final InputDataSource<TradeDto> tradeDataSource;

    @Qualifier("valuationCsvDataSource")
    private final InputDataSource<ValuationDto> valuationDataSource;

    public List<ConsolidatedTradeData> consolidate() {
        Map<BigInteger, ValuationDto> tradeIdValuationMap =
                 valuationDataSource.getData().stream()
                .collect(Collectors.toMap(ValuationDto::getTradeId, Function.identity()));

        Map<String, RefDataDto> inventoryRefDataMap =
                refDataSource.getData().stream()
                        .collect(Collectors.toMap(RefDataDto::getInventory, Function.identity()));

        return tradeDataSource.getData().stream()
                .map(ConsolidatedTradeData::new)
                .map(trade -> {
                    trade.setRefData(inventoryRefDataMap.get(trade.getInventory()));
                    trade.setValuationData(tradeIdValuationMap.get(trade.getTradeId()));
                    return trade;
                }).collect(Collectors.toList());
    }

    public ConsolidatedTradeDataDto calculateBreakStatus(ConsolidatedTradeDataDto dto) {
        BigDecimal absMsPc = (dto.getMspc() == null) ? BigDecimal.ZERO : dto.getMspc().abs();

        if (absMsPc.compareTo(BigDecimal.ZERO) > -1 && absMsPc.compareTo(BigDecimal.valueOf(99L)) < 0) {
            dto.setBreakStatus("0-99");
        } else if (absMsPc.compareTo(BigDecimal.valueOf(100L)) > -1 && absMsPc.compareTo(BigDecimal.valueOf(999L)) < 0) {
            dto.setBreakStatus("100-999");
        } else if (absMsPc.compareTo(BigDecimal.valueOf(1000L)) > -1 && absMsPc.compareTo(BigDecimal.valueOf(9999L)) < 0) {
            dto.setBreakStatus("1000-9999");
        } else if (absMsPc.compareTo(BigDecimal.valueOf(10000L)) > -1 && absMsPc.compareTo(BigDecimal.valueOf(99999L)) < 0) {
            dto.setBreakStatus("10000-99999");
        } else if (absMsPc.compareTo(BigDecimal.valueOf(100000L)) > -1) {
            dto.setBreakStatus("100000+");
        }
        return dto;
    }


    public ConsolidatedTradeDataDto calculateTerm(ConsolidatedTradeDataDto dto) {
        LocalDate today = LocalDate.now();

        if (isNotEmpty(dto.getTradeDataMaturityDate())) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate maturityDate = LocalDate.parse(dto.getTradeDataMaturityDate(), df);

            if (maturityDate.isBefore(today)) {
                log.info("Trade is already Matured. Term not applicable");
                return dto;
            }

            long diffInMonths = MONTHS.between(today, maturityDate);

            if (diffInMonths < 1) {
                dto.setTerm("0m - 1m");
            } else if (diffInMonths > 1 && diffInMonths <= 6) {
                dto.setTerm("1m - 6m");
            } else if (diffInMonths > 6 && diffInMonths <= 12) {
                dto.setTerm("6m - 1yr");
            } else if (diffInMonths > 12 && diffInMonths <= 120) {
                dto.setTerm("1yr - 10yr");
            } else if (diffInMonths > 120 && diffInMonths <= 360) {
                dto.setTerm("10yr - 30yr");
            } else if (diffInMonths > 360 && diffInMonths <= 600) {
                dto.setTerm("30yr - 50yr");
            } else if (diffInMonths > 600) {
                dto.setTerm("50yr+");
            }
        }
        return dto;
    }
}
