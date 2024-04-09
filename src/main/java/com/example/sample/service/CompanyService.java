package com.example.sample.service;

import com.example.sample.exception.impl.AlreadyExistTicker;
import com.example.sample.exception.impl.FailedScrapTicker;
import com.example.sample.exception.impl.NoCompanyException;
import com.example.sample.model.Company;
import com.example.sample.model.ScrapedResult;
import com.example.sample.persist.entity.CompanyRepository;
import com.example.sample.persist.entity.CompanyEntity;
import com.example.sample.persist.entity.DividendEntity;
import com.example.sample.persist.entity.DividendRepository;
import com.example.sample.scraper.Scraper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final Trie trie;

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private  final Scraper yahooFinanceScraper;
    public Company save(String ticker){
        boolean exists=this.companyRepository.existsByTicker(ticker);
        if(exists){
           throw new RuntimeException(new AlreadyExistTicker());
        }
        return this.storeCompanyAndDividend(ticker);

    }
    private Company storeCompanyAndDividend(String ticker){
        //ticker 를 기준으로 회사를 스크래핑
        Company company=this.yahooFinanceScraper.scraperCompanyByTicker(ticker);

        if(ObjectUtils.isEmpty(company)){
            throw new RuntimeException(new FailedScrapTicker());
        }
        ScrapedResult scrapedResult=this.yahooFinanceScraper.scrap(company);

        //해당 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑

        //스크래핑 결과

        CompanyEntity companyEntity= this.companyRepository.save(new CompanyEntity(company));

        List<DividendEntity> dividendEntityList=scrapedResult.getDividends().stream().map(
                e-> new DividendEntity(companyEntity.getId(),e)).collect(Collectors.toList());

        this.dividendRepository.saveAll(dividendEntityList);

        return company;

    }
    public Page<Company> getAllCompany(Pageable pageable){
       Page<CompanyEntity> companyEntities= this.companyRepository.findAll(pageable);

       Page<Company> companies=companyEntities.map(m->new Company(m.getTicker(), m.getName()));
        return companies;


    }

    public String deleteCompany(String ticker){
        CompanyEntity companyEntity=this.companyRepository.findByTicker(ticker)
                .orElseThrow(()->new NoCompanyException());

        this.dividendRepository.deleteAllByCompanyId(companyEntity.getId());
        this.companyRepository.delete(companyEntity);
        this.deleteAutoComplete(companyEntity.getName());

        return companyEntity.getName();
    }
    public void addAutoCompleteKeyword(String keyword){
        this.trie.put(keyword,null);
    }
    public List<String> autoComplete(String keyword){
        return (List<String>) this.trie.prefixMap(keyword).keySet()
                .stream().
                limit(10)
                .collect(Collectors.toList());
    }
    public void deleteAutoComplete(String keyword){
        this.trie.remove(keyword);
    }

}
