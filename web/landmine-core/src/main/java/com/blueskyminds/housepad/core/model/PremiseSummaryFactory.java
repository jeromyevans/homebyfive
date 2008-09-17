package com.blueskyminds.housepad.core.model;

import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.housepad.core.region.composite.RegionCompositeFactory;
import com.google.inject.Inject;

import java.util.Collection;

/**
 * Date Started: 30/10/2007
 * <p/>
 * History:
 */
public class PremiseSummaryFactory {

    private RegionCompositeFactory regionCompositeFactory;

    public PremiseSummary create(Premise premise) {
        PremiseSummary premiseSummary = new PremiseSummary();

        premiseSummary.setId(premise.getId());
        premiseSummary.setAddress(regionCompositeFactory.create(premise.getAddress()));
        premiseSummary.setType(premise.getType());
        premiseSummary.setBedrooms(premise.getBedrooms());
        premiseSummary.setBathrooms(premise.getBathrooms());
        
        return premiseSummary;
    }

    public PremiseSummary[] createList(Collection<Premise> premises) {
        PremiseSummary[] list = new PremiseSummary[premises.size()];
        int index = 0;
        for (Premise premise : premises) {
            list[index++] = create(premise);
        }
        return list;
    }

    public TableModel createTable(Collection<Premise> premises) {
        TableModel tableModel = TableModelBuilder.createModel().withCaption("List of properties");

        tableModel.addHiddenColumn("Id", "id");
        tableModel.addColumn("Street No", "streetNo").withAbbr("No.");
        tableModel.addSortableColumn("Street", "street").formattedAs("Region").withAbbr("St.");
        tableModel.addSortableColumn("Suburb", "suburb").formattedAs("Region");
        tableModel.addSortableColumn("PostCode", "postCode").formattedAs("Region");
        tableModel.addSortableColumn("State", "state").formattedAs("Region");
        tableModel.addSortableColumn("Type", "type");
        tableModel.addSortableColumn("Bedrooms", "bedrooms").withType(ColumnType.Number);
        tableModel.addSortableColumn("Bathrooms", "bathrooms").withType(ColumnType.Number);

        new PremiseTableMapper(premises).populate(tableModel);                             

        return tableModel;
    }

    @Inject
    public void setRegionCompositeFactory(RegionCompositeFactory regionCompositeFactory) {
        this.regionCompositeFactory = regionCompositeFactory;
    }
}
