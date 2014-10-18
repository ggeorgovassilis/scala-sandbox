var app = angular.module('app',['ngRoute', 'ui.bootstrap']);



String.prototype.startsWith = function(s){
    return(this.indexOf(s) == 0);
};

// since we're using only a single form we normally don't need a router, but it's an awesome way to keep a record on which URLs are processed
app.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider){
	$routeProvider.
	when('/:mode/:location?/:price/:date/', {
		templateUrl : 'assets/templates/search-page.html',
		controller : 'Router_Tabs'
	}).
	when('/location', {
		templateUrl : 'assets/templates/search-page.html',
		controller : 'Router_Tabs'
	}).
	when('/date', {
		templateUrl : 'assets/templates/search-page.html',
		controller : 'Router_Tabs'
	}).
	when('/interests', {
		templateUrl : 'assets/templates/search-page.html',
		controller : 'Router_Tabs'
	}).
	when('/advanced', {
		templateUrl : 'assets/templates/search-page.html',
		controller : 'Router_Tabs'
	}).
	otherwise({
        redirectTo : '/location',
    	controller : 'Router_Tabs'
    });
}]);

app.value('$strapConfig');

app.controller("Router_Tabs",["$scope","$http","$location","$routeParams","$timeout", function($scope, $http, $location, $routeParams, $timeout){
	var controller = this;
	$timeout(function(){
	$scope.tabLocation = controller.tabLocation = $location.path().startsWith('/location');
	$scope.tabDate = controller.tabDate = $location.path().startsWith('/date');
	$scope.tabInterests = controller.tabDate = $location.path().startsWith('/interests');
	$scope.tabAdvanced = controller.tabDate = $location.path().startsWith('/advanced');
	//these simulated clicks are ugly but the only way to update the tab UI
	if ($scope.tabLocation)
		$("#tab_location a").trigger("click");
	if ($scope.tabDate)
		$("#tab_date a").trigger("click");
	if ($scope.tabInterests)
		$("#tab_interests a").trigger("click");
	if ($scope.tabAdvanced)
		$("#tab_advanced a").trigger("click");
	});
	}]);
