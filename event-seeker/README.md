Event Seeker
=============================

Assumptions
=============================
- all times are GMT
- price is without unit
- feeds are of manageable size and offer up-to date information

Setting up
=============================
1. deploy the archive
2. start the application (presumably with "activator run")
3. run import-feeds.sh or 'curl' your own data into the application

Using
=============================
Navigate with a browser to http://localhost:9000
The application consist of 4 tabs which implement PYT-1, PYT-2 and PYT-3

Issues
=============================
You really have to run import-feeds.sh before using the application, otherwise not even the database tables are created
The feed import URL is a security issue and should be guarded with access control
The persistence layer is prone to SQL injection; also the SQL is not efficient (i.e. lack of joins)
Pagination results is missing in both the UI and the backend API
Sorting results is missing in the UI
The UI isn't consequent on when form changes automatically update results and when the "search" button must be clicked.
There is no 'loading' indicator when the UI is busy waiting for the server
Network requests are not invalidated when leaving a page and may arrive out of order
The application was tested only with Firefox
The application loads far too many individual external resources; they should be inlined in the build step (i.e. with grunt)

Rundown
=============================
The application is built on Play 2.3 for sala, uses Anorm with an in-memory DB for the persistence layer, 
exposes a JSON API under http://localhost:9000/api/ and runs an angular/bootstrap-ui frontend

There are unit tests for most of the functionality and a few exemplary integration tests.
The frontend makes use of the browser history to encode state.