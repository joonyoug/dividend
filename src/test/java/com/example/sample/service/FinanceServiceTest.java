package com.example.sample.service;

import com.example.sample.model.ScrapedResult;
import com.example.sample.persist.entity.DividendRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FinanceServiceTest {
    @Autowired
    private FinanceService financeService;
    @Autowired
    private DividendRepository dividendRepository;
    @Autowired
    private CompanyService companyService;

    @Test
    void successGetDividendByCompanyName() {
        //given
        companyService.save("MMM");
        //when
        ScrapedResult scrapedResult=financeService.getDividendByCompanyName("3M Company");
        //then
        assertEquals("1.36",scrapedResult.getDividends().get(0).getDividend());

    }



}