<#--
Table of regions
Root Context: tableModel
-->
<p>Freemarker Result</p>
<#-- todo: don't use YUI-'s split table,  use one table with same styles.  spit table column widths can't lineup without javascript -->
<div class="datatable yui-dt">
<div class="yui-dt-hd">
<table class="datasource">
  <caption><@s.property value="caption"/></caption>
  <thead>
    <tr class="yui-dt-first yui-dt-last">
      <@s.iterator value="columns" status="col">
        <@s.if test="!hidden">
          <th class="<@s.property value="name" default="column%{col.index}"/> <@s.property value="type" default=""/><@s.if test="sortable"> sortable</@s.if><@s.if test="#col.first">yui-dt-first</@s.if><@s.if test="#col.last">yui-dt-last</@s.if>">
            <div class="yui-dt-liner">
              <span class="yui-dt-label">
                <@s.property value="heading"/>
              </span>
            </div>
          </th>
        </@s.if>
      </@s.iterator>
    </tr>
  </thead>
</table>
</div>
<div class="yui-dt-bd">
  <table>
    <thead>
      <tr>
        <th rowspan="1" colspan="columns.size">
          <@s.property value="caption"/>
        </th>
      </tr>
    </thead>
  <tbody>
  <@s.iterator value="rows" var="row">
    <tr class="<@s.if test="#row.even">yui-dt-even</@s.if><@s.else>yui-dt-odd</@s.else><@s.if test="#row.first">yui-dt-first</@s.if><@s.if test="#row.last">yui-dt-last</@s.if>">
      <@s.iterator value="columns">
        <@s.if test="!hidden">
          <td>
            <div class="yui-dt-liner">
              <@s.property value="get(name)" escape="false"/>
            </div>
          </td>
        </@s.if>
      </@s.iterator>
    </tr>
  </@s.iterator>
  </tbody>
</table>
</div>
</div>



