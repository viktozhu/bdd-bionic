package com.bionic.jbehave;

import net.serenitybdd.jbehave.SerenityStories;
import org.jbehave.core.annotations.BeforeStories;
import org.jruby.RubyProcess;

public class AcceptanceTestSuite extends SerenityStories {
    @BeforeStories
    public void doOnce(){
        System.out.println("Do it once");
    }
}
