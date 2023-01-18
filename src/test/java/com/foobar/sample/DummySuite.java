package com.foobar.sample;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    DummyTest.class
})
public class DummySuite {

}
