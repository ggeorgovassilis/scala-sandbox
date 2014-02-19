var webshopApp = angular.module('webshopApp',['ngRoute']);

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

webshopApp.filter('escape', function() {
	return window.escape;
});

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
		order.receipt = data;
		console.log(data);
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

webshopApp.controller("OrderPlacementController",["$scope","$http",
function ($scope, $http) {
	var order = this;
	$scope.showNewOrderForm = function() {
		order.customer = 'test';
		order.milk = 1;
		order.wool = 2;
		order.validate = function validate(order) {
			var errors = {};
			if (!order.customer || order.customer == "")
				errors.customer = "Please enter the customer name";
			if (!(order.milk > 0 || order.wool > 0))
				errors.quantity = "Please speficy a quantity of either milk or wool.";
			if (errors.customer || errors.quantity)
				return errors;
		};

		
		order.submit = function() {
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
					url : 'api/order/1',
					data : dto
				}).success(function(data, status, headers, config) {
					var s = status == 201 ? "complete" : "partial";
					order.submitInProgress = false;
					order.receipt = data;
				}).error(function(data, status, headers, config) {
					order.submitInProgress = false;
					order.result = {
						wool : data.skins,
						milk : data.milk,
						status : "none"
					};
				});
			}
			;
		};
	};
	$scope.showNewOrderForm($scope, $http);
}]);
