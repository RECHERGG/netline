package dev.httpmarco.netline;

import dev.httpmarco.netline.tests.PacketRequestTest;
import dev.httpmarco.netline.tests.PacketTransmitTest;
import dev.httpmarco.netline.tests.SecurityListTest;
import dev.httpmarco.netline.tests.ServerClientBindingTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectClasses({
        ServerClientBindingTest.class,
        PacketTransmitTest.class,
        SecurityListTest.class,
        PacketRequestTest.class
})
@SuiteDisplayName("NetLine Test Suite")
public class NetLineTestSuite {
}
