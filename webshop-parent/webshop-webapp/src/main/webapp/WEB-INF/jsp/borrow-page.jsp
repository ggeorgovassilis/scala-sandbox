<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %><!doctype html>
<div class=panel ng-controller="BorrowBookController as order">
<form name="borrowBooksForm">
<label for="clientName">Customer name</label>
<input ng-hide="order.dateLoaned" ng-model="order.clientName" id="clientName" class="form-control" placeholder="Customer name"/>
<div class=error ng-show="order.errors.clientName">{{order.errors.clientName}}</div>
<br/>
<button class="btn btn-primary" ng-click="order.submit()" ng-disabled="order.submitInProgress">Borrow</button>
</form>
<br/>

<div class=error>{{order.errors.message}}</div>
<table class="table table-striped table-bordered table-condensed">
<tr><td colspan=2><h3>{{order.book.title}}</h3></td></tr>
<tr><td colspan=2>{{order.book.publisher.name}}</td></tr>
<tr><td class="firstCell"><label>ISBN</label></td><td>{{order.book.isbn}}</td></tr>
<tr><td><label>Author(s)</label></td><td><author ng-repeat="author in order.book.authors">{{author.name}}</author></td></tr>
<tr><td><label>Publication year</label></td><td>{{order.book.publicationYear}}</td></tr>
<tr><td><label>Availability</label></td><td>{{order.book.availability?"":"Not available"}} <a ng-show="order.book.availability" href="#/borrow/{{book.id}}">Available for borrowing</a></td></tr>
</table>
</div>