# eposrestapp
Anand epos checkout rest app

This is a rest application

Step 1 Calculate total cost
URL: in local machine
http://localhost:8080/epos/products?productlist=apple,apple,apple,Orange,Orange,Orange,Orange
URL: in cloud
http://52.213.142.65:8080/epos/products?productlist=apple,orange,apple

Step 2 Calculate total cost with offer
URL: in local machine
http://localhost:8080/epos/products?productlist=apple,apple,apple,Orange,Orange,Orange,Orange&offerlist=OFFER_BOGOF-apple,offer_3for2-Orange
URL: in cloud
http://52.213.142.65:8080/epos/products?productlist=apple,orange,apple&offerlist=OFFER_BOGOF-apple

Note:
Query parameters values for product (e.g., apple, orange) and offer (e.g., OFFER_BOGOF-apple) are not case sensitive.
Application can be deployed as war file or can start from spring boot class EposrestappApplication. Application hosted in AWS EC2 (http://localhost:8080/epos)
