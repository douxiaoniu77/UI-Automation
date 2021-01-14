'''
Created on Nov 19, 2018
@author: umartine

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-11-19   umartine    Initial creation

'''

from oci_cli_qa.lib.logger import LOG


def get_eval_exp(key_string):
    LOG.info("Original Key String: '{0}'".format(key_string))
    key_string_ar = key_string.split(".")
    for i in xrange(len(key_string_ar)):
        key_element = key_string_ar[i]
        key_element_split = key_element.split("[")
        firs_sub_element = True
        key_string_ar[i] = []
        for sub_element in key_element_split:
            if firs_sub_element:
                firs_sub_element = False
                key_string_ar[i].append(sub_element)
            else:
                key_string_ar[i].append("[{0}".format(sub_element))
    flattened_list = [y for x in key_string_ar for y in x]
    test_name = flattened_list.pop(0)
    for i in xrange(len(flattened_list)):
        item = flattened_list[i]
        if item[0] != '[':
            flattened_list[i] = "['{0}']".format(item)
    eval_exp = "".join(flattened_list)
    return (test_name, eval_exp)


def get_test(tests, test_name):
    test_details = None
    for test_details_aux in tests:
        if test_details_aux["name"] == test_name:
            test_details = test_details_aux
    return test_details


def get_test_desc_value(tests, key_string):
    LOG.info("[START] Get Previous Test Results")
    test_name, eval_exp = get_eval_exp(key_string)
    test_details = get_test(tests, test_name)
    desc_value = eval("test_details{0}".format(eval_exp))
    LOG.info("Description value is: '{0}'".format(desc_value))
    LOG.info("[End] Get Previous Test Results")
    return desc_value


def replace_values(tests, test_details_node):
    """ Check if node is an string """
    if isinstance(test_details_node, basestring):
        if test_details_node and test_details_node[0] == '{':
            test_details_node = get_test_desc_value(tests, test_details_node[1:-1])
    """ Check if node is a list """
    if isinstance(test_details_node, list):
        for i in xrange(len(test_details_node)):
            item = test_details_node[i]
            test_details_node[i] = replace_values(tests, item)
    """ Check if node is a dictionary """
    if isinstance(test_details_node, dict):
        for item in test_details_node:
            test_details_node[item] = replace_values(tests, test_details_node[item])
    """ Return node """
    return test_details_node
