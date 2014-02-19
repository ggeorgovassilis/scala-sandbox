<%@ taglib tagdir="/WEB-INF/tags" prefix="w" %>
<form name="orderForm" ng-controller="OrderPlacementController as order">
<h1>Place an order</h1>
<label for="customer-name">Customer name</label>
<input ng-model="order.customer" id="customer-name" required class="form-control" placeholder="Customer name"/>
<div ng-show="order.errors.customer">{{order.errors.customer}}</div>

<label for="milk">Milk quantity (lt)</label>
<input ng-model="order.milk" id="milk" name="milk" type="number" min="0" class="form-control" placeholder="Milk (liters)"/>
<div class="error" ng-show="orderForm.milk.$error.number">Not valid number!</div>

<label for="wool">Wool quantity (hides)</label>
<input ng-model="order.wool" type="number" min="0" id="wool" name="wool" class="form-control" placeholder="Wool (hides)"/>
<div class="error" ng-show="orderForm.wool.$error.number">Not valid number!</div>

<div ng-show="order.errors.quantity">{{order.errors.quantity}}</div>
<br/>
<button class="btn btn-primary" ng-click="order.submit()" ng-disabled="order.submitInProgress">Submit Order</button>
<w:back/>
<br/>
<div ng-show="order.receipt">
<h2 ng-show="order.result.status=='complete'" >Placed order for</h2>
<h2 ng-show="order.result.status=='partial'" >Placed partial order for</h2>
<h2 ng-show="order.result.status=='none'" >Couldn't place order</h2>
<br/>
<w:order-table/>
</div>
</form>