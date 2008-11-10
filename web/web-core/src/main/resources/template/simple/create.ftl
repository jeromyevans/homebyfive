<form <#rt>
<#if parameters.id?exists> <#t>
 id="${parameters.id?html}" <#rt>
</#if> <#t>
<#if parameters.handler?exists> <#t>
 data-handler="${parameters.handler?html}" <#rt>
</#if> <#t>
<#t>
 action="${parameters.resourceURI?html}" method="POST">
