function fn(creds) {
  var auth = creds.username + ':' + creds.password;
  var Base64 = Java.type('java.util.Base64');
  var encoded = Base64.getEncoder().encodeToString(auth.toString().getBytes());
  return 'Basic ' + encoded;
}