var webshopApp = angular.module('webshopApp',['ngRoute']);

webshopApp.value('$strapConfig');
webshopApp.config(function($routeProvider){
	$routeProvider.
	when('/', {
		templateUrl : 'templates/search-book.html'
	}).
	when('/search/:query/:page', {
		templateUrl : 'templates/search-book.html'
	}).
	when('/borrow/:id', {
		templateUrl : 'templates/borrow-page.html',
		controller : 'BorrowBookController'
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

webshopApp.run(['$route', '$rootScope', '$location', function ($route, $rootScope, $location) {
    var original = $location.path;
    $location.path = function (path, reload) {
        if (reload === false) {
            var lastRoute = $route.current;
            var un = $rootScope.$on('$locationChangeSuccess', function () {
                $route.current = lastRoute;
                un();
            });
        }
        return original.apply($location, [path]);
    };
}]);