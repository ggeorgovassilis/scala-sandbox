<table class="table table-striped table-bordered table-condensed" ng-show="order.receipt">
<tr>
	<td>Date</td><td>{{order.receipt.date.getDate() | numberFixedLen:2}}/{{order.receipt.date.getMonth()+1  | numberFixedLen:2}}/{{order.receipt.date.getFullYear() | numberFixedLen:4}}</td>
</tr>
<tr>
	<td>Customer</td><td>{{order.receipt.customerName}}</td>
</tr>
<tr ng-show="order.receipt.milk">
	<td>Milk (lt)</td><td>{{order.receipt.milk}}</td>
</tr>
<tr ng-show="order.receipt.skins">
	<td>Wool (units)</td><td>{{order.receipt.skins}}</td>
</tr>
<tr>
	<td>Order number</td><td><a href='#/order/{{order.receipt.id}}'>{{order.receipt.id}}</a></td>
</tr>
</table>
<div class="error" ng-show="order.errors.message">{{order.errors.message}}</div>