// Ulises Globals
var LIST_SERVICES_PAYLOAD = "service.json";
var LIST_TEST_TYPE_PAYLOAD = "test_type.json";

// Test suite apps
var app = angular.module("dashboardApp", []);

app.controller("dashboardController", function($scope, $http) {

	// Get test type payload
	   $http.get(LIST_TEST_TYPE_PAYLOAD).then(
			   function (response) {
				   $scope.operations = response.data;
				   var test_type = $scope.operations[0];
				   $scope.new_testsuite = {type: test_type};
				}
			 );
	   
   // Get Test suite payload
   $http.get(LIST_SERVICES_PAYLOAD).then(
		   function (response) {
			   $scope.service = response.data;
			}
		 );


   // Endpoint Functions
   $scope.addTestsuite = function(service) {
	   service.testsuites.push(angular.copy($scope.new_testsuite));
	   var test_type = $scope.operations[0];
	   $scope.new_testsuite = {type: test_type};
    };

	$scope.removeTestsuite = function(service, testsuite) {
        var index = service.testsuites.indexOf(testsuite);
		if (index > -1) {
			service.testsuites.splice(index, 1);
		}
    };

    // Filtering Functions
    $scope.isCreationTC = function(testCaseType) {
    	return $scope.isExpectedTC(testCaseType, "creation");
    };
    $scope.isDeletionTC = function(testCaseType) {
    	return $scope.isExpectedTC(testCaseType, "deletion");
    };
    $scope.isSuspendTC = function(testCaseType) {
    	return $scope.isExpectedTC(testCaseType, "suspend");
    };

    $scope.isExpectedTC = function(testCaseType, value) {
    	return testCaseType.type === value;
    };

 // Output functions
    $scope.printJson = function (myObject) {
    	return JSON.stringify(myObject, null, 3);
    };

});
