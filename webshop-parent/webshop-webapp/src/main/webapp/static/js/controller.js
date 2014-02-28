/**
 * angular.js controllers for the webshop application.
 * This module defines four routes:
 * 
 * / SearchBookController
 * Shows the home page with a search form and search result list
 * 
 * 
 */
var webshopApp = angular.module('webshopApp',['ngRoute']);

webshopApp.value('$strapConfig');
webshopApp.config(function($routeProvider){
	$routeProvider.
	when('/', {
		templateUrl : 'templates/main-page.html',
		controller : 'SearchBookController'
	}).
	when('/search/:query/:page', {
		templateUrl : 'templates/main-page.html',
		controller : 'SearchBookController'
	}).
	otherwise({
        redirectTo : '/'
    });
}).filter('numberFixedLen', function () {
    return function (n, len) {
        var num = parseInt(n, 10);
        len = parseInt(len, 10);
        if (isNaN(num) || isNaN(len)) {
            return n;
        }
        num = ''+num;
        while (num.length < len) {
            num = '0'+num;
        }
        return num;
    };
}).filter('escape', function() {
	return window.escape;
});

webshopApp.controller("SearchBookController",["$scope","$http","$location","$routeParams",
function($scope, $http, $location, $routeParams){
	var book = this;
	book.pageSize = 10;
	$scope.parseInt = parseInt;
	this.validate = function validate(book) {
		if (!book.query || book.query == ""){
			return {query:"Please enter the order number"};
		};
	};

	this.gotoPage = function(query, page){
		$location.path("/search/"+book.query+"/"+page);
	};

	this.showPage = function(query, page){
		book.page = page;
		book.query = query;
		$http({method: 'GET', url: 'api/books/search/'+query+"?page="+page}).
		success(function(data, status, headers, config) {
			book.results = data;
		}).
		error(function(data, status, headers, config) {
			book.errors = data;
			book.results = null;
		});	
	};
	
	this.previousPage = function(){
		book.gotoPage(book.query, book.page-1);
	};
	
	this.nextPage = function(){
		book.gotoPage(book.query, parseInt(book.page)+1);
	};

	this.submit = function(){
		book.errors = book.validate(book);
		if (book.errors)
			return;
		book.gotoPage(book.query, 0);
	};
	
	if ($routeParams.page){
		book.showPage($routeParams.query, $routeParams.page);
	}
}]);