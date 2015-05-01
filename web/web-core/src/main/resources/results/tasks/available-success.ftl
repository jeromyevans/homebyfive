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
  <tbody>
<@s.iterator>
  <tr>
    <td><@s.property value="key"/></td>
    <td>
      <@s.iterator value="params.entrySet()">
        <@s.property value="key"/>=<@s.property value="value" default="null"/>&nbsp;
      </@s.iterator>
    </td>
    <td>
      <@s.url id="url" value="/tasks/control/%{key}"/>
      <form action="<@s.property value="#url"/>" method="POST">      
        <@s.hidden name="_method" value="PUT"/>
        <@s.iterator value="availableParams">
          <@s.textfield name="params['%{key}']" label="%{key}" value="%{value}"/>
        </@s.iterator>        
        <input type="submit" name="_start" value="Start"/>
        <#-- <@s.submit name="_start" value="Start"/>-->
      </form>
    </td>
  </tr>  
</@s.iterator>
  </tbody>
</table>
</@s.if>
<@s.else>
  <p>No tasks</p>
</@s.else>
<@s.date name="now"/>