package com.td.secirity.valuation.sources;

import com.opencsv.bean.CsvToBeanBuilder;
import com.td.secirity.valuation.dto.RefDataDto;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Component("refDataCsvDataSource")
public class RefDataCsvDataSource implements InputDataSource<RefDataDto> {

    @Override
    public List<RefDataDto> getData()  {
        try (FileReader reader = new FileReader(new ClassPathResource("refdata.csv").getFile())) {
            return new CsvToBeanBuilder<RefDataDto>(reader)
                    .withType(RefDataDto.class)
                    .build()
                    .parse();
        } catch (IOException ex) {
            throw new RuntimeException("Error reading data from File", ex);
        }
    }
}
