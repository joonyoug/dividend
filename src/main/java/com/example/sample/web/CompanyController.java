package com.example.sample.web;

import com.example.sample.model.Company;
import com.example.sample.model.constants.CacheKey;
import com.example.sample.persist.entity.CompanyEntity;
import com.example.sample.service.CompanyService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.h2.util.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;
    private final CacheManager cacheManager;

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword){
        var result=this.companyService.autoComplete(keyword);

        return ResponseEntity.ok(result);

    }




    @GetMapping
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<?> searchCompany(final Pageable pageable){

        Page<Company> companies=this.companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companies);

    }

    /**
     *  회사, 배당금 추가.
     * @param request
     *  Company
     * @return
     */
    @PostMapping
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> addCompany(@RequestBody Company request)
    {
        String ticker=request.getTicker().trim();
        if(ObjectUtils.isEmpty(ticker)){
            throw new RuntimeException("tick is empty->");
        }
        Company company=this.companyService.save(ticker);
        this.companyService.addAutoCompleteKeyword(company.getName());
        return ResponseEntity.ok(company);


    }
    @DeleteMapping("{ticker}")
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> deleteCompany(@PathVariable String ticker)
    {
        String companyName=this.companyService.deleteCompany(ticker);
        this.clearFinanceCache(companyName);


        return ResponseEntity.ok(companyName);
    }
    public void clearFinanceCache(String companyName){
        this.cacheManager.getCache(CacheKey.KEY_FINANCE).evict(companyName);


    }



}
