<!doctype html>
<html lang="en" xmlns:ng="http://angularjs.org" id="ng-app" ng-app="webshop">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title>Webshop</title>
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
<link rel="stylesheet"
	href="static/webshop.css">

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script
	src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.12/angular.min.js"></script>
<script type="text/javascript"
	src="static/controller.js"></script>
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>

<form name="orderForm" ng-controller="OrderController as order">
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
<button class="btn" ng-click="order.submit()">Submit Order</button>

<div ng-show="order.result">
<br/>
<h2 ng-show="order.result.status=='complete'" >Placed order for</h2>
<h2 ng-show="order.result.status=='partial'" >Placed partial order for</h2>
<h2 ng-show="order.result.status=='none'" >Couldn't place order</h2>
<table class="table table-striped table-bordered table-condensed" ng-show="order.result.status!='none'">
<tr>
	<td>Date</td><td>{{order.result.day}}</td>
</tr>
<tr ng-show="order.milk">
	<td>Milk (lt)</td><td>{{order.result.milk}}</td>
</tr>
<tr ng-show="order.wool">
	<td>Wool (units)</td><td>{{order.result.wool}}</td>
</tr>
<tr>
	<td>Order number</td><td>{{order.result.id}}</td>
</tr>
</table>
</div>
</form>


</body>
</html>
