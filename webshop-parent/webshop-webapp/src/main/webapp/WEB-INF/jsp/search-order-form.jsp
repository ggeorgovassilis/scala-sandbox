<%@ taglib tagdir="/WEB-INF/tags" prefix="w" %>
<h2>Find an order</h2>
<form name="orderForm" ng-controller="SearchOrderController as order">
<label for="order-id">Order ID</label>
<input ng-model="order.id" id="order-id" required class="form-control" placeholder="Order ID"/>
<br/>
<button class="btn btn-primary" ng-click="order.submit()" ng-disabled="order.submitInProgress">Submit Order</button>
<a href="#/">Back to overview</a>
</form>