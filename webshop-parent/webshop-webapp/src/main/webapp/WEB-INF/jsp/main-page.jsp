<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %><!doctype html>
<h2>Welcome to <s:message code="ui.shopName"/>!</h2>
<div class=panel ng-controller="SearchBookController as book">
<form name="searchBooksForm">
<label for="query">Search the library for a book</label>
<input ng-model="book.query" id="query" class="form-control" placeholder="Title" value="book"/>
<div class=error ng-show="book.errors.query">{{book.errors.query}}</div>
<br/>
<table>
<tr>
<td><button class="btn btn-primary" ng-click="book.submit()" ng-disabled="book.submitInProgress">Search</button></td>
<td class=pagenumber>
<span ng-show="book.results">Page {{parseInt(book.page)+1}}</span>
</td>
<td>
<button class="btn btn-default" ng-click="book.previousPage()" ng-disabled="parseInt(book.page)<1" >Previous</button>
</td>
<td>
<button class="btn btn-default" ng-click="book.nextPage()" ng-disabled="book.results.length<book.pageSize">Next</button>
</td>
</tr>
</table>
</form>
<br/>

<div class=error>{{book.errors.message}}</div>
<div class="panel results" ng-repeat="book in book.results">
<table class="table table-striped table-bordered table-condensed">
<tr><td colspan=2><h3>{{book.title}}</h3></td></tr>
<tr><td colspan=2>{{book.publisher.name}}</td></tr>
<tr><td class="firstCell"><label>ISBN</label></td><td>{{book.isbn}}</td></tr>
<tr><td><label>Author(s)</label></td><td><span ng-repeat="author in book.authors"><author>{{author.name}}</author></td></tr>
<tr><td><label>Publication year</label></td><td>{{book.publicationYear}}</td></tr>
<tr><td><label>Availability</label></td><td>{{book.availability?"Available":"Not available"}}</td></tr>
</table>
</div>
</div>