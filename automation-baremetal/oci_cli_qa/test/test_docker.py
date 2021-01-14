import pytest
import sys

sys.path.append("/systemtests/automation-baremetal/oci_cli_qa")

TEST_CASES = [
    ("PIC_3_5", "3+5", 8),
    ("PIC_2_4", "2+4", 6),
    ("PIC_6_9", "6*9", 42),
]

@pytest.mark.parametrize("test_name,test_input,expected", TEST_CASES)
def test_eval(test_name, test_input, expected):
    if expected == 6:
        pytest.skip("Test case skipped")
    print "Running test: '{0}'".format(test_name)
    print "Evaluation {0}".format(test_input)
    print "Expected {0}".format(expected)
    assert eval(test_input) == expected
    open("{0}.suc".format(test_name), 'a').close()

