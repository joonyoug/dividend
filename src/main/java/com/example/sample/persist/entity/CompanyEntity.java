package com.example.sample.persist.entity;

import com.example.sample.model.Company;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity(name="COMPANY")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String name;
    @Column(unique = true)
    private String ticker;

    public CompanyEntity(Company company){
        this.ticker=company.getTicker();
        this.name=company.getName();
    }

}
