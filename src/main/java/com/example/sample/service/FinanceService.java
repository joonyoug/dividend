package com.example.sample.service;


import com.example.sample.exception.impl.NoCompanyException;
import com.example.sample.model.Company;
import com.example.sample.model.Dividend;
import com.example.sample.model.ScrapedResult;
import com.example.sample.model.constants.CacheKey;
import com.example.sample.persist.entity.CompanyRepository;
import com.example.sample.persist.entity.CompanyEntity;
import com.example.sample.persist.entity.DividendEntity;
import com.example.sample.persist.entity.DividendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceService {
    private final DividendRepository dividendRepository;
    private final CompanyRepository companyRepository;

    @Cacheable(key="#companyName",value = CacheKey.KEY_FINANCE)
    //캐시에 데이터가 없을때 실행
    public ScrapedResult getDividendByCompanyName(String companyName){
        log.info("search company->"+companyName);
        //회사명을 기준으로 회사 정보를 조회
        CompanyEntity companyEntity=this.companyRepository.findByName(companyName)
                .orElseThrow(()->new NoCompanyException());

        //2.조회된 회사 id로 배당금을 조회
        List<DividendEntity> dividendEntityList=this.dividendRepository.findAllByCompanyId(companyEntity.getId());

        List<Dividend> dividendList= dividendEntityList.stream().map(
                                    m-> new Dividend(m.getDate(),m.getDividend())
                            ).collect(Collectors.toList());


        return new ScrapedResult(new Company(companyEntity.getTicker(),companyEntity.getName()),dividendList);
    }





}
