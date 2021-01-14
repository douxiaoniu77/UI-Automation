'''
Created on Sep 14, 2018
@author: umartine

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-09-14   umartine    Initial creation
    2018-09-20   umartine    Add support for POST request
    2018-09-26   umartine    Support for new testsuite payload
    2018-09-27   umartine    Improve logging to use pretty format
    2018-11-12   pcamaril    Add support for path variables
    2018-11-14   ttazhang    Add support for R1 endpoint
    2018-11-19   umartine    Add header parameters to POST call
    2018-11-20   ttazhang    Add support for delete and put

'''

import requests
import json
import email.utils
from pprint import pformat

from oci_cli_qa.lib.logger import LOG
from SignedRequestAuth import SignedRequestAuth


def get_rest_headers():
    headers = {
        "content-type": "application/json",
        "date": email.utils.formatdate(usegmt = True)
    }
    return headers


def get_signed_request_auth_details(tenancy_ocid, user_ocid, api_key_footprint,
                                    private_key):
    LOG.info("[START] Generating Auth Details")
    LOG.info("Tenancy OCID: '{0}'".format(tenancy_ocid))
    LOG.info("User OCID: '{0}'".format(user_ocid))
    LOG.info("API key footprint: '{0}'".format(api_key_footprint))
    api_key = "/".join([tenancy_ocid, user_ocid, api_key_footprint])
    LOG.info("[END] Generating Auth Details")
    return SignedRequestAuth(api_key, private_key)


def get_endpoint(endpoints, endpoint_name):
    LOG.info("[START] Get endpoint")
    LOG.info("Endpoint name: '{0}'".format(endpoint_name))
    endpoint_out = None
    for endpoint in endpoints:
        if endpoint["name"] == endpoint_name:
            LOG.info("Endpoint found")
            endpoint_out = endpoint
    LOG.info("Endpoint to be returned:")
    LOG.info(endpoint_out)
    LOG.info("[END] Get endpoint")
    return endpoint_out


def get_account(accounts, account_name):
    LOG.info("[START] Get account")
    LOG.info("Account name: '{0}'".format(account_name))
    account_out = None
    for account in accounts:
        if account["name"] == account_name:
            LOG.info("Account found")
            account_out = account
    LOG.info("Account details:")
    LOG.info("Tenancy OCID: {0}".format(account["tenancy_ocid"]))
    LOG.info("User OCID: {0}".format(account["user_ocid"]))
    LOG.info("API key footprint: {0}".format(account["api_footprint"]))
    LOG.info("[END] Get account")
    return account_out

def get_path(test_uri_old, test_path):
    '''
    Method to substitute the values contained in the test path
    into the test_uri
    '''
    LOG.info("[START] Get path")
    test_uri_new = test_uri_old
    lst_variables = [path_variable for path_variable in test_uri_old.split("/") if "{" in path_variable]
    # Substitute every value contained in the test_path dictionary
    for path_key, path_value in test_path.iteritems():
        variable_to_change = "{" + path_key + "}"
        try:
            LOG.info("Trying to replace variable {0}".format(path_key))
            index_variable = lst_variables.index(variable_to_change)
        except ValueError:
            LOG.error("Variable {0} not in path dictionary".format(path_key))
            raise ValueError("Unknown variable {0}".format(path_key))
        test_uri_new = test_uri_new.replace(variable_to_change, path_value)
        lst_variables.remove(variable_to_change)
        LOG.info("Variable replaced: {0}".format(path_key))

    # After iterate through all variables in dictionary, test_uri should not contain
    # any bracket {}
    if len(lst_variables) > 0:
        LOG.err("Unknown variable: {0}".format(lst_variables[0]))
        raise ValueError("Unknown variable: {0}".format(lst_variables[0]))

    LOG.info("URI modified to be returned:{0}".format(test_uri_new))
    LOG.info("[END] Get path")
    return test_uri_new

def api_request(test_details, endpoints, accounts):
    LOG.info("[START] API Call")

    # Check request type
    LOG.info("Call type: '{0}'".format(test_details["request_type"]))

    # Get URI
    LOG.info("Endpoint name: '{0}'".format(test_details["endpoint"]))
    uri = get_endpoint(endpoints, test_details["endpoint"])["uri"]
    # Modify path with values given
    uri = get_path(uri, test_details["path"])

    # Get Authentication Details
    LOG.info("Account name: '{0}'".format(test_details["account"]))
    account = get_account(accounts, test_details["account"])
    signed_auth = get_signed_request_auth_details(account["tenancy_ocid"],
                                                  account["user_ocid"],
                                                  account["api_footprint"],
                                                  account["api_privatekey"])

    # Get headers
    headers = get_rest_headers()

    # Print Payload
    LOG.info("Path Payload: \n'{0}'".format(pformat(test_details["path"])))
    LOG.info("Header Payload: \n'{0}'".format(pformat(test_details["header"])))
    LOG.info("Body Payload: \n'{0}'".format(pformat(test_details["body"])))

    # Doing actual call

    # wa for R1 SSL issue , do not verify SSL for R1
    verify_ssl = True
    if uri.find('.r1.oracleiaas') >= 1:
        verify_ssl = False

    response = None
    if test_details["request_type"] == "GET":
        response = requests.get(uri,
                                auth = signed_auth,
                                headers = headers,
                                verify=verify_ssl,
                                params = test_details["header"])
    if test_details["request_type"] == "POST":
        response = requests.post(uri,
                                 auth = signed_auth,
                                 headers = headers,
                                 verify=verify_ssl,
                                 params=test_details["header"],
                                 data = json.dumps(test_details["body"]))
    if test_details["request_type"] == "DELETE":
        response = requests.delete(uri,
                                 auth = signed_auth,
                                 headers = headers,
                                 verify=verify_ssl)
    if test_details["request_type"] == "PUT":
        response = requests.put(uri,
                                 auth = signed_auth,
                                 headers = headers,
                                 verify=verify_ssl,
                                 params=test_details["header"],
                                 data = json.dumps(test_details["body"]))

    # Aggregating results
    response_status = response.status_code
    response_body = None
    try:
        response_body = response.json()
    except ValueError:
        LOG.info("Empty body received")
    results = {"status": response_status,
               "body" : response_body}

    LOG.info("Status Code: {0}".format(results["status"]))
    LOG.info("Response Body: \n'{0}'".format(pformat(results["body"],
                                                     indent = 4)))

    LOG.info("[END] API Call")
    return results
