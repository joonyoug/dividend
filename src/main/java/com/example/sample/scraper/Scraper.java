package com.example.sample.scraper;

import com.example.sample.model.Company;
import com.example.sample.model.ScrapedResult;

public interface Scraper {
    Company scraperCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
