function dataSource(config) {
  var DataSourceBuilder = Java.type('util.data.jdbc.DataSourceBuilder');
  return DataSourceBuilder.dataSource(config);
}