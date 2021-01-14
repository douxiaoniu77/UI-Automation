// Ulises Globals
var LIST_SERVICES_PAYLOAD = "dashboard.json";
var LIST_TAS_OPERATIONS_PAYLOAD = "tas_operations.json";

// Test suite apps
var app = angular.module("dashboardApp", []);

app.controller("dashboardController", function($scope, $http) {

	// Get Operations payload
	   $http.get(LIST_TAS_OPERATIONS_PAYLOAD).then(
			   function (response) {
				   $scope.operations = response.data;
				}
			 );
	   
   // Get Test suite payload
   $http.get(LIST_SERVICES_PAYLOAD).then(
		   function (response) {
			   $scope.services = response.data;
			}
		 );


   // Endpoint Functions
   $scope.addTestsuite = function(service) {
	   service.testsuites.push(angular.copy(service.new_testsuite));
	   service.new_testsuite = {name: ""};
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

});
