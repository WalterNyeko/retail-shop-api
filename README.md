## API Documentation
1. Cloning the project
- Move to the desired workspace on your computer and execute the git command `git clone https://github.com/WalterNyeko/retail-shop-api.git`   

2. Database Setup
- This application uses a relational database to persist its data
- You can either use embedded database or MySQL.
- If you would like to use H2, go to `src/main/resources/application.properties` and uncomment line `1-7`, comment out line `9-16` to avoid conflicts
- If you would like to use MySQL, just change *${DATABASE}*, *${USERNAME}*, and *{PASSWORD}* with their real values or provide their values in the environment variable by creating a `.env` file in the project root directory and providing values for those three fields (DATABASE, USERNAME, and PASSWORD).
- After doing the above task, you can then ensure that the provided DATABASE is actually created before running the application, else create it.
- If all the above is set, you are then done with setting up the database, and you can now run the application.

3. Running the application
- This project uses maven to manage its dependencies, please ensure you have maven installed
- Move into the project directory and execute the command `mvn spring-boot:run`.
- The application should be able to fire up shortly on default spring boot port *8080* .

4. Testing the application functionalities
- The technical challenge for this api is majorly on implementing the different discount options.
- In order to systematically solve this, I have added quite a number of activities that need to get done before computing discount. Below are the steps necessary before discount computation;
> - Sign up as a customer, or affiliate, or employee
> - Use your username and password to login to the application such that you get a `jwt` token in the response, that will be used to execute all other remaining requests.
> - Create product categories. Example of product category names can be `Groceries`, `Electronics`, `Scholastics`, `Hardware`, `etc`. When sending this request, send with a header `Authorization: Bearer <token>`, this applies to all the remaining requests as well.
> - Create products. Example of products include `Phone`, `Sugar`, `Books`, etc. While creating products, the product must be assigned to one of the previously created product category. Note: Any product that gets assigned to the `Groceries` category will not be given any of the *percentage-based discounts* .
> - Place an order for a product or products. Each product in the cart has a quantity attached. Upon placing your order for product(s), the response will show you how much is the totalCost of the ordered products, and how much is the discountedCost after deducting all the discounts based on the criteria listed in the guide. The response also shows the orderId which you can also use to query the order-details: The details of all the products that were in the cart for the orderId provided.
> - Kindly note that the above request for placing order also requires the use of `jwt` in the header as explained earlier, only then will the API know whether the user placing order is an ordinary customer, loyal customer, employee, or affiliate; and discounts will be given based on the same.
> 
> 4 endpoints to call before placing order (Signup, Login, Create Category, Create Product).

5. Sample Request and Responses
- ### Sign Up
#### Http Method: `POST`
#### URL: `http://localhost:8080/v1/users/signup`
#### Employee Request Body: 
```
{
	"username": "kidega",
	"firstName": "Bob",
	"lastName": "Kidega",
	"email": "kidega@gmail.com",
	"phoneNumber": "0786277076",
	"idType": "NATIONAL_ID",
	"idNumber": "CM92005105H8RF",
	"dateOfBirth": "28/09/1995",
	"address": "Koch",
	"password": "KidegaBob@123!",
	"employee": {
		"employeeNumber": "1234GH",
		"lineManager": "Ojara Martin",
		"employmentDate": "21/01/2022",
		"contractExpiryDate": "21/01/2024"
	}
}
```
#### Affiliate Request Body:

```
{
	"username": "okeny",
	"firstName": "Walter",
	"lastName": "Nyeko",
	"email": "nyeko@gmail.com",
	"phoneNumber": "0786277071",
	"idType": "NATIONAL_ID",
	"idNumber": "CM92005105H8RF",
	"dateOfBirth": "28/09/1992",
	"address": "Koro Abili",
	"password": "WalterNyeko@123!",
	"affiliate": {
		"affiliateNumber": "1234GH",
		"lineManager": "Okeny Justine",
		"contractStartDate": "21/01/2022",
		"contractEnddate": "21/01/2024",
		"affiliateCommission": 0.6
	}
}
```

#### Ordinary Customer Request Body:

```
{
	"username": "okeny",
	"firstName": "Walter",
	"lastName": "Nyeko",
	"email": "nyeko@gmail.com",
	"phoneNumber": "0786277071",
	"idType": "NATIONAL_ID",
	"idNumber": "CM920050105H8RF",
	"dateOfBirth": "28/09/1992",
	"address": "Koro Abili",
	"password": "WalterNyeko@123!"
}
```

#### Sample Success Response:

```
{
	"resourceId": 6
}
```

- ### Sign In

#### HttpMethod: `POST`
#### URL: `http://localhost:8080/v1/users/signin`
#### Sample Request Body:
```
{
	"username": "nyeko",
	"password": "WalterNyeko@123!"
}
```

#### Sample Response Body:

```
{
	"username": "nyeko",
	"firstName": "Walter",
	"lastName": "Nyeko",
	"idNumber": "CM92005105H8RF",
	"address": "Koro Abili",
	"email": "nyeko@gmail.com",
	"phoneNumber": "0786277071",
	"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJueWVrbyIsImV4cCI6MTY0NDMxMDM5MiwiaWF0IjoxNjQ0Mjc0MzkyfQ.7X2F4jhbAz1JzZrvg_SsmKRaKH5XYSsYV3SNam2x-YI"
}
```
### Create Category

#### HttpMethod: `POST`
#### URL: `http://localhost:8080/v1/products/category`
#### Header: `Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJueWVrbyIsImV4cCI6MTY0NDMxMDM5MiwiaWF0IjoxNjQ0Mjc0MzkyfQ.7X2F4jhbAz1JzZrvg_SsmKRaKH5XYSsYV3SNam2x-YI`
#### Sample Request Body:

```
{
	"name": "Hardware",
	"description": "Some description"
}
```

#### Sample Response Body:

```
{
	"resourceId": 1
}
```

### Create Product

#### HttpMethod: `POST`
#### URL: `http://localhost:8080/v1/products`
#### Header: `Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJueWVrbyIsImV4cCI6MTY0NDMxMDM5MiwiaWF0IjoxNjQ0Mjc0MzkyfQ.7X2F4jhbAz1JzZrvg_SsmKRaKH5XYSsYV3SNam2x-YI`
#### Sample request Body:

```
{
	"name": "Hoop Iron",
	"description": "Some description",
	"sellingPrice": 140.00,
	"productCategory": {
		"id": 1
	}
}
```

#### Sample Response Body:

```
{
	"resourceId": 1
}
```

### Place Order

#### HttpMethod: `POST`
#### URL: `http://localhost:8080/v1/orders`
#### Header: `Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJueWVrbyIsImV4cCI6MTY0NDMxMDM5MiwiaWF0IjoxNjQ0Mjc0MzkyfQ.7X2F4jhbAz1JzZrvg_SsmKRaKH5XYSsYV3SNam2x-YI`
#### Sample Request Body:

```
{
	"cartItems": [
		{
			"quantity": 2,
			"product": {
				"id": 1
			}
		},
		{
			"quantity": 2,
			"product": {
				"id": 2
			}
		}
	]
}
```

#### Sample response Body:

```
{
	"id": 27,
	"status": "PENDING",
	"totalCost": 3100.00,
	"discountedCost": 2790.00,
	"orderDate": "2022-02-07T23:15:29.083+00:00",
	"orderedBy": 1
}
```

6. Running unit and integration tests
- Use command `mvn clean test` to run test.
- I used `JaCoCo` to do instrumentation, documentation, and providing code coverage report.
- When you run `mvn clean test` the test coverage report will be stored in *target/sites/index.html*. You can open the html file in any browser and view the code coverage.
- If you use *IntelliJ IDEA*, you can just run the tests with coverage from the IDE directly and view the test suits with coverage on the IDE.

7. Documenting the API
- I had started integrating swagger to document the API endpoints.