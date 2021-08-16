package com.td.secirity.valuation.service;

import com.td.secirity.valuation.dto.ConsolidatedTradeDataDto;
import com.td.secirity.valuation.dto.RefDataDto;
import com.td.secirity.valuation.dto.TradeDto;
import com.td.secirity.valuation.dto.ValuationDto;
import com.td.secirity.valuation.entity.ConsolidatedTradeData;
import com.td.secirity.valuation.sources.InputDataSource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ConsolidationServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @InjectMocks
    private ConsolidationService service;

    @Mock
    private InputDataSource<RefDataDto> refDataSource;

    @Mock
    private InputDataSource<TradeDto> tradeDataSource;

    @Mock
    private InputDataSource<ValuationDto> valuationDataSource;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(service, "refDataSource", refDataSource);
        ReflectionTestUtils.setField(service, "tradeDataSource", tradeDataSource);
        ReflectionTestUtils.setField(service, "valuationDataSource", valuationDataSource);
    }

    @Test
    public void calculateTerm_emptyMaturityDate() {
        ConsolidatedTradeDataDto data = ConsolidatedTradeDataDto.builder().build();
        service.calculateTerm(data);
        assertNull(data.getTerm());
    }

    @Test
    public void calculateTerm_pastMaturityDate() {
        ConsolidatedTradeDataDto data = ConsolidatedTradeDataDto.builder().tradeDataMaturityDate("20190620").build();
        service.calculateTerm(data);
        assertNull(data.getTerm());
    }

    @Test
    public void calculateTerm_futureMaturityDate() {
        LocalDate maturityDate = LocalDate.now().plusYears(2).plusMonths(3);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");

        ConsolidatedTradeDataDto data = ConsolidatedTradeDataDto.builder()
                .tradeDataMaturityDate(maturityDate.format(df)).build();
        service.calculateTerm(data);
        assertThat(data.getTerm(), is("1yr - 10yr"));
    }

    @Test
    public void calculateBreakStatus() {
        ConsolidatedTradeDataDto data = ConsolidatedTradeDataDto.builder()
                .valuationDataMmbMs(BigDecimal.valueOf(-14109586.5))
                .valuationDataMmbMsPc(BigDecimal.valueOf(14839736.32)).build();
        service.calculateBreakStatus(data);
        assertThat(data.getBreakStatus(), is("100000+"));
    }

    @Test
    public void calculateBreakStatus_oneValueMissing() {
        ConsolidatedTradeDataDto data = ConsolidatedTradeDataDto.builder()
                .valuationDataMmbMsPc(BigDecimal.valueOf(14839736.32)).build();
        service.calculateBreakStatus(data);
        assertThat(data.getBreakStatus(), is("0-99"));
    }

    @Test
    public void consolidate() {
        Mockito.when(tradeDataSource.getData()).thenReturn(
                Arrays.asList(TradeDto.builder().tradeId(BigInteger.ONE).tradeDate("20210723").inventory("Inv1").tradeStatus("Active").build())
        );
        Mockito.when(valuationDataSource.getData()).thenReturn(
                Arrays.asList(ValuationDto.builder().tradeId(BigInteger.TEN).build())
        );
        Mockito.when(refDataSource.getData()).thenReturn(
                Arrays.asList(RefDataDto.builder().inventory("Inv2").build())
        );

        List<ConsolidatedTradeData> consolidatedTradeDataList = service.consolidate();
        assertFalse(consolidatedTradeDataList.isEmpty());
        ConsolidatedTradeData consolidatedTradeData = consolidatedTradeDataList.get(0);
        assertNotNull(consolidatedTradeData.getTradeData());
        assertNull(consolidatedTradeData.getRefData());
        assertNull(consolidatedTradeData.getValuationData());
    }

    @Test
    public void consolidate_matchingRefData() {
        Mockito.when(tradeDataSource.getData()).thenReturn(
                Arrays.asList(TradeDto.builder().tradeId(BigInteger.ONE).tradeDate("20210723").inventory("Inv1").tradeStatus("Active").build())
        );
        Mockito.when(valuationDataSource.getData()).thenReturn(
                Arrays.asList(ValuationDto.builder().tradeId(BigInteger.ONE).build())
        );
        Mockito.when(refDataSource.getData()).thenReturn(
                Arrays.asList(RefDataDto.builder().inventory("Inv1").build())
        );

        List<ConsolidatedTradeData> consolidatedTradeDataList = service.consolidate();
        assertFalse(consolidatedTradeDataList.isEmpty());
        ConsolidatedTradeData consolidatedTradeData = consolidatedTradeDataList.get(0);
        assertNotNull(consolidatedTradeData.getTradeData());
        assertNotNull(consolidatedTradeData.getRefData());
        assertNotNull(consolidatedTradeData.getValuationData());
    }

    @Test
    public void consolidate_multipleTrade() {
        Mockito.when(tradeDataSource.getData()).thenReturn(
                Arrays.asList(
                        TradeDto.builder().tradeId(BigInteger.ONE).tradeDate("20210723").inventory("Inv1").tradeStatus("Active").build(),
                        TradeDto.builder().tradeId(BigInteger.valueOf(11L)).tradeDate("20210820").inventory("Inv2").tradeStatus("Active").build()
                        )
        );
        Mockito.when(valuationDataSource.getData()).thenReturn(
                Arrays.asList(ValuationDto.builder().tradeId(BigInteger.ONE).build())
        );
        Mockito.when(refDataSource.getData()).thenReturn(
                Arrays.asList(
                        RefDataDto.builder().inventory("Inv1").build(),
                        RefDataDto.builder().inventory("Inv2").build()
                        )
        );

        List<ConsolidatedTradeData> consolidatedTradeDataList = service.consolidate();
        assertFalse(consolidatedTradeDataList.isEmpty());
        ConsolidatedTradeData consolidatedTradeData = consolidatedTradeDataList.get(0);
        assertNotNull(consolidatedTradeData.getTradeData());
        assertNotNull(consolidatedTradeData.getRefData());
        assertNotNull(consolidatedTradeData.getValuationData());

        ConsolidatedTradeData consolidatedTradeData1 = consolidatedTradeDataList.get(1);
        assertNotNull(consolidatedTradeData1.getTradeData());
        assertNotNull(consolidatedTradeData1.getRefData());
        assertNull(consolidatedTradeData1.getValuationData());
    }
}
