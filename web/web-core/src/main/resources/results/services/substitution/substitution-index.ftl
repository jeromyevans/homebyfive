<h1>Synchronize with Host</h1>
<p>Current substitutions will be posted to the target host.</p>
<@s.form id="synchronizeForm" name="synchronizeForm" action="synchronize"method="POST" theme="xhtml">
  <@s.textfield label="Hostname:" name="hostname" value="%{hostname}" theme="xhtml"/>
  <input type="submit" name="_submit" value="Synchronize Target"/>
</@s.form>

<h1>Substitutions</h1>
<#include "/results/datatable.ftl">