app.controller("SearchController",["$scope","$http","$location","$routeParams","$timeout",
function($scope, $http, $location, $routeParams, $timeout){
var controller = this;

$scope.getLocation = function(val) {
    return $http.get('/api/locations/'+escape(val)).then(function(response){
    	var result=new Array();
    	for (var i=0;i<response.data.length;i++)
    		result[i] = response.data[i];
    	return result;

    });
};

this.makePath = function(){
	var location = sanitize(controller.location);
	var price = controller.price?controller.price:0
	var date = controller.date?controller.date:"anytime"
	
	return "/"+controller.mode+"/"+escape(location)+"/"+escape(price)+"/"+escape(date)+"/"
}

this.onSearchButtonClicked = function(){
	var path = controller.makePath();
	console.log(path)
	$location.path(path);
}

this.onInterestButtonClicked=function(interest){
	interest.selected = !!!interest.selected;
	var queryString = "";
	var prefix = "";
	for (var i=0;i<controller.availableInterests.length;i++){
		var interest = controller.availableInterests[i]
		if (interest.selected){
			console.log(interest)
			queryString+=prefix + interest.eventType+":"+interest.category;
			prefix=","
		}
	}
	$http.get('/api/events/interests?listOfInterests='+escape(queryString))
	.success(function(data){
		controller.results = data;
	})
	.error(function(data){
		alert("Problem loading events")
	})
}

this.loadAvailableInterests=function(){
	if (!app.availableInterests){
		$http.get('/api/interests').success(function(data){
			controller.availableInterests = app.availableInterests = data;
			var eventTypesSet = {}
			var eventTypes = new Array();
			for (var i=0;i<data.length;i++){
				var interest = data[i];
				eventTypesSet[interest.eventType]=true;
			}
			for (var p in eventTypesSet){
				eventTypes.push(p)
			}
			controller.availableEventTypes = app.availableEventTypes = eventTypes;
		}).error(function(data){
			alert("Problem loading interests");
		});
	} else {
		controller.availableEventTypes = app.availableEventTypes;
		controller.availableInterests = app.availableInterests;
	}
}

controller.handleRoute=function($location){
	controller.formFields={
			location:/location|advanced/.test($location.path()),
			date:/date|advanced/.test($location.path()),
			price:/advanced/.test($location.path()),
			advanced:/advanced/.test($location.path()),
			interests:/interests/.test($location.path()),
			search:!/interest|date/.test($location.path())
	}

	if (/location/.test($location.path()))
		controller.mode = "location";
	if (/date/.test($location.path()))
		controller.mode = "date";
	if (/advanced/.test($location.path()))
		controller.mode = "advanced";
	if (/interests/.test($location.path()))
		controller.mode = "interests";

	if ($routeParams.location || $routeParams.price || $routeParams.date!="anytime"){
		controller.location = $routeParams.location;
		controller.date = $routeParams.date=="anytime"?null:$routeParams.date;
		controller.price = parseInt($routeParams.price);
		
		var location = sanitize(controller.location);
		var date = controller.date?toIso(new Date(controller.date)):"";
		var price = controller.price?controller.price:0;
		$http.get('/api/events?eventTypes=&location='+escape(location)+'&maxPrice='+price+'&date='+date+'&orderBy=date').success(function(data){
			controller.results = data;
		}).error(function(data){
			alert(data);
		});
	}
}

controller.loadAvailableInterests();
controller.handleRoute($location);

}]);