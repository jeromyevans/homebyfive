<h1>Load Substitutions from CSV</h1>
<p>Substitutions will be sent to the target host.</p>
<@s.form id="uploadForm" name="uploadForm" action="load" enctype="multipart/form-data" method="POST" theme="xhtml">
  <@s.textfield label="Hostname:" name="hostname" value="%{hostname}" theme="xhtml"/>
  <@s.file label="CSV file:" name="upload" required="true" value="%{uploadFileName}"/>
  <input type="submit" name="_submit" value="Upload"/>
</@s.form>