<div class="commands">
<ul>
  <#list parameters.regionGroup.regions as region>
    <li><#list region.sequence as regionRef><#if regionRef.name?exists><a class="command" href="${regionRef.path}">${regionRef.name}</a> </#if></#list></li>
  </#list>
</ul>
</div>