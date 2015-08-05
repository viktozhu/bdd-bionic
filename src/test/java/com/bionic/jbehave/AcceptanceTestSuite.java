package com.bionic.jbehave;

import com.bionic.utils.PropertyLoader;
import net.serenitybdd.jbehave.SerenityStories;
import org.jbehave.core.annotations.BeforeStories;

public class AcceptanceTestSuite extends SerenityStories {
    @BeforeStories
    public void setup() {
        PropertyLoader.loadPropertys();
    }
}
