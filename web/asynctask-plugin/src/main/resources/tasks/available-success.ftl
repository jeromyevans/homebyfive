<#-- list of available tasks -->
<@s.if test="taskCount > 0">
<table cellspacing="2" cellpadding="2">
  <thead>
    <tr>
      <th>Key</th>
      <th>Params</th>
      <th>Commands</th>
    </tr>
  </thead>
<@s.iterator>
  <tr>
    <td><@s.property value="key"/></td>
    <td>
      <@s.iterator value="params.entrySet()">
        <@s.property value="key"/>=<@s.property value="value" default="null"/>&nbsp;
      </@s.iterator>
    </td>
    <td>
      <form action="/tasks/controlPanel/<@s.property value="key"/>" method="POST">
        <@s.hidden name="_method" value="PUT"/>
        <@s.iterator value="availableParams">
          <@s.textfield name="params['%{key}']" label="%{key}" value="%{value}"/>
        </@s.iterator>
        <@s.submit name="_start" value="Start"/>
      </form>
    </td>

  </tr>
</@s.iterator>
</table>
</@s.if>
<@s.else>
  <p>No tasks</p>
</@s.else>
<@s.date name="now"/>