package com.blueskyminds.business.contact.interaction;

import com.blueskyminds.business.contact.PartyPOC;
import com.blueskyminds.business.contact.interaction.PCIMap;
import com.blueskyminds.homebyfive.framework.core.AbstractEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

/**
 * Record a message/request/interaction between two parties
 *
 * Identifies the party, the points of contact used, the timestamp and the message
 *
 * Date Started: 2/08/2007
 * <p/>
 * History:
 */
@Entity
@Table(name="POCInteraction")
public class POCInteraction extends AbstractEntity {

    private PartyPOC fromParty;
    private Set<PCIMap> toParties;
    private Set<PCIMap> ccParties;
    private Date timestamp;
    private String message;
    private String mimeType;


    public POCInteraction() {
        init();
    }

    public POCInteraction(PartyPOC fromParty, Set<PartyPOC> toParties, Set<PartyPOC> ccParties, Date timestamp, String message, String mimeType) {
        this.fromParty = fromParty;
        this.toParties = prepareMap(toParties);
        this.ccParties = prepareMap(ccParties);
        this.timestamp = timestamp;
        this.message = message;
        this.mimeType = mimeType;
    }

    public POCInteraction(PartyPOC fromParty, Set<PartyPOC> toParties, String message, String mimeType) {
        init();
        this.fromParty = fromParty;
        this.toParties = prepareMap(toParties);
        this.message = message;
        this.mimeType = mimeType;
    }

    private void init() {
        toParties = new HashSet<PCIMap>();
        ccParties = new HashSet<PCIMap>();
        timestamp = new Date();
    }

    /** Transfers PartyPOC's into a map to this PCI */
    private Set<PCIMap> prepareMap(Set<PartyPOC> partyPOCs) {
        Set<PCIMap> maps = new HashSet<PCIMap>();
        if (partyPOCs != null) {
            for (PartyPOC partyPOC : partyPOCs) {
                maps.add(new PCIMap(this, partyPOC));
            }
        }
        return maps;
    }
    @ManyToOne
    @JoinColumn(name = "PartyPOCId")
    public PartyPOC getFromParty() {
        return fromParty;
    }

    public void setFromParty(PartyPOC fromParty) {
        this.fromParty = fromParty;
    }

    public void addTo(PartyPOC partyPOC) {
        toParties.add(new PCIMap(this, partyPOC));
    }

    @OneToMany(mappedBy = "pocInteraction", cascade = CascadeType.ALL)
    public Set<PCIMap> getToParties() {
        return toParties;
    }

    public void setToParties(Set<PCIMap> toParties) {
        this.toParties = toParties;
    }

    public void addCC(PartyPOC partyPOC) {
        ccParties.add(new PCIMap(this, partyPOC));
    }

    @OneToMany(mappedBy = "pocInteraction", cascade = CascadeType.ALL)
    public Set<PCIMap> getCcParties() {
        return ccParties;
    }

    public void setCcParties(Set<PCIMap> ccParties) {
        this.ccParties = ccParties;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Timestamp")
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Basic
    @Column(name="Message", length = 16384)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Basic
    @Column(name="MimeType")
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
