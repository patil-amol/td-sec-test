package com.td.secirity.valuation.sources;

import com.opencsv.bean.CsvToBeanBuilder;
import com.td.secirity.valuation.dto.ValuationDto;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Component("valuationCsvDataSource")
public class ValuationCsvDataSource implements InputDataSource<ValuationDto> {
    
    @Override
    public List<ValuationDto> getData() {
        try (FileReader reader = new FileReader(new ClassPathResource("valuation.csv").getFile())) {
            return new CsvToBeanBuilder<ValuationDto>(reader)
                    .withType(ValuationDto.class)
                    .build()
                    .parse();
        } catch (IOException ex) {
            throw new RuntimeException("Error reading data from File", ex);
        }
    }
}
