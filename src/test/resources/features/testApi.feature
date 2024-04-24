Feature: test scenario to call mock

  @mock:service:test_request.json
  Scenario: Make request to mockService and get status code 200 as response
    Given Make request to server
    Then Response code is "200"