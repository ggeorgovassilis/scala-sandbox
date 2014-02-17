angular.module('webshop', []).controller(
		'OrderController',
		function($http) {
			var order = this;
			order.customer='test';
			order.milk=1;
			order.wool=2;
			this.validate = function validate(order){
				var errors = {};
				if (!order.customer ||order.customer == "")
					errors.customer = "Please enter the customer name";
				if (!(order.milk>0 || order.wool >0))
					errors.quantity = "Please speficy a quantity of either milk or wool."
				if (errors.customer || errors.quantity)
					return errors;
			};
			
			this.submit = function pay() {
				order.errors = this.validate(order);
				if (!order.errors){
					var dto={customer:order.customer, 
							order:{
								milk:order.milk,
								skins:order.wool
							}
					};
				    $http({method: 'POST', url: 'api/order/1', data:dto}).
				    success(function(data, status, headers, config) {
				    	var s = status==201?"complete":"partial";
				    	order.result = {wool:data.skins, milk:data.milk, status:s};
				    }).
				    error(function(data, status, headers, config) {
				    	order.result = {wool:data.skins, milk:data.milk, status:"none"};
				    });
				}
			};
		});