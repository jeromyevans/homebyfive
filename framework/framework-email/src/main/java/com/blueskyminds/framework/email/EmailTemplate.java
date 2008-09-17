package com.blueskyminds.framework.email;

import com.blueskyminds.framework.AbstractDomainObject;

import javax.persistence.Entity;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Table;

/**
 * A re-usable template for stock emails
 *
 * Date Started: 14/05/2008
 */
@Entity
@Table(name="EmailTemplate")
public class EmailTemplate extends AbstractDomainObject {

    private String key;
    private String category;
    private String name;
    private String description;
    private String subject;
    private String content;
    private String format;
    private String mimeType;
    private String language;

    public EmailTemplate(String key, String category, String name, String description, String subject, String content, String format, String mimeType, String language) {
        this.key = key;
        this.category = category;
        this.name = name;
        this.description = description;
        this.subject = subject;
        this.content = content;
        this.format = format;
        this.mimeType = mimeType;
        this.language = language;
    }

    public EmailTemplate() {
    }

    @Basic
    @Column(name="KeyValue")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Basic
    @Column(name="Category")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Basic
    @Column(name="Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name="Subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Basic
    @Column(name="Content", length = 8192)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name="Format")
    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Basic
    @Column(name="MimeType")
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Basic
    @Column(name="Language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
