package com.blueskyminds.homebyfive.business.contact.interaction;

import com.blueskyminds.homebyfive.framework.core.AbstractEntity;
import com.blueskyminds.homebyfive.business.contact.interaction.POCInteraction;
import com.blueskyminds.homebyfive.business.contact.PartyPOC;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

/**
 * Maps a PartyPOC to a PartyContactInteraction
 *
 * Date Started: 6/08/2007
 * <p/>
 * History:
 */
@Entity
@Table(name="PCIMap")
public class PCIMap extends AbstractEntity {

    private POCInteraction pocInteraction;
    private PartyPOC partyPOC;


    public PCIMap(POCInteraction pocInteraction, PartyPOC partyPOC) {
        this.pocInteraction = pocInteraction;
        this.partyPOC = partyPOC;
    }

    public PCIMap() {
    }

    @ManyToOne
    @JoinColumn(name="POCInteractionId")
    public POCInteraction getPocInteraction() {
        return pocInteraction;
    }

    public void setPocInteraction(POCInteraction pocInteraction) {
        this.pocInteraction = pocInteraction;
    }

    @ManyToOne
    @JoinColumn(name="PartyPOCId")
    public PartyPOC getPartyPOC() {
        return partyPOC;
    }

    public void setPartyPOC(PartyPOC partyPOC) {
        this.partyPOC = partyPOC;
    }
}
