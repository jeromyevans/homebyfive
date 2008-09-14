package com.blueskyminds.hibernate;

import javax.persistence.*;
import java.util.Date;
import java.io.Serializable;

@Entity
@SequenceGenerator(
    name="SEQ_STORE",
    sequenceName="localSequence"
)
@Table(name="event")
public class Event implements Serializable {
    private Long id;

    private String title;
    private Date date;

    public Event() {}

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STORE")
    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="dateTime", nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Basic
    @Column(name="title", length=20)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}