// Ulises Globals
var HUDSON_JOB_URL = "http://slcn03vmf0300.us.oracle.com:8899/job/TEST_REST_OCI_sample/build";
var TEST_SUITE_PAYLOAD = "payload_2.json";

var TEST_DETAILS_REST_CALL = {
			account: "account",
			endpoint: "endpoint",
			path: "json",
			header: "json",
			body: "json",
			request_type: "request_type",
			expected_response: "string"
		};

var REQUEST_TYPES = ["GET", "POST"];
var TEST_DETAILS_WAIT = {wait_time: "string"};

var TEST_DETAILS_LIST = {
		REST_CALL: TEST_DETAILS_REST_CALL,
		WAIT: TEST_DETAILS_WAIT
		};


// Test suite apps
var app = angular.module("testsuiteApp", []);

app.controller("testsuiteController", function($scope, $http) {
    // Globals
	$scope.test_types = Object.keys(TEST_DETAILS_LIST);
	$scope.request_types = REQUEST_TYPES;


	$scope.launchJob = function() {
		// Prepare HTTP Request and form data descriptor
		var XHR = new XMLHttpRequest();
    	var form_data_descriptor  = new FormData();

		// Create Payload file
		var testsuite_payload = $scope.printJson($scope.testsuite);
    	var testsuite_payload_file = new Blob([testsuite_payload], {type : 'text/plain'});

		// Fill file form data descriptor and send it to endpoint
    	form_data_descriptor.append("file0", testsuite_payload_file);
    	form_data_descriptor.append('json', '{"parameter": [{"name":"TEST_SUITE_FILE", "file":"file0"}]}');
    	XHR.open('POST', HUDSON_JOB_URL);
    	XHR.send(form_data_descriptor);

    	// Update UI Status and Disable Button
    	var status_div = document.getElementById('testsuite_status');
    	status_div.innerHTML = "Go to <a href='" + HUDSON_JOB_URL + "'>Hudson</a> to see your job";
    	var status_button = document.getElementById('testsuite_launch');
    	//status_button.disabled = true; 
    };

   // Get Test suite payload
   $http.get(TEST_SUITE_PAYLOAD).then(
		   function (response) {
			   $scope.testsuite = response.data;
			   $scope.showAccount($scope.testsuite.accounts[0]);
			   $scope.showTest($scope.testsuite.tests[0]);
			}
		 );


   // Endpoint Functions
   $scope.new_endpoint = {name: "", uri: ""};

   $scope.addEndpoint = function() {
        $scope.testsuite.endpoints.push(angular.copy($scope.new_endpoint));
		$scope.new_endpoint = {name: "", uri: ""};
    };

	$scope.removeEndpoint = function(endpoint) {
        var index = $scope.testsuite.endpoints.indexOf(endpoint);
		if (index > -1) {
			$scope.testsuite.endpoints.splice(index, 1);
		}
    };


    // Account Functions
    $scope.showAccount = function(account) {
    	$scope.current_account = account;
    };

    $scope.addAccount = function() {
    	new_account = {name: "New Account"};
    	$scope.testsuite.accounts.push(new_account);
    	$scope.current_account = new_account;
    };

    $scope.removeAccount = function(account) {
        var index = $scope.testsuite.accounts.indexOf(account);
		if (index > -1) {
			$scope.testsuite.accounts.splice(index, 1);
			$scope.showAccount($scope.testsuite.accounts[0]);
		}
    };


    // Account Functions
    $scope.showTest = function(test) {
    	$scope.current_test = test;
    	$scope.getDetails(test.type);
    	$scope.current_test_details = Object.keys(test.details);
    };

    $scope.removeTest = function(test) {
        var index = $scope.testsuite.tests.indexOf(test);
		if (index > -1) {
			$scope.testsuite.tests.splice(index, 1);
			$scope.showTest($scope.testsuite.tests[0]);
		}
    };

    $scope.addTest = function() {
    	new_test = {name: "new_test", type: "REST_CALL", details: {}};
    	$scope.testsuite.tests.push(new_test);
    	$scope.showTest(new_test);
    };
    
    // Update JSON strings
    $scope.getDetails = function(test_type) {
    	$scope.current_test_payloads_details = {};
    	for (key in TEST_DETAILS_LIST[test_type]) {
    		if (TEST_DETAILS_LIST[test_type][key] === "json") {
    			if (!(key in $scope.current_test.details)) {
    				$scope.current_test.details[key] = {};
    			}
    			json_string = JSON.stringify($scope.current_test.details[key],  null, 3);
    			$scope.current_test_payloads_details[key] = json_string;
    		} else {
    			if (!(key in $scope.current_test.details)) {
    				$scope.current_test.details[key] = "";
    			}
    		}
    	}
    };
    
    $scope.updateJsonDetails = function(test_type) {
    	for (key in TEST_DETAILS_LIST[test_type]) {
    		if (TEST_DETAILS_LIST[test_type][key] === "json") {
    			try {
    				jsonObject = JSON.parse($scope.current_test_payloads_details[key]);
    			} catch {
    				jsonObject = {};
    			}
    			$scope.current_test.details[key] = jsonObject;
    		}
    	}
    	$scope.getDetails(test_type);
    };
    
    // Filtering functions
    $scope.isKeyString = function(key) {
    	return $scope.isKeyExpected(key, "string");
    };
    
    $scope.isKeyEndpoint = function(key) {
    	return $scope.isKeyExpected(key, "endpoint");
    };

    $scope.isKeyAccount = function(key) {
    	return $scope.isKeyExpected(key, "account");
    };

    $scope.isKeyJson = function(key) {
    	return $scope.isKeyExpected(key, "json");
    };

    $scope.isKeyText = function(key) {
    	return $scope.isKeyExpected(key, "text");
    };

    $scope.isKeyRequestType = function(key) {
    	return $scope.isKeyExpected(key, "request_type");
    };    

    $scope.isKeyExpected = function(key, value) {
    	test_details = TEST_DETAILS_LIST[$scope.current_test.type];
    	return test_details[key] === value;
    };

    // Output functions
    $scope.printJson = function (myObject) {
    	return JSON.stringify(myObject, null, 3);
    };
});
