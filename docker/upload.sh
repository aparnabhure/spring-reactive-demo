# about the file
file_to_upload=/jacoco/jacoco.exec
bucket=apbhurebucket1
filepath="/${bucket}/${file_to_upload}"

# metadata
contentType="application/x-compressed-tar"
dateValue=`date -R`
signature_string="PUT\n\n${contentType}\n${dateValue}\n${filepath}"

#s3 keys
s3_access_key=<your s3 access key>
s3_secret_key=<your s3 secret key>

#prepare signature hash to be sent in Authorization header
signature_hash=`echo -en ${signature_string} | openssl sha1 -hmac ${s3_secret_key} -binary | base64`

# actual curl command to do PUT operation on s3
curl -X PUT -T "${file_to_upload}" \
  -H "Host: ${bucket}.s3.amazonaws.com" \
  -H "Date: ${dateValue}" \
  -H "Content-Type: ${contentType}" \
  -H "Authorization: AWS ${s3_access_key}:${signature_hash}" \
  https://${bucket}.s3.amazonaws.com/${file_to_upload}