webshopApp.service("BookService",["$http", "$location",
function($http, $location){
	this.$http = $http;
	this.$location = $location;
	this.pageSize = 10;
	var self = this;
	this.gotoPage = function(query, page){
		$location.path("/search/"+query+"/"+page, false);
		return self.showSearchResults(query, page, function(data){
			
		}, function(data){
			
		});
	};

	this.showSearchResults = function(query, page, callbackOk, callbackError){
		return $http({method: 'GET', url: 'api/books/search/'+query+"?page="+page}).
		success(function(data, status, headers, config) {
			callbackOk(data);
		}).
		error(function(data, status, headers, config) {
			callbackError(data);
		});	
	};
	
}]);

