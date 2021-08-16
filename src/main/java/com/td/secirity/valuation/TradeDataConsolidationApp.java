package com.td.secirity.valuation;

import com.td.secirity.valuation.dto.ConsolidatedTradeDataDto;
import com.td.secirity.valuation.entity.ConsolidatedTradeData;
import com.td.secirity.valuation.service.ConsolidationService;
import com.td.secirity.valuation.service.OutputService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SpringBootApplication
public class TradeDataConsolidationApp implements CommandLineRunner {

    @Autowired
    private ConsolidationService consolidationService;

    @Autowired
    private OutputService outputService;

    @Override
    public void run(String... args) throws Exception {
        String outputPath = null;
        if (args.length < 1) {
            ClassPathResource resources = new ClassPathResource("/");
            outputPath = resources.getFile().getAbsolutePath() + "/td_sec_output.csv";
            log.info("Output file path is not not provided, output file will be written at {}", outputPath);
        } else {
            outputPath = args[0];
        }

        List<ConsolidatedTradeData> consolidatedTradeDataList = consolidationService.consolidate();

        ModelMapper modelMapper = new ModelMapper();
        List<ConsolidatedTradeDataDto> consolidatedTradeDtoList =
                consolidatedTradeDataList.stream()
                .map(data -> modelMapper.map(data, ConsolidatedTradeDataDto.class))
                .map(consolidationService::calculateBreakStatus)
                .map(consolidationService::calculateTerm)
                .collect(Collectors.toList());

        outputService.writeCsv(consolidatedTradeDtoList, outputPath);
    }

    public static void main(String[] args) {
        SpringApplication.run(TradeDataConsolidationApp.class, args);
    }
}
