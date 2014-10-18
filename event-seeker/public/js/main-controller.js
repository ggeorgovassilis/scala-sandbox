app.controller("MainPageController",["$scope","$http","$location","$routeParams","$timeout", 
function($scope, $http, $location, $routeParams, $timeout){
var controller = this;

this.locationTabSelected = function(){
	if (!/location/.test($location.path()))
		$location.path("/location");
	controller.activeTab = 'Location';
}

this.dateTabSelected = function(){
	if (!/date/.test($location.path()))
		$location.path("/date");
	controller.activeTab = 'Date';
}

this.interestTabSelected = function(){
	if (!/interests/.test($location.path()))
		$location.path("/interests");
	controller.activeTab = 'Interest';
}

this.advancedTabSelected = function(){
	if (!/advanced/.test($location.path()))
		$location.path("/advanced");
	controller.activeTab = 'Advanced search';
}

}]);
