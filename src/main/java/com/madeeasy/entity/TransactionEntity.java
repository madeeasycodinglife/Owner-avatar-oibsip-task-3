package com.madeeasy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Setter
public class TransactionEntity {
    @Id
    private String id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    private Timestamp date;
    private String type;
    private double amount;
    private String relatedAccountId;  // Field to store the related account ID
}