<h1>Load Postal Codes from CSV</h1>
<p>Postal Codeswill be posted to the target host.</p>
<@s.form id="uploadForm" name="uploadForm" action="loadPostalCodes" enctype="multipart/form-data" method="POST" theme="xhtml">
  <@s.checkbox label="Perform update instead of Create" name="updateOnly" value="%{updateOnly}" theme="xhtml"/>
  <@s.checkbox label="Attempt Update if Create fails" name="updateOrCreate" value="%{updateOrCreate}" theme="xhtml"/>
  <@s.textfield label="Hostname" name="hostname" value="%{hostname}" theme="xhtml"/>
  <@s.file label="CSV file" name="upload" required="true" value="%{uploadFileName}"/>
  <input type="submit" name="_submit" value="Upload"/>
</@s.form>

