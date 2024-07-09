package com.madeeasy.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "`user`")
@Getter
@Setter
public class User {


    @Id
    private String id;

    private String name;
    @Column(unique = true)
    private String email;
    private String pin;
    private String phone;
    private String address;

    @ElementCollection(fetch = FetchType.EAGER) // Eagerly fetch the roles collection
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private List<Role> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Account account;
}
