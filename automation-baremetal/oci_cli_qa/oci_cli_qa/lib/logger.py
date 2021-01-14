'''
Created on Mar 9, 2018
@author: umartine

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-03-09   umartine    Initial creation
    2018-03-15   umartine    Improve STDOUT logging 

'''

import logging

FILE_NAME = "OCI_CLI_QA.log"
TAGS = "[%(asctime)s][%(filename)s:%(lineno)s][%(levelname)s]"
FORMAT = "{0}:%(message)s".format(TAGS)

LOG = logging.getLogger('model_repair')
formatter = logging.Formatter(FORMAT)

hdlr = logging.FileHandler(FILE_NAME)
hdlr.setFormatter(formatter)

stream_hdlr = logging.StreamHandler()
stream_hdlr.setFormatter(formatter)

LOG.addHandler(hdlr)
LOG.addHandler(stream_hdlr)
LOG.setLevel(logging.INFO)
