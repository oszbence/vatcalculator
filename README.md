# VAT calculator

## Starting the app
To start the app, run VatCalculatorApplication.main(String[] args) method

## Accessing the app
To access the app, send an HTTP GET request to http://localhost:8080/calculator with the following parameters:
1) vatPercent: should be a valid Austrian VAT rate in percent (10, 13 or 20). This parameter is mandatory.
2) exactly one of the following 3 parameters: net, vatAmount, gross. These parameters should be greater than 0 and can contain at most 2 decimal fractional digits (scale).

## Examples

### Calculate from VAT amount
#### Request
http://localhost:8080/calculator?vatPercent=13&vatAmount=13
#### Response
{"net":100.00,"vatAmount":13,"gross":113.00,"vatPercent":13}

### Calculate from gross amount
#### Request
http://localhost:8080/calculator?vatPercent=13&gross=113
#### Response
{"net":100.00,"vatAmount":13.00,"gross":113,"vatPercent":13}

### Calculate from net amount
#### Request
http://localhost:8080/calculator?vatPercent=13&net=100
#### Response
{"net":100,"vatAmount":13.00,"gross":113.00,"vatPercent":13}

### Missing vatPercent
#### Request
http://localhost:8080/calculator?net=100
#### Response
{"status":"BAD_REQUEST","message":"Missing vatPercent"}

Additional test cases can be found and run in CalculatorServiceSpec.groovy
