<%@ taglib tagdir="/WEB-INF/tags" prefix="w" %>
<p class="error" ng-show="order.errors.message">{{order.errors.message}}</p>
<div ng-controller="ShowOrderController as order">
<w:order-table/>
</div>
<a href="#/">Back to overview</a>
