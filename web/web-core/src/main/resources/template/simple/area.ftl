<#include "/${parameters.templateDir}/${parameters.theme}/controlheader.ftl" />

<#if (parameters.editMode?default("View") == "Edit" || parameters.editMode?default("View") == "Add" || editMode?default("View") == "Edit" || editMode?default("View") == "Add") >
<#--
EditMode: Create a text field and select
The select is populated with the list named by the xUnitsMap expression
-->
<input id="${parameters.id}" type="text" name="${parameters.name}.amount" class="text <#if parameters.required?default(false)>required</#if>"
<#if parameters.tabindex?exists>
 tabindex="${parameters.tabindex?html}"<#rt/>
</#if>
<#if parameters.nameValue?exists>
 value="<@s.property value="parameters.nameValue.amount"/>"<#rt/>
</#if>/>
<@s.select theme="simple" name="${parameters.name}.units" list="${parameters.xUnitsMap}" value="parameters.nameValue.units"/>
<#else>
<#--
ViewMode: Display the current amount and units
-->
<div id="${parameters.id?html}" class="text">
<@s.property value="parameters.nameValue.amount" default="-"/>
<@s.property value="parameters.nameValue.units" default=""/>
</div>
</#if>

<#include "/${parameters.templateDir}/${parameters.theme}/controlfooter.ftl" />