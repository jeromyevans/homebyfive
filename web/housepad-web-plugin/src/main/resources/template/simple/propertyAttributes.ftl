<#-- Render PropertyAttributes -->

<#-- propertyTypes is a Map in the valueStack -->
<#--<@s.select key="property.type" name="type" list=propertyTypes listKey="key" listValue="value" value=parameters.attributes.type title=parameters.showLabelsInViewMode/>-->
<#-- value is not set because it evaluates it to the literal value first (House) and then tries to evaluate that as an expression -->
<@s.select key="property.type" name="type" list=propertyTypes listKey="key" listValue="value" value="%{'${parameters.attributes.type?default('')}'}" title=parameters.showLabelsInViewMode/>
<#-- but that is okay for text fields for some reason -->
<@s.textfield key="property.bedrooms" name="bedrooms" value=parameters.attributes.bedrooms title=parameters.showLabelsInViewMode/>
<@s.textfield key="property.bathrooms" name="bathrooms" value=parameters.attributes.bathrooms title=parameters.showLabelsInViewMode/>
<@s.textfield key="property.carspaces" name="carspaces" value=parameters.attributes.carspaces title=parameters.showLabelsInViewMode/>

<#-- buildingAreaUnits and landAreaUnits are expressions (String) -->
<@hp.area key="property.buildingarea" name="buildingArea" unitsMap="buildingAreaUnits" title=parameters.showLabelsInViewMode/>
<@hp.area key="property.landarea" name="landArea" unitsMap="landAreaUnits" title=parameters.showLabelsInViewMode/>
<@s.textfield key="property.yearbuilt" name="constructionDate" value=parameters.attributes.constructionDate title=parameters.showLabelsInViewMode/>

