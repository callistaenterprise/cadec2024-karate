function fn() {
  if (karate.get('stockChannel')) {
    stockChannel.close();
  }
  if (karate.get('levelChannel')) {
    levelChannel.close();
  }
}