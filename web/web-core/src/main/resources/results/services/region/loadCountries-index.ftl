<h1>Load Countries from CSV</h1>
<p>Countries will be posted to the target host.  Use the 2 column CSV: "Name", "2 digit code" </p>
<@s.form id="uploadForm" name="uploadForm" action="loadCountries" enctype="multipart/form-data" method="POST" theme="xhtml">
  <@s.checkbox label="Perform update instead of Create" name="updateOnly" value="%{updateOnly}" theme="xhtml"/>
  <@s.textfield label="Hostname" name="hostname" value="%{hostname}" theme="xhtml"/>
  <@s.file label="CSV file" name="upload" required="true" value="%{uploadFileName}"/>
  <input type="submit" name="_submit" value="Upload"/>
</@s.form>

