<#-- list of active tasks -->
<@s.if test="taskCount > 0">
<table>
  <thead>
    <tr>
      <th>Key</th>
      <th>Timetamp</th>
      <th>Status</th>
      <th>Model</th>
      <th>Commands</th>
    </tr>
  </thead>
  <tbody>
<@s.iterator>
  <tr>
    <td><@s.property value="key"/></td>
    <td><@s.date name="timestamp" format="dd-MM-yy hh:mm:ss"/></td>
    <td><@s.property value="status"/></td>
    <td><@s.property value="toXML(model)"/></td>
    <td>
      <@s.url id="url" value="/tasks/controlPanel/%{key}"/>
      <form action="<@s.property value="#url"/>" method="POST">
        <input type="hidden" name="_method" value="delete">
        <input type="submit" name="_stop" value="Stop">
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