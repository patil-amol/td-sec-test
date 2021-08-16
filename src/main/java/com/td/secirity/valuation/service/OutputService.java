package com.td.secirity.valuation.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.td.secirity.valuation.dto.ConsolidatedTradeDataDto;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Component
public class OutputService {

    public void writeCsv(List<ConsolidatedTradeDataDto> consolidatedTradeDtoList, String file) {
        try (Writer writer  = new FileWriter(file)) {
            StatefulBeanToCsv sbc = new StatefulBeanToCsvBuilder(writer)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR).withOrderedResults(true)
                    .build();

            sbc.write(consolidatedTradeDtoList);
        }catch (Exception ex) {
           throw new RuntimeException("Error while writing output csv", ex);
        }

    }
}
