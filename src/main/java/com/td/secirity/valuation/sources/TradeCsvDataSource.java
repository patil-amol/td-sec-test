package com.td.secirity.valuation.sources;

import com.opencsv.bean.CsvToBeanBuilder;
import com.td.secirity.valuation.dto.TradeDto;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Component("tradeCsvDataSource")
public class TradeCsvDataSource implements InputDataSource<TradeDto> {

    @Override
    public List<TradeDto> getData() {
        try (FileReader reader = new FileReader(new ClassPathResource("trade.csv").getFile())) {
            return new CsvToBeanBuilder<TradeDto>(reader)
                    .withType(TradeDto.class)
                    .build()
                    .parse();
        } catch (IOException ex) {
            throw new RuntimeException("Error reading data from File", ex);
        }
    }
}
