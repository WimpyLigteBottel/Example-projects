package com.wimpy.dao.entity;

import javax.persistence.*;

@Entity
@Table
public class MtgCard {


    @Id
    @GeneratedValue
    private long id;

    @Column
    private String name;

    public long getId() {
        return id;
    }

    public MtgCard setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MtgCard setName(String name) {
        this.name = name;
        return this;
    }
}
