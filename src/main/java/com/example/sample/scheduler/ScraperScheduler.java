package com.example.sample.scheduler;

import com.example.sample.model.Company;
import com.example.sample.model.ScrapedResult;
import com.example.sample.model.constants.CacheKey;
import com.example.sample.persist.entity.CompanyRepository;
import com.example.sample.persist.entity.CompanyEntity;
import com.example.sample.persist.entity.DividendEntity;
import com.example.sample.persist.entity.DividendRepository;
import com.example.sample.scraper.Scraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableCaching
public class ScraperScheduler {
    private final CompanyRepository companyRepository;
    private final Scraper yahooFinanceScraper;
    private final DividendRepository dividendRepository;
    @CacheEvict(value = CacheKey.KEY_FINANCE,allEntries = true)
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduler(){
        log.info("Scraping scheduler is started");

        //저장된 회사 목록 조회
        List<CompanyEntity> companyEntityList=this.companyRepository.findAll();


        //회사마다 배당금 정보를 새로 스크래핑
        for(var company:companyEntityList){
            log.info("scraping ->"+ company.getName());

            ScrapedResult scrapedResult=this.yahooFinanceScraper.scrap(new Company(company.getTicker(), company.getName()));

            //스크래핑한 배당금 정보 중 데이터베이스어 없는 값은 저장
            scrapedResult.getDividends().stream()
                    //디비든 모델을 디비든 엔티티로 매핑
                    .map(m->new DividendEntity(company.getId(),m))
                    //엘리먼트를 하나씩 디비든 레파지토리에 삽입
                    .forEach(
                            e-> {
                                boolean exists = this.dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), (e.getDate()));
                                if(!exists){
                                    this.dividendRepository.save(e);
                                }
                                log.info("insert new dividend->"+e.toString());

                            }
                    );
            //연속적으로 스크래핑 대상 사이트 서버에 요청을 날리지 않도록 일시정지

            try {
                Thread.sleep(3000);
            }catch (InterruptedException e){
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }



        }









    }

}
