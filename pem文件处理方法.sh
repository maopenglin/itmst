openssl genrsa  -out private.key 2048
chmod 0600 private.key
security import private.key -k ~/Library/Keychains/login.keychain

openssl req -new  -key private.key  -out CertificateSigningRequest.certSigningRequest -subj "/emailAddress=apple@cmstop.com/CN=maopenglin/C=CN/ST=BJ/L=BeiJing"



chmod 0600 aps-18.cer
security import aps-18.cer -k ~/Library/Keychains/login.keychain

openssl x509 -in aps-18.cer -inform DER -out apn_developer_identity.pem -outform PEM
openssl rsa -in private.key -text > private.pem
openssl rsa -out private_key_noenc.pem -in private.pem

openssl pkcs12 -export -in apn_developer_identity.pem -inkey private_key_noenc.pem -certfile CertificateSigningRequest.certSigningRequest -name "apn_developer_identity" -out apn_developer_identity.p12 -passin pass:xxxx -passout pass:1
openssl pkcs12 -in apn_developer_identity.p12 -out apn_dev.pem -nodes -passin pass:1
