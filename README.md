# University Domains and Names API tests

## Getting Started

use mvn or mvnw for maven goals

* `mvn clean test` - run test pack

To generate allure report use one of the following command:

* `mvn allure:serve`

Report will be generated into temp folder. Web server with results will start.

* `mvn allure:report`

Report will be generated tо directory: `target/site/allure-maven/index.html`

**Test results and its description you can find in allure report in suits tab**

## Test pack:
1. INFO endpoint test

   Checking that info endpoint returns HTML with API information

2. SEARCH endpoint test without parameters

   Checking that search endpoint returns all universities with correct format

3. SEARCH endpoint test with domain parameter

   Checking that search endpoint with domain parameter will return only universities with specified domain

4. SEARCH Endpoint Test with name, country and domain parameter don't consider domain parameter

   Checking that search endpoint with name, country and domain parameter will return only universities with specified name part and country but don't consider domain

5. Search Endpoint Test Empty List Parameters

   Checking that search endpoint can return empty list

6. Update Endpoint Test

   Checking that update endpoint shows that dataset was updated