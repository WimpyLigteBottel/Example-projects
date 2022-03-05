package com.wimpy.dao.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table
public class MtgHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @JoinColumn
    @OneToOne(fetch = FetchType.LAZY)
    private MtgCard mtgCard;

    @Column
    private String link;
    @Column
    private BigDecimal price;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public MtgCard getMtgCard() {
        return mtgCard;
    }

    public void setMtgCard(MtgCard mtgCard) {
        this.mtgCard = mtgCard;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
