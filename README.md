# eposrestapp
Anand epos checkout rest app

This is a rest application
URL when running on local machine
Step 1 Calculate total cost
http://localhost:8080/epos/products?productlist=apple,apple,apple,Orange,Orange,Orange,Orange
Step 2 Calculate total cost with offer
http://localhost:8080/epos/products?productlist=apple,apple,apple,Orange,Orange,Orange,Orange&offerlist=OFFER_BOGOF-apple,offer_3for2-Orange
Query parameters values product (e.g., apple, orange) and offer values (e.g., OFFER_BOGOF-apple) are not case sensitive.

Application can be deployed as war file or can start from spring boot class EposrestappApplication.
