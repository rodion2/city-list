package com.kuehnenagel.citylist.features.usermanagement;

import com.kuehnenagel.citylist.common.persistence.PersistentEntity;
import com.kuehnenagel.citylist.common.security.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "user", schema = "public")
public class User extends PersistentEntity {

    private String name;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

}
