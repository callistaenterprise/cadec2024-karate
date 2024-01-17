function databaseAccess(dataSource) {
  var DatabaseAccess = Java.type('util.data.jdbc.DatabaseAccessImpl');
  return new DatabaseAccess(dataSource);
}