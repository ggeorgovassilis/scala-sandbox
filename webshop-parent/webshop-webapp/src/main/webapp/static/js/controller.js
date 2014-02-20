/**
 * angular.js controllers for the webshop application.
 * This module defines four routes:
 * 
 * / MainController
 * Shows the home page
 * 
 * /order/{id} ShowOrderController
 * Shows an order identified by ID
 * 
 * /place-order PlaceOrderController
 * Validates an order form, places the order and shows the newly placed order
 * 
 * /search-order SearchOrderController
 * Searches for an order based in its ID
 * 
 */
var webshopApp = angular.module('webshopApp',['ngRoute']);

webshopApp.value('$strapConfig');
webshopApp.config(function($routeProvider){
	$routeProvider.
	when('/', {
		templateUrl : 'templates/main-page.html',
		controller : 'MainController'
	}).
	when('/search-order', {
		templateUrl : 'templates/search-order-form.html',
		controller : 'SearchOrderController'
	}).
	when('/place-order', {
		templateUrl : 'templates/order-placement-form.html',
		controller : 'OrderPlacementController'
	}).
	when('/order/:orderId',{
		templateUrl : 'templates/show-order-page.html',
		controller : 'ShowOrderController'
	})
	.otherwise({
        redirectTo : '/'
    });
});


webshopApp
.filter('numberFixedLen', function () {
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
});
webshopApp.filter('escape', function() {
	return window.escape;
});

function unpack(receipt){
	receipt.date = new Date(receipt.date);
	return receipt;
}

webshopApp.controller("MainController",["$scope","$http",
function ($scope, $http) {
}]);


webshopApp.controller("ShowOrderController",["$scope","$http","$location","$routeParams", function($scope, $http, $location, $routeParams){
	var order = this;
	order.id = $routeParams.orderId;
	order.submitInProgress = true;
	order.errors = null;
	$http({
		method : 'GET',
		url : 'api/order/'+order.id,
	}).success(function(data, status, headers, config) {
		order.submitInProgress = false;
		order.receipt = unpack(data);
	}).error(function(data, status, headers, config) {
		order.receipt = null;
		order.submitInProgress = false;
		if (status == 404){
			order.errors = {message:"No order with ID "+order.id};
		} else{
			order.errors = {message:"There was some technical problem"};
		}
	});
	
}]);

webshopApp.controller("SearchOrderController",["$scope","$http","$location",
function($scope, $http, $location){
	var order = this;
	this.validate = function validate(order) {
		if (!order.id || order.id == ""){
			return {id:"Please enter the order number"};
		};
	};
	
	this.submit = function(){
		$location.path("/order/"+order.id);
	};
}]);

function pad(num){
	var s = num;
	if (num<10)
		s="0"+s;
	return s;
}

function parseDate(s){
	var parts = s.split("/");
	var date=  new Date(parts[2], parseInt(parts[1])-1, parts[0]);
	return date;
	
}


var _MS_PER_DAY = 1000 * 60 * 60 * 24;

function dateDiffInDays(a, b) {
	var utc1 = Date.UTC(a.getFullYear(), a.getMonth(), a.getDate());
	var utc2 = Date.UTC(b.getFullYear(), b.getMonth(), b.getDate());
	return Math.floor((utc2 - utc1) / _MS_PER_DAY);
}

webshopApp.controller("OrderPlacementController",["$scope","$http", function ($scope, $http) {
	$( "#datepicker" ).datepicker({dateFormat: "dd mm yy", showButtonPanel: true});

	var order = this;
	$scope.showNewOrderForm = function() {
		order.customer = 'test';
		order.milk = 1;
		order.wool = 2;
		var now = new Date();
		order.date = pad(now.getDate())+"/"+pad(now.getMonth()+1)+"/"+now.getFullYear();
		order.validate = function validate(order) {
			var errors = {};
			if (!order.customer || order.customer == "")
				errors.customer = "Please enter the customer name";
			if (!(order.milk > 0 || order.wool > 0))
				errors.quantity = "Please speficy a quantity of either milk or wool.";
			
			var orderDate = parseDate(order.date);
			var daysToOrder = dateDiffInDays(new Date(), orderDate);
			
			if (daysToOrder<1)
				errors.day = "Please enter a date in the future";
			else
				order.day = daysToOrder;
			if (errors.customer || errors.quantity || errors.day)
				return errors;
		};

		
		order.submit = function() {
			order.status = null;
			order.errors = order.validate(order);
			if (!order.errors) {
				var dto = {
					customer : order.customer,
					order : {
						milk : order.milk,
						skins : order.wool,
					}
				};
				order.submitInProgress = true;
				$http({
					method : 'POST',
					url : 'api/order/'+order.day,
					data : dto
				}).success(function(data, status, headers, config) {
					var s = status == 201 ? "complete" : "partial";
					order.submitInProgress = false;
					order.receipt = unpack(data);
					order.status = s;
				}).error(function(data, status, headers, config) {
					order.submitInProgress = false;
					if (status == 404){
						order.status = 'none';
						order.errors = {};
					} else {
						order.errors = data.fieldErrors;
						order.status = 'none';
					}
					order.receipt = {};
				});
			}
			;
		};
	};
	$scope.showNewOrderForm($scope, $http);
}]);
