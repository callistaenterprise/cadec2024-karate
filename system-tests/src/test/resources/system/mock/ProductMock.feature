Feature: Product Mock

  Background:
    * def products = read('data/initial.json')
    * def sequence = 1000
    * def nextId = function() {return ++sequence}

  Scenario: pathMatches('/products') && methodIs('post')
    * products[request.articleId] = request
    * products[request.articleId].productId = nextId()
    * products[request.articleId].inventory = 0
    * def response = request
    * def responseStatus = 201

  Scenario: pathMatches('/products/{articleId}') && methodIs('get')
    * def articleId = pathParams.articleId
    * def product = products[articleId]
    * def response = product
    * def responseStatus = product ? 200 : 404

  Scenario: pathMatches('/products/{articleId}') && methodIs('patch')
    * def articleId = pathParams.articleId
    * products[articleId].name = request.name
    * def response = request

  Scenario: pathMatches('/products/{articleId}') && methodIs('delete')
    * def articleId = pathParams.articleId
    * delete products[articleId]
    * def responseStatus = 204

  Scenario: pathMatches('/products') && methodIs('get')
    * def response = karate.valuesOf(products)

# Reset mock
Scenario: requestUri == '/reset'
  * karate.set('products', read('data/initial.json'))
