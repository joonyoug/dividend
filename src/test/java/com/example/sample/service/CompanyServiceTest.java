package com.example.sample.service;

import com.example.sample.model.Company;
import com.example.sample.persist.entity.CompanyEntity;
import com.example.sample.persist.entity.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
class CompanyServiceTest {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void successSave() throws Exception{
        //given
        Company company=new Company();
        company.setName("3M Company");
        company.setTicker("MMM");
        //when
        Company company1=companyService.save("MMM");
        //then

        assertEquals(company1.getName(),company.getName());
        assertEquals(company1.getTicker(),company.getTicker());

    }
    @Test
    void successGetAllCompany() {
        //given
        this.companyService.save("MMM");
        this.companyService.save("AAPL");
        //when
        Page<Company> companies=this.companyService.getAllCompany(PageRequest.of(0,2));
        //then
        assertEquals(2,companies.getContent().size());
        assertEquals("MMM",companies.getContent().get(0).getTicker());
        assertEquals("AAPL",companies.getContent().get(1).getTicker());
    }

    @Test
    void successDeleteCompany() {
        //given
        this.companyService.save("MMM");

        //when
        String company=this.companyService.deleteCompany("MMM");
        //then
        assertEquals("3M Company",company);
    }




}