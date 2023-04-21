package com.kuehnenagel.citylist.features.citymanagement;

import com.kuehnenagel.citylist.common.persistence.PersistentEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class City extends PersistentEntity {

    private String name;

    @Column(name="photo_link")
    private String photoLink;

}
