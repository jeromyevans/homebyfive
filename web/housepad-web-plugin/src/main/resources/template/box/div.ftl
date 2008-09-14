<div <#rt>
<#if parameters.id?exists> <#t>
 id="${parameters.id?html}" <#rt>
</#if> <#t>
<#t>
<#if parameters.cssClass?exists> <#t>
 class="borderbox ${parameters.cssClass?html}" <#rt>
<#else>
 class="borderbox" <#rt>
</#if><#t>
<#t>
<#if parameters.cssStyle?exists><#t>
 style="${parameters.cssStyle?html}" <#rt>
</#if>><#t>
  <div class="borderboxtop">
    <div></div>
  </div>
  <div class="borderboxcontent">
  