webshopApp.controller("SearchBookController",["$scope","$http","$location","$routeParams", "BookService",
function($scope, $http, $location, $routeParams, BookService){
	var book = this;
	book.pageSize = 10;
	book.page = 0;
	$scope.parseInt = parseInt;
	this.validate = function validate(book) {
		if (!book.query || book.query == ""){
			return {query:"Please enter a search term"};
		};
	};

	this.submit = function(){
		book.errors = book.validate(book);
		if (book.errors)
			return;
		BookService.gotoPage(book.query, 0).then(function(response){
			book.results = response.data;
			book.errors = null;
		}, function(response){
			book.results=null;
			book.errors = response.data;
		});
	};
	
	if ($routeParams.page){
		book.page = parseInt($routeParams.page);
		book.query = $routeParams.query;
		BookService.showSearchResults(book.query, book.page, function(data){
			book.errors = null;
			book.results = data;
		},function(data){
			book.errors = data;
			book.results = null;
		});
	}
}]);

webshopApp.controller("BorrowBookController",["$scope","$http","$location","$routeParams",
     function($scope, $http, $location, $routeParams){
     	var order = this;
     	order.id = $routeParams.id;
     	$scope.parseInt = parseInt;
     	this.validate = function validate(order) {
     		if (!order.clientName){
     			return {clientName:"Please enter the name of the person who is borrowing the book"};
     		};
     	};
     	
     	this.loadBook = function(id){
     		$http({method: 'GET', url: 'api/books/'+id}).
     		success(function(data, status, headers, config) {
     			order.book = data;
     		}).
     		error(function(data, status, headers, config) {
     			order.errors = data;
     		});	
     	};

 		
     	this.submit = function(){
     		order.errors = order.validate(order);
     		if (order.errors)
     			return;
     		var params = $.param({clientName: order.clientName});
     		$http({method: 'POST', url: 'api/borrow/'+order.id, data:params, headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).
     		success(function(data, status, headers, config) {
     			order = data;
     		}).
     		error(function(data, status, headers, config) {
     			order.errors = data;
     		});	
     	};
     	
    	this.loadBook(order.id);
    }]);

