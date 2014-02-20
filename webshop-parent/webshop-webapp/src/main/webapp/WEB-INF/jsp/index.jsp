<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %><!doctype html>
<html lang="en" xmlns:ng="http://angularjs.org" ng-app="webshopApp">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title><s:message code="ui.shopName"/></title>
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
<link rel="stylesheet"
	href="static/css/webshop.css">
<link rel="stylesheet"
	href="http://mgcrea.github.io/angular-strap/1.0/vendor/bootstrap-datepicker.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>

<script
	src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="http://code.angularjs.org/1.2.13/angular.js"></script>
<script type="text/javascript"
	src="http://code.angularjs.org/1.2.13/angular-route.js"></script>
<script type="text/javascript" src="http://mgcrea.github.io/angular-strap/1.0/vendor/bootstrap-datepicker.js"></script>

<script type="text/javascript"
	src="static/js/controller.js"></script>

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body ng-controller="MainController">
<div class='main-panel' ng-view>
</div>
</body>
</html>
