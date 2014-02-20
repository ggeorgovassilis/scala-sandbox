<%@ taglib tagdir="/WEB-INF/tags" prefix="w" %>
<form name="orderForm" ng-controller="OrderPlacementController as order">
<h1>Place an order</h1>
<label for="customer-name">Customer name</label>
<input ng-model="order.customer" id="customer-name" required class="form-control" placeholder="Customer name"/>
<div ng-show="order.errors.customer">{{order.errors.customer}}</div>

<label for="milk">Milk quantity (lt)</label>
<input ng-model="order.milk" id="milk" name="milk" type="number" class="form-control" placeholder="Milk (liters)"/>
<div class="error" ng-show="orderForm.milk.$error.number">Not valid number!</div>
<div class="error" ng-show="order.errors.milk">{{order.errors.milk}}</div>

<label for="wool">Wool quantity (hides)</label>
<input ng-model="order.wool" type="number" id="wool" name="wool" class="form-control" placeholder="Wool (hides)"/>
<div class="error" ng-show="orderForm.wool.$error.number">Not valid number!</div>
<div class="error" ng-show="order.errors.wool">{{order.errors.wool}}</div>
<div class="error" ng-show="order.errors.quantity">{{order.errors.quantity}}</div>

<label for="date">Date to execute order (month/day/year)</label>
<input id="datepicker" data-date-format="dd/mm/yyyy" ng-model="order.date" type="date" class="form-control" value="0" placeholder="Day"/>
<div class="error" ng-show="orderForm.date.$error.number">Not valid number!</div>
<div class="error" ng-show="order.errors.day">{{order.errors.day}}</div>
<br/>
<button class="btn btn-primary" ng-click="order.submit()" ng-disabled="order.submitInProgress">Submit Order</button>
<w:back/>
<br/>
<div ng-show="order.receipt">
<h2 ng-show="order.status=='complete'" >Order placed</h2>
<h2 ng-show="order.status=='partial'" >Order partially placed</h2>
<h2 ng-show="order.status=='none'" >Couldn't place order</h2>
<br/>
<div ng-show="order.status!='none'">
<w:order-table/>
</div>
</div>
</form>
