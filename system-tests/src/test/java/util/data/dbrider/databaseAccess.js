function databaseAccess(dataSource) {
  var DatabaseAccess = Java.type('util.data.dbrider.DBRiderDatabaseAccessImpl');
  return new DatabaseAccess(dataSource);
}